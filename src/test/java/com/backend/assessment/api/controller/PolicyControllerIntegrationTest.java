package com.backend.assessment.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.backend.assessment.api.dto.response.ErrorResponse;
import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.dto.response.PolicySummaryResponse;
import com.backend.assessment.common.logging.CorrelationId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class PolicyControllerIntegrationTest {

    private static final String POLICIES_PATH = "/api/policies";
    private static final long SEEDED_POLICY_COUNT = 12L;
    private static final Set<String> REGION_DISPLAY_NAMES =
            Set.of("Singapore", "Hong Kong", "Australia", "India", "Japan");
    private static final Set<String> STATUS_DISPLAY_NAMES = Set.of("Active", "Lapsed");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PagedPolicyResponse getPage(String query) throws Exception {
        MvcResult result = mockMvc.perform(get(POLICIES_PATH + query)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        return objectMapper.readValue(body, PagedPolicyResponse.class);
    }

    @Test
    void getPolicies_withDefaultPaging_returnsFirstPageOfSeededData() throws Exception {
        PagedPolicyResponse response = getPage("");
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(SEEDED_POLICY_COUNT, response.totalElements());
        assertEquals(2, response.totalPages());
        assertEquals(10, response.content().size());
    }

    @Test
    void getPolicies_withExplicitPageSize_limitsContentAndPaginates() throws Exception {
        PagedPolicyResponse response = getPage("?page=0&size=5");
        assertEquals(5, response.size());
        assertEquals(5, response.content().size());
        assertEquals(SEEDED_POLICY_COUNT, response.totalElements());
        assertEquals(3, response.totalPages());
    }

    @Test
    void getPolicies_whenPoliciesReturned_mapRegionAndStatusToDisplayValues() throws Exception {
        PagedPolicyResponse response = getPage("?size=20");
        for (PolicySummaryResponse summary : response.content()) {
            assertTrue(REGION_DISPLAY_NAMES.contains(summary.region()));
            assertTrue(STATUS_DISPLAY_NAMES.contains(summary.status()));
        }
    }

    @Test
    void getPolicies_acrossAllSeededData_includesBothExpiringAndNonExpiringPolicies() throws Exception {
        PagedPolicyResponse response = getPage("?size=20");
        boolean anyExpiringSoon = response.content().stream().anyMatch(PolicySummaryResponse::isExpiringSoon);
        boolean anyNotExpiringSoon = response.content().stream().anyMatch(summary -> !summary.isExpiringSoon());
        assertTrue(anyExpiringSoon);
        assertTrue(anyNotExpiringSoon);
    }

    @Test
    void getPolicies_whenRequested_setsCorrelationIdResponseHeader() throws Exception {
        MvcResult result = mockMvc.perform(get(POLICIES_PATH)).andReturn();
        String correlationId = result.getResponse().getHeader(CorrelationId.HEADER_NAME);
        assertNotNull(correlationId);
        assertFalse(correlationId.isBlank());
    }

    @Test
    void request_whenResourceMissing_returnsNotFoundErrorWithCorrelationId() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/does-not-exist")).andReturn();
        assertEquals(404, result.getResponse().getStatus());
        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ErrorResponse error = objectMapper.readValue(body, ErrorResponse.class);
        assertEquals(404, error.status());
        assertNotNull(error.correlationId());
        assertFalse(error.correlationId().isBlank());
    }
}

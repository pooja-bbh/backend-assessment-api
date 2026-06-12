package com.backend.assessment.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import com.backend.assessment.api.dto.request.FlagPoliciesRequest;
import com.backend.assessment.api.dto.response.ErrorResponse;
import com.backend.assessment.api.dto.response.FlagResultResponse;
import com.backend.assessment.api.dto.response.PagedPolicyResponse;
import com.backend.assessment.api.dto.response.PolicyStatsResponse;
import com.backend.assessment.api.dto.response.PolicySummaryResponse;
import com.backend.assessment.common.logging.CorrelationId;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class PolicyControllerIntegrationTest {

    private static final String POLICIES_PATH = "/api/v1/policies";
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
        return objectMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), PagedPolicyResponse.class);
    }

    @Test
    void listPolicies_withDefaultPaging_returnsFirstPageOfSeededData() throws Exception {
        PagedPolicyResponse response = getPage("");
        assertEquals(0, response.page());
        assertEquals(10, response.size());
        assertEquals(SEEDED_POLICY_COUNT, response.totalElements());
        assertEquals(2, response.totalPages());
        assertEquals(10, response.content().size());
    }

    @Test
    void listPolicies_withExplicitPageSize_limitsContentAndPaginates() throws Exception {
        PagedPolicyResponse response = getPage("?page=0&size=5");
        assertEquals(5, response.content().size());
        assertEquals(3, response.totalPages());
    }

    @Test
    void listPolicies_whenReturned_mapDisplayValuesAndExposeNewFields() throws Exception {
        for (PolicySummaryResponse summary : getPage("?size=20").content()) {
            assertTrue(REGION_DISPLAY_NAMES.contains(summary.region()));
            assertTrue(STATUS_DISPLAY_NAMES.contains(summary.status()));
            assertNotNull(summary.id());
            assertNotNull(summary.lineOfBusiness());
        }
    }

    @Test
    void listPolicies_filteredByStatusActive_returnsOnlyActive() throws Exception {
        for (PolicySummaryResponse summary : getPage("?status=ACTIVE&size=20").content()) {
            assertEquals("Active", summary.status());
        }
    }

    @Test
    void listPolicies_filteredByLineOfBusiness_returnsOnlyThatLine() throws Exception {
        for (PolicySummaryResponse summary : getPage("?lineOfBusiness=LIFE&size=20").content()) {
            assertEquals("Life", summary.lineOfBusiness());
        }
    }

    @Test
    void listPolicies_searchByHolderName_matchesHolder() throws Exception {
        PagedPolicyResponse response = getPage("?q=Tan&size=20");
        assertTrue(response.totalElements() >= 1);
        for (PolicySummaryResponse summary : response.content()) {
            assertTrue(summary.holderName().toLowerCase().contains("tan"));
        }
    }

    @Test
    void getPolicyById_whenExists_returnsPolicy() throws Exception {
        PolicySummaryResponse first = getPage("?size=1").content().get(0);
        MvcResult result = mockMvc.perform(get(POLICIES_PATH + "/" + first.id())).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        PolicySummaryResponse fetched = objectMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), PolicySummaryResponse.class);
        assertEquals(first.id(), fetched.id());
    }

    @Test
    void getPolicyById_whenMissing_returnsNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get(POLICIES_PATH + "/99999999")).andReturn();
        assertEquals(404, result.getResponse().getStatus());
        ErrorResponse error = objectMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertEquals(404, error.status());
    }

    @Test
    void getSummary_returnsAggregatedStatistics() throws Exception {
        MvcResult result = mockMvc.perform(get(POLICIES_PATH + "/summary")).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        PolicyStatsResponse stats = objectMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), PolicyStatsResponse.class);
        assertFalse(stats.countsByStatus().isEmpty());
        assertFalse(stats.totalPremiumByLineOfBusiness().isEmpty());
        assertTrue(stats.expiringSoonCount() >= 1);
    }

    @Test
    void flagPolicies_flagsExistingAndReportsMissing() throws Exception {
        long existingId = getPage("?size=1").content().get(0).id();
        String body = objectMapper.writeValueAsString(
                new FlagPoliciesRequest(List.of(existingId, 99999999L)));
        MvcResult result = mockMvc.perform(patch(POLICIES_PATH + "/flag")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        FlagResultResponse flagResult = objectMapper.readValue(
                result.getResponse().getContentAsString(StandardCharsets.UTF_8), FlagResultResponse.class);
        assertEquals(2, flagResult.requested());
        assertEquals(1, flagResult.updated());
        assertTrue(flagResult.missingIds().contains(99999999L));
    }

    @Test
    void listPolicies_whenRequested_setsCorrelationIdResponseHeader() throws Exception {
        MvcResult result = mockMvc.perform(get(POLICIES_PATH)).andReturn();
        String correlationId = result.getResponse().getHeader(CorrelationId.HEADER_NAME);
        assertNotNull(correlationId);
        assertFalse(correlationId.isBlank());
    }
}

CREATE TABLE policies (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    policy_number    VARCHAR(50)    NOT NULL UNIQUE,
    holder_name      VARCHAR(200)   NOT NULL,
    region           VARCHAR(2)     NOT NULL,
    status           VARCHAR(20)    NOT NULL,
    premium_amount   NUMERIC(15, 2) NOT NULL,
    premium_currency VARCHAR(3)     NOT NULL,
    start_date       DATE           NOT NULL,
    end_date         DATE           NOT NULL
);

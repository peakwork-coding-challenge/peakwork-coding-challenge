CREATE TABLE resolved_place (
  osm_type      TEXT      NOT NULL,
  osm_id        BIGINT    NOT NULL,
  last_updated  TIMESTAMP NOT NULL,
  json          JSONB     NOT NULL,
  country_code  TEXT,
  CONSTRAINT resolved_place_pk PRIMARY KEY (osm_type, osm_id)
);

CREATE INDEX resolved_place_last_updated_idx ON resolved_place (last_updated);
CREATE INDEX resolved_place_country_code_idx ON resolved_place (country_code);

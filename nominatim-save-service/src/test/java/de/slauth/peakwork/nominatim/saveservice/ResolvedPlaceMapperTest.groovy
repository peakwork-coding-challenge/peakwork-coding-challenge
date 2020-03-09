package de.slauth.peakwork.nominatim.saveservice

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ResolvedPlaceMapperTest extends Specification {

    @Shared
    def objectMapper = new ObjectMapper()

    def mapper = new ResolvedPlaceMapper(new NominatimIdMapper())

    def 'maps osm_type #sourceOsmType to #targetOsmType'() {
        given:
            def source = objectMapper.readTree """{"osm_type": "$sourceOsmType", "osm_id": 4711}"""
        when:
            def result = mapper.apply source
        then:
            with(result.id) {
                osmType == targetOsmType
                osmId == 4711
            }
        where:
            sourceOsmType | targetOsmType
            'node'        | 'N'
            'way'         | 'W'
            'relation'    | 'R'
    }

    def 'maps address.country_code'() {
        given:
            def source = objectMapper.readTree '{"osm_type": "way", "osm_id": 280940520, "address": {"country_code": "ar"}}'
        when:
            def result = mapper.apply source
        then:
            result.countryCode == 'ar'
    }

    def 'throws IllegalArgumentException if #description'() {
        given:
            def source = objectMapper.readTree json
        when:
            mapper.apply source
        then:
            thrown(IllegalArgumentException)
        where:
            description                       | json
            'osm_type and osm_id are missing' | '{}'
            'osm_type is missing'             | '{"osm_id": 4711}'
            'osm_type is invalid'             | '{"osm_type": 42, "osm_id": 4711}'
            'osm_type is unknown'             | '{"osm_type": "foo"}'
            'osm_id is missing'               | '{"osm_type": "way"}'
            'osm_id is invalid'               | '{"osm_type": "way", "osm_id": "foo"}'
    }
}

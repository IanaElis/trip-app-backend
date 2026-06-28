package travel.itinerary.dto;

import travel.itinerary.entity.carrier.CompanyType;

public record CompanyDto(
        Long id,
        String name,
        CompanyType type
) {

}

package travel.itinerary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import travel.itinerary.dto.AirlineDto;
import travel.itinerary.dto.CompanyDto;
import travel.itinerary.entity.carrier.Airline;
import travel.itinerary.entity.carrier.Company;
import travel.itinerary.entity.transport.TransportType;

import java.util.List;

@Mapper(componentModel = "cdi",
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface CarrierMapper {
    Company toCompanyEntity(CompanyDto companyDto);
    CompanyDto toCompanyDto(Company company);
    CompanyDto toCompanyDto(Company company, TransportType transportType);
    List<CompanyDto> toCompanyDtoList(List<Company> companies);

    Airline toAirlineEntity(AirlineDto airlineDto);
    AirlineDto toAirlineDto(Airline airline);
    List<AirlineDto> toAirlineDtoList(List<Airline> airlines);
}

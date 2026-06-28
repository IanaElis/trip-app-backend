package travel.itinerary.service;

import travel.itinerary.dto.AirlineDto;
import travel.itinerary.dto.CompanyDto;
import travel.itinerary.entity.carrier.Airline;
import travel.itinerary.entity.carrier.Company;
import travel.itinerary.entity.transport.TransportType;

import java.util.List;

public interface CarrierService {
    //List<CarrierSuggestionDto> autocomplete(...);

    Company getOrCreateCompany(String name, TransportType transportType);
    AirlineDto createAirline(AirlineDto airline);

    CompanyDto updateCompany(CompanyDto company);
    AirlineDto updateAirline(AirlineDto airline);

    AirlineDto getAirlineByIataCode(String iataCode);
    Airline getAirline(String iataCode);
    List<AirlineDto> getAllAirlines();
    CompanyDto getCompanyDtoById(Long id);

    Company getCompany(Long id);

    Company getCompanyByName(String companyName);

    List<String> getAllCompaniesByType(String type);
    List<CompanyDto> getAllCompanies();

    void deleteCompany(String companyName);
    void deleteAirline(String iataCode);
}

package travel.itinerary.service.implementation;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import travel.itinerary.dto.AirlineDto;
import travel.itinerary.dto.CompanyDto;
import travel.itinerary.entity.carrier.Airline;
import travel.itinerary.entity.carrier.Company;
import travel.itinerary.entity.carrier.CompanyType;
import travel.itinerary.entity.transport.TransportType;
import travel.itinerary.mapper.CarrierMapper;
import travel.itinerary.repository.AirlineRepository;
import travel.itinerary.repository.CompanyRepository;
import travel.itinerary.service.CarrierService;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
class CarrierServiceImpl implements CarrierService {
    @Inject
    CompanyRepository companyRepository;
    @Inject
    AirlineRepository airlineRepository;
    @Inject
    CarrierMapper carrierMapper;

    @Transactional
    @Override
    public Company getOrCreateCompany(String name, TransportType transportType) {
        Company company = companyRepository.findByName(name);
        if (company == null) {
            company = new Company();

            company.setName(name);
            CompanyType type;
            switch(transportType){
                case BUS -> type = CompanyType.BUS;
                case CAR -> type = CompanyType.RENTAL;
                case TRAIN -> type = CompanyType.RAIL;
                default -> throw new IllegalArgumentException("Invalid transportType: " + transportType);
            }
            company.setType(type);
        }
        return company;
    }


    @Transactional
    @Override
    public AirlineDto createAirline(AirlineDto airline) {
        Airline entity = carrierMapper.toAirlineEntity(airline);
        return carrierMapper.toAirlineDto(entity);
    }

    @Transactional
    @Override
    public CompanyDto updateCompany(CompanyDto dto) {
        Company entity = companyRepository.findByName(dto.name());
        if (entity == null) {
            throw new NotFoundException();
        }
        if(dto.name() != null && !dto.name().equalsIgnoreCase(entity.getName())){
            entity.setName(dto.name());
        }
        if(dto.type()!=null && dto.type() != entity.getType())
            entity.setType(dto.type());
        return carrierMapper.toCompanyDto(entity);
    }

    @Override
    public AirlineDto updateAirline(AirlineDto dto) {
        Airline entity = airlineRepository.findByName(dto.name());
        if (entity == null) {
            throw new NotFoundException();
        }
        if(dto.name() != null && !dto.name().equalsIgnoreCase(entity.getName())){
            entity.setName(dto.name());
        }
        if(dto.iataCode()!=null && !dto.iataCode().equals(entity.getIataCode()))
            entity.setIataCode(dto.iataCode());
        return null;
    }

    @Override
    public AirlineDto getAirlineByIataCode(String iataCode) {
        Airline airline = airlineRepository.findAirlineByIataCode(iataCode)
                .orElseThrow(() -> new NotFoundException("Airline not found"));
        return carrierMapper.toAirlineDto(airline);
    }

    @Override
    public Airline getAirline(String iataCode) {
        return airlineRepository.findAirlineByIataCode(iataCode)
                .orElseThrow(() -> new NotFoundException("Airline not found"));
    }

    @Override
    public List<AirlineDto> getAllAirlines() {
        List<Airline> list = airlineRepository.listAll(Sort.ascending("name"));
        return carrierMapper.toAirlineDtoList(list);
    }

    @Override
    public CompanyDto getCompanyDtoById(Long id) {
        Company company = companyRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));
        return carrierMapper.toCompanyDto(company);
    }

    @Override
    public Company getCompany(Long id) {
        return companyRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));
    }

    @Override
    public Company getCompanyByName(String companyName){
        Company entity = companyRepository.findByName(companyName);
        if(entity == null){
            throw new NotFoundException("Company not found");
        }
        return entity;
    }

    @Override
    public List<CompanyDto> getAllCompaniesByType(CompanyType type) {
        List<Company> list = companyRepository.findByType(type);
        return carrierMapper.toCompanyDtoList(list);
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company> list = companyRepository.listAll(Sort.ascending("name"));
        return carrierMapper.toCompanyDtoList(list);
    }

    @Override
    public void deleteCompany(String companyName) {
        Company entity = companyRepository.findByName(companyName);
        if (entity != null) {
            companyRepository.delete(entity);
        }
        else{
            throw new NotFoundException("Company not found");
        }
    }

    @Override
    public void deleteAirline(String iataCode) {
        Airline entity = airlineRepository.findAirlineByIataCode(iataCode)
                .orElseThrow(() -> new NotFoundException("Airline not found"));
        airlineRepository.delete(entity);
    }
}

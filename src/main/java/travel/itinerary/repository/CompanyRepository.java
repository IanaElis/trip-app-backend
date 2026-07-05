package travel.itinerary.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.itinerary.entity.carrier.Company;
import travel.itinerary.entity.carrier.CompanyType;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CompanyRepository implements PanacheRepository<Company> {
    public Company findByName(String name){
        return find("name", name).firstResult();
    }

    public List<Company> findByType(CompanyType type){
        return find("type = ?1 order by name asc", type).list();
    }
}

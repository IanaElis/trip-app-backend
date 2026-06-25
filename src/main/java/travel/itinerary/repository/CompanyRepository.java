package travel.itinerary.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import travel.itinerary.entity.carrier.Company;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CompanyRepository implements PanacheRepository<Company> {
    public Company findByName(String name){
        return find("name", name).firstResult();
    }

    public List<Company> findByType(String type){
        return find("type = ?1 order by name asc", type).list();
    }
}

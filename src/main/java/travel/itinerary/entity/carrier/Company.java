package travel.itinerary.entity.carrier;

import jakarta.persistence.*;

@Entity
@Table(
        name = "companies"
//        indexes = {
//                @Index(name = "idx_company_type", columnList = "type")
//        }
)
public class Company{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private CompanyType type;

    public Company(String name, CompanyType type) {
        this.name = name;
        this.type = type;
    }

    public Company() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public CompanyType getType() {
        return type;
    }
}

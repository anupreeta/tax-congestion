package congestion.calculator.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToOne(mappedBy = "cityEntity", cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private CityTaxDays cityTaxDays;

    @OneToMany(mappedBy="cityEntity")
    private Set<CityHolidays> cityHolidays;

    @OneToOne(mappedBy = "cityEntity", cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private CityHolidayMonths cityHolidayMonths;

    @OneToMany(mappedBy="cityEntity")
    private Set<CityTaxCharges> cityTaxCharges;

    @OneToOne(mappedBy = "cityEntity", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private CityTaxRules cityTaxRules;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "city_vehicle",
            joinColumns = @JoinColumn(name = "city_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> taxExemptVehicles;
}

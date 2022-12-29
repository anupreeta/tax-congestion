package congestion.calculator.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city_tax_days")
public class CityTaxDays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_monday", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isMonday;

    @Column(name = "is_tuesday", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isTuesday;

    @Column(name = "is_wednesday", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isWednesday;

    @Column(name = "is_thursday", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isThursday;

    @Column(name = "is_friday", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isFriday;

    @Column(name = "is_saturday", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isSaturday;

    @Column(name = "is_sunday", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isSunday;

    @OneToOne
    @MapsId
    @JoinColumn(name = "city_id")
    private CityEntity cityEntity;

}

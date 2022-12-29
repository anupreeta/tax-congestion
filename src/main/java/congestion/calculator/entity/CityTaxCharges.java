package congestion.calculator.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city_tax_charges")
public class CityTaxCharges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "charge", nullable = false)
    private BigDecimal charge;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=false)
    private CityEntity cityEntity;

}

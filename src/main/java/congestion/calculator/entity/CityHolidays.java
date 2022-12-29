package congestion.calculator.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city_holidays")
public class CityHolidays {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name="city_id", nullable = false)
    private CityEntity cityEntity;

}

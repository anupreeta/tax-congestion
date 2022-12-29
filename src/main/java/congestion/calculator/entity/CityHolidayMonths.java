package congestion.calculator.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city_holiday_months")
public class CityHolidayMonths {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_january", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isJanuary;

    @Column(name = "is_february", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isFebruary;

    @Column(name = "is_march", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isMarch;

    @Column(name = "is_april", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isApril;

    @Column(name = "is_may", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isMay;

    @Column(name = "is_june", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isJune;

    @Column(name = "is_july", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isJuly;

    @Column(name = "is_august", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isAugust;

    @Column(name = "is_september", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isSeptember;

    @Column(name = "is_october", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isOctober;

    @Column(name = "is_november", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isNovember;

    @Column(name = "is_december", nullable = false, columnDefinition = "BIT", length = 1)
    private boolean isDecember;


    @OneToOne
    @MapsId
    @JoinColumn(name = "city_id")
    private CityEntity cityEntity;

}

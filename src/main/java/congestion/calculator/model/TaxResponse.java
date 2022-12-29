package congestion.calculator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
public class TaxResponse {

    private BigDecimal totalTax;
    private Map<String, BigDecimal> datewiseTaxCharges;
}

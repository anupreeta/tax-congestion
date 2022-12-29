package congestion.calculator.controller;

import congestion.calculator.exception.CustomException;
import congestion.calculator.model.TaxRequest;
import congestion.calculator.model.TaxResponse;
import congestion.calculator.service.CongestionTaxCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tax")
public class TaxController {

    private CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Autowired
    public TaxController(CongestionTaxCalculatorService congestionTaxCalculatorService) {
        this.congestionTaxCalculatorService = congestionTaxCalculatorService;
    }

    @PostMapping
    public ResponseEntity<TaxResponse> calculateCongestionTax(@RequestBody TaxRequest taxRequest,
                                                              @RequestHeader("city") String city) throws CustomException {
        congestionTaxCalculatorService.validateVehicle(taxRequest.getVehicle());
        congestionTaxCalculatorService.validateCity(city);
        TaxResponse result = congestionTaxCalculatorService.calculateTax(taxRequest.getVehicle(), taxRequest.getCheckInTime(), city);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
package congestion.calculator.controller;

import congestion.calculator.exception.CustomException;
import congestion.calculator.model.TaxRequest;
import congestion.calculator.model.TaxResponse;
import congestion.calculator.model.Vehicle;
import congestion.calculator.service.CongestionTaxCalculatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TaxControllerTest {

    @InjectMocks
    private TaxController taxController;

    @Mock
    private CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Test
    public void shouldReturnSuccessForValidInput() throws Exception {

        TaxRequest request = constructRequest("Car");
        TaxResponse taxResponse = TaxResponse.builder().totalTax(new BigDecimal(10)).build();
        Mockito.when(congestionTaxCalculatorService.calculateTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg")).thenReturn(taxResponse);
        ResponseEntity<TaxResponse> response = taxController.calculateCongestionTax(request, "Gothenburg");
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalTax()).isEqualTo(new BigDecimal(10));
    }

    @Test(expected = CustomException.class)
    public void shouldThrowErrorForInvalidVehicle() throws Exception {

        TaxRequest request = constructRequest("Space Ship");
        Mockito.doThrow(new CustomException("Invalid Vehicle")).when(congestionTaxCalculatorService).validateVehicle(request.getVehicle());
        taxController.calculateCongestionTax(request, "Gothenburg");
    }

    @Test(expected = CustomException.class)
    public void shouldThrowErrorForInvalidCity() throws Exception {

        TaxRequest request = constructRequest("Car");
        Mockito.doThrow(new CustomException("Invalid City")).when(congestionTaxCalculatorService).validateCity("Lund");
        taxController.calculateCongestionTax(request, "Lund");
    }

    private TaxRequest constructRequest(String vehicleType) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleType);
        List<Date> dateList = new ArrayList<>();
        dateList.add(new Date());
        TaxRequest request = new TaxRequest();
        request.setVehicle(vehicle);
        request.setCheckInTime(dateList);
        return request;
    }
}
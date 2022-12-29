package congestion.calculator.service;

import congestion.calculator.entity.*;
import congestion.calculator.model.TaxRequest;
import congestion.calculator.model.TaxResponse;
import congestion.calculator.model.Vehicle;
import congestion.calculator.repository.CityRepository;
import congestion.calculator.repository.VehicleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CongestionTaxCalculatorServiceTest {

    @Mock
    CityRepository cityRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @InjectMocks
    CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Test
    public void shouldReturnZeroWhenEmptyInput() throws ParseException {
        Date dateTime = getDateTime("2013-01-02 06:15:00");
        TaxRequest request = createRequest("Car", dateTime);
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getEmptyVehicleResponse());
        TaxResponse result = congestionTaxCalculatorService.calculateTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTotalTax()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void shouldReturnZeroInHolidayMonth() throws ParseException {
        Date dateTime = getDateTime("2013-07-02 10:20:00");
        TaxRequest request = createRequest("Car", dateTime);
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getEmptyVehicleResponse());
        TaxResponse result = congestionTaxCalculatorService.calculateTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTotalTax()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void shouldReturnSuccessForValidInput() throws ParseException {
        Date dateTime = getDateTime("2013-01-02 06:15:00");
        TaxRequest request = createRequest("Car", dateTime);
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getVehicleResponse());
        TaxResponse result = congestionTaxCalculatorService.calculateTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTotalTax()).isEqualTo(new BigDecimal(8));
    }

    @Test
    public void shouldReturnSuccessForValidInputDifferentTime() throws ParseException {
        Date dateTime = getDateTime("2013-03-05 15:15:00");
        TaxRequest request = createRequest("Car", dateTime);
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getVehicleResponse());
        TaxResponse result = congestionTaxCalculatorService.calculateTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTotalTax()).isEqualTo(new BigDecimal(13));
    }

    @Test
    public void shouldReturnSuccessForMultipleDateTimes() throws ParseException {
        List<Date> dateList = new ArrayList<>();
        Date dateTime1 = getDateTime("2013-01-14 07:30:00");
        Date dateTime2 = getDateTime("2013-01-14 15:33:27");
        Date dateTime3 = getDateTime("2013-01-14 16:40:00");
        Date dateTime4 = getDateTime("2013-02-08 06:27:00");
        Date dateTime5 = getDateTime("2013-02-08 06:20:27");
        Date dateTime6 = getDateTime( "2013-02-08 14:35:00");
        Date dateTime7 = getDateTime("2013-02-08 15:29:00");
        Date dateTime8 = getDateTime("2013-02-08 15:47:00");
        Date dateTime9 = getDateTime("2013-02-08 16:01:00");
        Date dateTime10 = getDateTime("2013-02-08 16:48:00");
        Date dateTime11 = getDateTime("2013-02-08 17:49:00");
        Date dateTime12 = getDateTime("2013-02-08 18:29:00");
        Date dateTime13 = getDateTime("2013-02-08 18:35:00");
        Date dateTime14 = getDateTime("2013-03-26 14:25:00");
        Date dateTime15 = getDateTime("2013-03-28 14:07:27");
        Date dateTime16 = getDateTime("2013-01-14 06:00:00");
        dateList.add(dateTime1);
        dateList.add(dateTime2);
        dateList.add(dateTime3);
        dateList.add(dateTime4);
        dateList.add(dateTime5);
        dateList.add(dateTime6);
        dateList.add(dateTime7);
        dateList.add(dateTime8);
        dateList.add(dateTime9);
        dateList.add(dateTime10);
        dateList.add(dateTime11);
        dateList.add(dateTime12);
        dateList.add(dateTime13);
        dateList.add(dateTime14);
        dateList.add(dateTime15);
        dateList.add(dateTime16);

        TaxRequest request = createRequestMultipleDates("Car", dateList);
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getVehicleResponse());
        TaxResponse result = congestionTaxCalculatorService.calculateTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTotalTax()).isEqualTo(new BigDecimal(128));
    }

    @Test
    public void shouldReturnSuccessForValidInputCurrentYear() throws ParseException {
        Date dateTime = getDateTime("2022-12-27 16:15:00");
        TaxRequest request = createRequest("Car", dateTime);
        Mockito.when(cityRepository.findByName("Gothenburg")).thenReturn(getVehicleResponse());
        TaxResponse result = congestionTaxCalculatorService.calculateTax(request.getVehicle(), request.getCheckInTime(), "Gothenburg");
        assertThat(result).isNotNull();
        assertThat(result.getTotalTax()).isEqualTo(new BigDecimal(18));
    }

    private Date getDateTime(String tripDateTime) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = formatter.parse(tripDateTime);
        return dateTime;
    }

    private TaxRequest createRequest(String vehicleType, Date arrivalTimes) throws ParseException {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleType);
        List<Date> dateList = new ArrayList<>();
        dateList.add(arrivalTimes);
        TaxRequest request = new TaxRequest();
        request.setCheckInTime(dateList);
        request.setVehicle(vehicle);
        return request;
    }

    private TaxRequest createRequestMultipleDates(String vehicleType, List<Date> dateList) throws ParseException {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleType);
        TaxRequest request = new TaxRequest();
        request.setCheckInTime(dateList);
        request.setVehicle(vehicle);
        return request;
    }

    private Optional<CityEntity> getEmptyVehicleResponse() {
        CityEntity cityEntity = CityEntity.builder().name("Gothenburg").id(1L).build();
        return Optional.of(cityEntity);
    }

    private Optional<CityEntity> getVehicleResponse() throws ParseException {
        CityEntity cityEntity = CityEntity.builder().name("Gothenburg").id(1L).build();

        createCalendarEntities(cityEntity);

        Set<CityTaxCharges> cityTaxCharges = new HashSet<>();
        CityTaxCharges cityTaxCharges1 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("06:00"))
                .endTime(LocalTime.parse("06:29:59"))
                .charge(BigDecimal.valueOf(8))
                .build();
        CityTaxCharges cityTaxCharges2 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("06:30"))
                .endTime(LocalTime.parse("06:59:59"))
                .charge(BigDecimal.valueOf(13))
                .build();
        CityTaxCharges cityTaxCharges3 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("07:00"))
                .endTime(LocalTime.parse("07:59:59"))
                .charge(BigDecimal.valueOf(18))
                .build();
        CityTaxCharges cityTaxCharges4 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("08:00"))
                .endTime(LocalTime.parse("08:29:59"))
                .charge(BigDecimal.valueOf(13))
                .build();
        CityTaxCharges cityTaxCharges5 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("08:30"))
                .endTime(LocalTime.parse("14:59:59"))
                .charge(BigDecimal.valueOf(8))
                .build();
        CityTaxCharges cityTaxCharges6 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("15:00"))
                .endTime(LocalTime.parse("15:29:59"))
                .charge(BigDecimal.valueOf(13))
                .build();
        CityTaxCharges cityTaxCharges7 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("15:30"))
                .endTime(LocalTime.parse("16:59:59"))
                .charge(BigDecimal.valueOf(18))
                .build();
        CityTaxCharges cityTaxCharges8 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("17:00"))
                .endTime(LocalTime.parse("17:59:59"))
                .charge(BigDecimal.valueOf(13))
                .build();
        CityTaxCharges cityTaxCharges9 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("18:00"))
                .endTime(LocalTime.parse("18:29:59"))
                .charge(BigDecimal.valueOf(8))
                .build();
        CityTaxCharges cityTaxCharges10 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("18:30"))
                .endTime(LocalTime.parse("23:59:59"))
                .charge(BigDecimal.valueOf(0))
                .build();
        CityTaxCharges cityTaxCharges11 = CityTaxCharges.builder()
                .cityEntity(cityEntity)
                .startTime(LocalTime.parse("00:00"))
                .endTime(LocalTime.parse("05:59:59"))
                .charge(BigDecimal.valueOf(0))
                .build();
        cityTaxCharges.add(cityTaxCharges1);
        cityTaxCharges.add(cityTaxCharges2);
        cityTaxCharges.add(cityTaxCharges3);
        cityTaxCharges.add(cityTaxCharges4);
        cityTaxCharges.add(cityTaxCharges5);
        cityTaxCharges.add(cityTaxCharges6);
        cityTaxCharges.add(cityTaxCharges7);
        cityTaxCharges.add(cityTaxCharges8);
        cityTaxCharges.add(cityTaxCharges9);
        cityTaxCharges.add(cityTaxCharges10);
        cityTaxCharges.add(cityTaxCharges11);

        cityEntity.setCityTaxCharges(cityTaxCharges);

        CityTaxRules cityTaxRules = CityTaxRules.builder()
                .numberOfTaxFreeDaysBeforeHoliday(1)
                .numberOfTaxFreeDaysAfterHoliday(1)
                .maxTaxPerDay(new BigDecimal(60))
                .singleChargePeriodMins(60)
                .build();
        cityEntity.setCityTaxRules(cityTaxRules);

        return Optional.of(cityEntity);
    }

    private void createCalendarEntities(CityEntity cityEntity) throws ParseException {
        CityTaxDays cityTaxDays = CityTaxDays.builder()
                .isSunday(false).isSaturday(false)
                .isMonday(true).isTuesday(true).isWednesday(true).isThursday(true).isFriday(true).build();
        cityEntity.setCityTaxDays(cityTaxDays);

        CityHolidayMonths cityHolidayMonths = CityHolidayMonths.builder()
                .isJanuary(false).isFebruary(false).isMarch(false).isApril(false).isMay(false).isJune(false)
                .isJuly(true).isAugust(false).isSeptember(false).isOctober(false).isNovember(false).isDecember(false).build();
        cityEntity.setCityHolidayMonths(cityHolidayMonths);

        Date dateTime = getDateTime("2013-03-28 00:00:00");
        CityHolidays cityHolidays = CityHolidays.builder().date(dateTime).cityEntity(cityEntity).build();
        Set<CityHolidays> holidays = new HashSet<>();
        holidays.add(cityHolidays);
        cityEntity.setCityHolidays(holidays);
    }
}
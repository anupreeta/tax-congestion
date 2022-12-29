package congestion.calculator.service;

import congestion.calculator.DateTimeUtil;
import congestion.calculator.entity.CityEntity;
import congestion.calculator.entity.CityTaxCharges;
import congestion.calculator.entity.Vehicle;
import congestion.calculator.exception.CustomException;
import congestion.calculator.model.TaxResponse;
import congestion.calculator.repository.CityRepository;
import congestion.calculator.repository.VehicleRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CongestionTaxCalculatorService {

    private CityRepository cityRepository;
    private VehicleRepository vehicleRepository;

    @Autowired
    public CongestionTaxCalculatorService(CityRepository cityRepository, VehicleRepository vehicleRepository) {
        this.cityRepository = cityRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public TaxResponse calculateTax(congestion.calculator.model.Vehicle vehicle, List<Date> dates, String city)
    {
        if(dates == null || dates.isEmpty())
            return TaxResponse.builder().totalTax(new BigDecimal(0)).build();

        CityEntity cityEntity = cityRepository.findByName(city).get();

        // check for tax-free vehicle
        if(isTollFreeVehicle(cityEntity.getTaxExemptVehicles(), vehicle))
            return TaxResponse.builder().totalTax(new BigDecimal(0)).build();

        Map<String, BigDecimal> datewiseTaxCharges = new HashMap<>();

        DateTimeUtil.sortDateByAsc(dates);

        // remove tax free days and holiday month
        dates.removeIf(date -> isTollFreeDate(date, cityEntity));

        // apply single charge rule
        Map<String, List<BigDecimal>> chargesPerDay = applySingleChargeRule(dates, cityEntity);

        // calculate total tax
        BigDecimal totalFee = calculateTotalTaxBySingleChargeRule(datewiseTaxCharges, cityEntity, chargesPerDay);

        return TaxResponse.builder().totalTax(totalFee).datewiseTaxCharges(datewiseTaxCharges).build();
    }

    @SneakyThrows
    private Map<String, List<BigDecimal>> applySingleChargeRule(List<Date> dates, CityEntity cityEntity) {
        List<Date> visitedSlots = new ArrayList<>();
        Map<String, List<BigDecimal>> result = new HashMap<>();
        for(int start = 0; start< dates.size(); start++) {
            // ignore duplicate date entries
            if(visitedSlots.contains(dates.get(start)))
                continue;
            // calculate tax for first date entry, single charge rule doesn't apply
            BigDecimal charge = calculateTollFeeByDateAndCharge(dates.get(start), cityEntity.getCityTaxCharges());
            for (int end = start + 1; end < dates.size(); end++) {
                long duration  = dates.get(end).getTime() - dates.get(start).getTime();
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                if(diffInMinutes <= cityEntity.getCityTaxRules().getSingleChargePeriodMins()) {
                    visitedSlots.add(dates.get(end));
                    BigDecimal temp = calculateTollFeeByDateAndCharge(dates.get(end), cityEntity.getCityTaxCharges());
                    if(temp.compareTo(charge) == 1) charge = temp;
                } else break;
            }
            calculateChargesByDate(dates, result, start, charge);
        }
        return result;
    }

    private BigDecimal calculateTotalTaxBySingleChargeRule(Map<String, BigDecimal> chargerHistoryPerDay, CityEntity cityEntity, Map<String, List<BigDecimal>> chargesPerDay) {
        BigDecimal totalFee = new BigDecimal(0);
        for (Map.Entry<String, List<BigDecimal>> entry : chargesPerDay.entrySet()) {
            BigDecimal totalChargePerDay = entry.getValue().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            if(cityEntity.getCityTaxRules() != null &&
                    cityEntity.getCityTaxRules().getMaxTaxPerDay() != null &&
                    totalChargePerDay.compareTo(cityEntity.getCityTaxRules().getMaxTaxPerDay()) == 1)
                totalChargePerDay = cityEntity.getCityTaxRules().getMaxTaxPerDay();
            chargerHistoryPerDay.put(entry.getKey(), totalChargePerDay);
            totalFee = totalFee.add(totalChargePerDay);
        }
        return totalFee;
    }

    private void calculateChargesByDate(List<Date> dates, Map<String, List<BigDecimal>> result, int start, BigDecimal charge) {
        String dateString = DateTimeUtil.removeTime(dates.get(start));
        List<BigDecimal> chargeLists;
        if(result.containsKey(dateString)) {
            chargeLists = result.get(dateString);
        } else {
            chargeLists = new ArrayList<>();
        }
        chargeLists.add(charge);
        result.put(dateString, chargeLists);
    }

    private BigDecimal calculateTollFeeByDateAndCharge(Date date, Set<CityTaxCharges> taxCharges) {
        BigDecimal totalFee = new BigDecimal(0);
        if(taxCharges == null || taxCharges.isEmpty()) return totalFee;

        for (CityTaxCharges cityTaxCharges : taxCharges) {
            LocalTime fromTime = cityTaxCharges.getStartTime();
            LocalTime toTime = cityTaxCharges.getEndTime();
            LocalTime source = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if(!source.isBefore(fromTime) && source.isBefore(toTime)) {
                return totalFee.add(cityTaxCharges.getCharge());
            }
        }

        return totalFee;
    }

    private boolean isTollFreeVehicle(Set<Vehicle> taxExemptVehicles, congestion.calculator.model.Vehicle vehicle) {
        if (taxExemptVehicles == null)
            return false;
        if(taxExemptVehicles.stream()
                .filter(taxExemptVehicle -> taxExemptVehicle.getName().equalsIgnoreCase(vehicle.getType())).count() > 0)
            return true;
        return false;
    }

    private Boolean isTollFreeDate(Date date, CityEntity cityEntity)
    {
        int month = date.getMonth() + 1;
        int day = date.getDay() + 1;

        if (DateTimeUtil.isWeekend(cityEntity.getCityTaxDays(), day))
            return true;
        if (DateTimeUtil.isTaxFreeDay(date, cityEntity))
            return true;
        if (DateTimeUtil.isHolidayMonth(cityEntity.getCityHolidayMonths(), month))
            return true;

        return false;
    }

    public void validateCity(String city) throws CustomException {
        if(cityRepository.findByName(city).isEmpty()) {
            throw new CustomException("City not found. Please enter a valid city", HttpStatus.NOT_FOUND);
        }
    }

    public void validateVehicle(congestion.calculator.model.Vehicle vehicle) throws CustomException {
        if(vehicleRepository.findByName(vehicle.getType()).isEmpty()) {
            throw new CustomException("Vehicle type not found. Please enter a valid vehicle", HttpStatus.NOT_FOUND);
        }
    }
}
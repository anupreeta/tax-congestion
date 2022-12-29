package congestion.calculator.model;

import congestion.calculator.DateTimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaxRequest {

    private Vehicle vehicle;
    private List<Date> checkInTime;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Date> getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(List checkInTime) {
        List<Date> localDateTimes = new ArrayList<>();
        if(checkInTime != null) {
            checkInTime.forEach(time -> {
                Date dateTime = null;
                try {
                    dateTime = DateTimeUtil.objectToDate(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                localDateTimes.add(dateTime);
            });
        }
        this.checkInTime = localDateTimes;
    }
}

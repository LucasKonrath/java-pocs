package org.example;

public class TaxiRide {
    private Boolean isNightSurcharge;
    private Long distanceInMile;

    public Boolean getNightSurcharge() {
        return isNightSurcharge;
    }

    public void setNightSurcharge(Boolean nightSurcharge) {
        isNightSurcharge = nightSurcharge;
    }

    public Long getDistanceInMile() {
        return distanceInMile;
    }

    public void setDistanceInMile(Long distanceInMile) {
        this.distanceInMile = distanceInMile;
    }
}

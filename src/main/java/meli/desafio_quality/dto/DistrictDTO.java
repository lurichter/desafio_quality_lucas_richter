package meli.desafio_quality.dto;

public class DistrictDTO {

    private String districtName;
    private double squareMeterValue;

    public DistrictDTO() {
    }

    public DistrictDTO(String districtName, double squareMeterValue) {
        this.districtName = districtName;
        this.squareMeterValue = squareMeterValue;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public double getSquareMeterValue() {
        return squareMeterValue;
    }

    public void setSquareMeterValue(double squareMeterValue) {
        this.squareMeterValue = squareMeterValue;
    }
}

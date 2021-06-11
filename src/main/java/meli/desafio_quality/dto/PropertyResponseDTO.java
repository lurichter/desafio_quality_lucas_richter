package meli.desafio_quality.dto;

import java.util.List;

public class PropertyResponseDTO {

    private double totalSquareMeters;
    private double propertyValue;
    private String biggestRoom;
    private List<RoomSquareMetersDTO> rooms;

    public PropertyResponseDTO() {
    }

    public PropertyResponseDTO(double totalSquareMeters, double propertyValue, String biggestRoom, List<RoomSquareMetersDTO> rooms) {
        this.totalSquareMeters = totalSquareMeters;
        this.propertyValue = propertyValue;
        this.biggestRoom = biggestRoom;
        this.rooms = rooms;
    }

    public double getTotalSquareMeters() {
        return totalSquareMeters;
    }

    public void setTotalSquareMeters(double totalSquareMeters) {
        this.totalSquareMeters = totalSquareMeters;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(double propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getBiggestRoom() {
        return biggestRoom;
    }

    public void setBiggestRoom(String biggestRoom) {
        this.biggestRoom = biggestRoom;
    }

    public List<RoomSquareMetersDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomSquareMetersDTO> rooms) {
        this.rooms = rooms;
    }
}

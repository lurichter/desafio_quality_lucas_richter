package meli.desafio_quality.dto;

public class RoomSquareMetersDTO {

    private String roomName;
    private double roomSquareMeters;

    public RoomSquareMetersDTO() {
    }

    public RoomSquareMetersDTO(String roomName, double roomSquareMeters) {
        this.roomName = roomName;
        this.roomSquareMeters = roomSquareMeters;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getRoomSquareMeters() {
        return roomSquareMeters;
    }

    public void setRoomSquareMeters(double roomSquareMeters) {
        this.roomSquareMeters = roomSquareMeters;
    }
}

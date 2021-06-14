package meli.desafio_quality.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RoomDTO {

    @NotBlank
    @Pattern(regexp = "\\p{Lu}[\\p{L}\\s0-9]*", message = "deve começar com letra maiúscula.")
    @Size(max = 30)
    private String roomName;
    @NotNull
    @Range(max = 25)
    private double roomWidth;
    @NotNull
    @Range(max = 33)
    private double roomLength;

    public RoomDTO() {
    }

    public RoomDTO(String roomName, double roomWidth, double roomLength) {
        this.roomName = roomName;
        this.roomWidth = roomWidth;
        this.roomLength = roomLength;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public double getRoomWidth() {
        return roomWidth;
    }

    public void setRoomWidth(double roomWidth) {
        this.roomWidth = roomWidth;
    }

    public double getRoomLength() {
        return roomLength;
    }

    public void setRoomLength(double roomLength) {
        this.roomLength = roomLength;
    }
}

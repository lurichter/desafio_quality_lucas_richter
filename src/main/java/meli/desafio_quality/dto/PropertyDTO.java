package meli.desafio_quality.dto;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class PropertyDTO {

    @Pattern(regexp = "[A-Z][\\w\\s]*", message = "deve começar com letra maiúscula.")
    @Size(min = 3, max = 30)
    @NotBlank
    private String name;
    @Size(min = 3, max = 45)
    @NotBlank
    private String district;
    private List<@Valid RoomDTO> rooms;

    public PropertyDTO() {
    }

    public PropertyDTO(String name, String district, List<RoomDTO> rooms) {
        this.name = name;
        this.district = district;
        this.rooms = rooms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDTO> rooms) {
        this.rooms = rooms;
    }
}

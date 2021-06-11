package meli.desafio_quality.unit;

import meli.desafio_quality.dto.PropertyDTO;
import meli.desafio_quality.dto.RoomDTO;
import meli.desafio_quality.dto.RoomSquareMetersDTO;
import meli.desafio_quality.exception.DistrictNotFoundException;
import meli.desafio_quality.repository.DistrictRepository;
import meli.desafio_quality.service.PropertyService;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class PropertyApiTest {

    DistrictRepository districtRepository = new DistrictRepository();
    PropertyService propertyService = new PropertyService(districtRepository);
    List<RoomDTO> rooms = new ArrayList<>();
    PropertyDTO propertyDTO = new PropertyDTO();

    @BeforeEach
    void setUp() {

        this.rooms.add(new RoomDTO("Sala", 3.7, 7.3));
        this.rooms.add(new RoomDTO("Cozinha", 3.2, 3.3));
        this.rooms.add(new RoomDTO("Banheiro Social", 1.3, 3.3));
        this.rooms.add(new RoomDTO("Quarto 1", 2.5, 4.0));
        this.rooms.add(new RoomDTO("Quarto 2", 3.6, 4.0));
        this.rooms.add(new RoomDTO("Suíte", 3.6, 4.0));
        this.rooms.add(new RoomDTO("Banheiro Suíte", 1.6, 3.3));

        this.propertyDTO.setName("Propriedade 1");
        this.propertyDTO.setDistrict("Pinheiros");
        this.propertyDTO.setRooms(this.rooms);
    }

    @Test
    void returnTotalSquareMetersWhenValidProperty() {
        Assertions.assertThat(85.94).isEqualTo(this.propertyService.getPropertySquareMeters(propertyDTO.getRooms()));
    }

    @Test
    void returnPropertyValueWhenDistrictFound() {
        try {
            Assertions.assertThat(1108626.0).isEqualTo(this.propertyService.getPropertyValue(propertyDTO));
        } catch (DistrictNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void returnExceptionWhenDistrictNotFound() {
        propertyDTO.setDistrict("Invalid District");
        Assertions.assertThatThrownBy(() -> {this.propertyService.getPropertyValue(propertyDTO);})
                .hasCauseInstanceOf(DistrictNotFoundException.class);
    }

    @Test
    void returnBiggestRoomWhenValidProperty() {
        Assertions.assertThat("Sala").isEqualTo(this.propertyService.getBiggestRoom(propertyDTO.getRooms()));
    }

    @Test
    void returnListOfRoomSquareMetersWhenValidProperty() {
        List<RoomSquareMetersDTO> actual = this.propertyService.getSquareMetersByRoom(propertyDTO.getRooms());

        Assertions.assertThat(actual)
                            .extracting(
                                    record -> record.getRoomName(),
                                    record -> record.getRoomSquareMeters())
                            .contains(Tuple.tuple("Sala", 27.01),
                                            Tuple.tuple("Cozinha", 10.56),
                                            Tuple.tuple("Banheiro Social", 4.29),
                                            Tuple.tuple("Quarto 1", 10.0),
                                            Tuple.tuple("Quarto 2", 14.4),
                                            Tuple.tuple("Suíte", 14.4),
                                            Tuple.tuple("Banheiro Suíte", 5.28));

    }

}

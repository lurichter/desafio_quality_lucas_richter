package meli.desafio_quality.service;

import meli.desafio_quality.dto.*;
import meli.desafio_quality.exception.DistrictNotFoundException;
import meli.desafio_quality.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    @Autowired
    private DistrictRepository districtRepository;

    public PropertyService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<RoomSquareMetersDTO> getSquareMetersByRoom (List<RoomDTO> rooms) {
        return rooms.stream().map(x -> new RoomSquareMetersDTO(x.getRoomName(),
                                                Math.round(x.getRoomWidth() * x.getRoomLength() * 100.0) / 100.0))
                                .collect(Collectors.toList());
    }

    public String getBiggestRoom(List<RoomDTO> rooms) {
        List<RoomSquareMetersDTO> squareMeters = getSquareMetersByRoom(rooms);
        RoomSquareMetersDTO biggestRoom = squareMeters.stream().max(Comparator.comparingDouble(x -> x.getRoomSquareMeters())).orElse(null);
        return biggestRoom.getRoomName();
    }

    public double getPropertySquareMeters(List<RoomDTO> rooms) {
        List<RoomSquareMetersDTO> squareMeters = getSquareMetersByRoom(rooms);
        return squareMeters.stream().mapToDouble(RoomSquareMetersDTO::getRoomSquareMeters).sum();
    }

    public double getPropertyValue(PropertyDTO property) throws DistrictNotFoundException {
        DistrictDTO district = this.districtRepository.getDistrictByName(property.getDistrict());
        if (district == null) {
            throw new DistrictNotFoundException("bairro não encontrado.");
        }
        double squareMeterValue = district.getSquareMeterValue();
        return getPropertySquareMeters(property.getRooms()) * squareMeterValue;
    }

    public PropertyResponseDTO computePropertyData(PropertyDTO property) throws DistrictNotFoundException{
        PropertyResponseDTO propertyData = new PropertyResponseDTO();
        propertyData.setTotalSquareMeters(getPropertySquareMeters(property.getRooms()));
        propertyData.setPropertyValue(getPropertyValue(property));
        propertyData.setBiggestRoom(getBiggestRoom(property.getRooms()));
        propertyData.setRooms(getSquareMetersByRoom(property.getRooms()));
        return propertyData;
    }


}

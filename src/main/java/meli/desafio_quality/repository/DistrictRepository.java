package meli.desafio_quality.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import meli.desafio_quality.dto.DistrictDTO;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DistrictRepository {

    private List<DistrictDTO> database;

    public DistrictRepository() {
        this.database = loadDatabase();
    }

    public List<DistrictDTO> getDatabase() {
        return database;
    }

    public DistrictDTO getDistrictByName(String districtName) {
        List<DistrictDTO> districts = loadDatabase();
        DistrictDTO district = null;
        if (districts != null) {
            district = districts.stream().filter(DistrictDTO -> DistrictDTO.getDistrictName().equals(districtName)).findFirst().orElse(null);
        }
        return district;
    }

    public void updateDatabase(List<DistrictDTO> database) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(ResourceUtils.getFile("classpath:districts.json"), database);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<DistrictDTO> loadDatabase() {
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:districts.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<DistrictDTO>> typeRef = new TypeReference<List<DistrictDTO>>(){};
        List<DistrictDTO> districts = null;
        try {
            districts = objectMapper.readValue(file, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (districts != null) {
            return districts;
        } else {
            return new ArrayList<DistrictDTO>();
        }

    }

}

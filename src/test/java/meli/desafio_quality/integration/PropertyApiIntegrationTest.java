package meli.desafio_quality.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import meli.desafio_quality.dto.DistrictDTO;
import meli.desafio_quality.dto.PropertyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@SpringBootTest
public class PropertyApiIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private String requestBody;

    @BeforeEach
    void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.requestBody = "{\n" +
                "    \"name\" : \"Propriedade 1\",\n" +
                "    \"district\" : \"Pinheiros\",\n" +
                "    \"rooms\" : [\n" +
                "        {\"roomName\": \"Sala\", \"roomWidth\" : 3.7, \"roomLength\" : 7.3},\n" +
                "        {\"roomName\": \"Cozinha\", \"roomWidth\" : 3.2, \"roomLength\" : 3.3},\n" +
                "        {\"roomName\": \"Banheiro Social\", \"roomWidth\" : 1.3, \"roomLength\" : 3.3},\n" +
                "        {\"roomName\": \"Quarto 1\", \"roomWidth\" : 2.5, \"roomLength\" : 4.0},\n" +
                "        {\"roomName\": \"Quarto 2\", \"roomWidth\" : 3.6, \"roomLength\" : 4.0},\n" +
                "        {\"roomName\": \"Suíte\", \"roomWidth\" : 3.6, \"roomLength\" : 4.0},\n" +
                "        {\"roomName\": \"Banheiro Suíte\", \"roomWidth\" : 1.6, \"roomLength\" : 3.3}\n" +
                "    ]\n" +
                "}";

    }

    @Test
    void returnTotalSquareMetersWhenValidProperty() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalSquareMeters").value(85.94));

    }

    @Test
    void returnPropertyValueWhenDistrictFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.propertyValue").value(1108626.0));
    }

    @Test
    void returnDistrictNotFoundMessageWhenDistrictNotFound() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.setDistrict("Invalid District");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.field").value("district"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("bairro não encontrado."));
    }

    @Test
    void returnBiggestRoomWhenValidProperty() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.biggestRoom").value("Sala"));
    }

    @Test
    void returnListOfRoomSquareMetersWhenValidProperty() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[0].roomName").value("Sala"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[0].roomSquareMeters").value(27.01))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[1].roomName").value("Cozinha"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[1].roomSquareMeters").value(10.56))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[2].roomName").value("Banheiro Social"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[2].roomSquareMeters").value(4.29))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[3].roomName").value("Quarto 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[3].roomSquareMeters").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[4].roomName").value("Quarto 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[4].roomSquareMeters").value(14.4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[5].roomName").value("Suíte"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[5].roomSquareMeters").value(14.4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[6].roomName").value("Banheiro Suíte"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rooms[6].roomSquareMeters").value(5.28));
    }

}

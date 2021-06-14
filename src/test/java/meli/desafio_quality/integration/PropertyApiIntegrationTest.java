package meli.desafio_quality.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import meli.desafio_quality.dto.DistrictDTO;
import meli.desafio_quality.dto.PropertyDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
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

    @Test
    void returnNameMustNotBeBlankMessageWhenPropertyNameNotProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.setName("");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'name' && @.message == 'não deve estar em branco')]").exists());

    }

    @Test
    void returnInvalidNameLengthMessageWhenPropertyInvalidNameLengthProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.setName("Ab");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'name' && @.message == 'tamanho deve ser entre 3 e 30')]").exists());

        property.setName("Propriedade com quantidade grande de caracteres");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'name' && @.message == 'tamanho deve ser entre 3 e 30')]").exists());
    }

    @Test
    void returnNameMustBeCapitalizedMessageWhenPropertyNameNotCapitalized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.setName("propriedade 1");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'name' && @.message == 'deve começar com letra maiúscula.')]").exists());

    }

    @Test
    void returnDistrictMustNotBeBlankMessageWhenPropertyDistrictNotProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.setDistrict("");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'district' && @.message == 'não deve estar em branco')]").exists());
    }

    @Test
    void returnInvalidDistrictLengthMessageWhenPropertyInvalidDistrictLengthProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.setDistrict("Ab");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'district' && @.message == 'tamanho deve ser entre 3 e 45')]").exists());

        property.setDistrict("Distrito de propriedade quantidade muito grande de caracteres");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'district' && @.message == 'tamanho deve ser entre 3 e 45')]").exists());
    }

    @Test
    void returnRoomNameMustNotBeBlankMessageWhenPropertyRoomNameNotProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.getRooms().get(0).setRoomName("");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomName' && @.message == 'não deve estar em branco')]").exists());
    }

    @Test
    void returnRoomNameMustBeCapitalizedMessageWhenPropertyRoomNameNotCapitalized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.getRooms().get(0).setRoomName("sala");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomName' && @.message == 'deve começar com letra maiúscula.')]").exists());
    }

    @Test
    void returnInvalidRoomNameLengthMessageWhenPropertyInvalidRoomNameLengthProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.getRooms().get(0).setRoomName("Sa");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomName' && @.message == 'tamanho deve ser entre 3 e 30')]").exists());

        property.getRooms().get(0).setRoomName("Sala com número de caracteres muito grande");
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomName' && @.message == 'tamanho deve ser entre 3 e 30')]").exists());
    }

    @Test
    void returnInvalidRoomWidthMessageWheInvalidPropertyRoomWidthProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.getRooms().get(0).setRoomWidth(0.0);
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomWidth' && @.message == 'deve estar entre 1 e 25')]").exists());

        property.getRooms().get(0).setRoomWidth(26.0);
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomWidth' && @.message == 'deve estar entre 1 e 25')]").exists());

    }

    @Test
    void returnInvalidRoomLengthMessageWheInvalidPropertyRoomLengthProvided() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<PropertyDTO> typeRef = new TypeReference<PropertyDTO>(){};
        PropertyDTO property = objectMapper.readValue(this.requestBody, typeRef = typeRef);
        property.getRooms().get(0).setRoomLength(0.0);
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomLength' && @.message == 'deve estar entre 1 e 33')]").exists());

        property.getRooms().get(0).setRoomLength(34.0);
        this.requestBody = objectMapper.writeValueAsString(property);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/property")
                .locale(LocaleContextHolder.getLocale())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.field == 'rooms[0].roomLength' && @.message == 'deve estar entre 1 e 33')]").exists());
    }

}

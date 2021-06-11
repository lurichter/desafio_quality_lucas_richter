package meli.desafio_quality.controller;

import meli.desafio_quality.dto.PropertyDTO;
import meli.desafio_quality.dto.PropertyResponseDTO;
import meli.desafio_quality.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public ResponseEntity<PropertyResponseDTO> computePropertyData(@RequestBody @Valid PropertyDTO property) throws Exception {
        return new ResponseEntity<PropertyResponseDTO>(propertyService.computePropertyData(property), HttpStatus.OK);
    }
}

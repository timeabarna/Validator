package com.idomsoft.personvalidator.controller;

import com.idomsoft.personvalidator.service.PersonValidatorService;
import com.idomsoft.personvalidator.model.PersonValidatorRequest;
import com.idomsoft.personvalidator.model.PersonValidatorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/person-validation")
@Validated
public class PersonValidatorController {
    @Autowired private PersonValidatorService personValidatorService;

    @PostMapping
    public PersonValidatorResponse validateAndExtendSzemelyDTO(@Valid @RequestBody PersonValidatorRequest personRequest) {
        return personValidatorService.validateAndExtendSzemelyDTO(personRequest);
    }
}

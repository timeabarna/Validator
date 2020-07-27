package com.idomsoft.validatormain.controller;

import com.idomsoft.validatormain.service.ValidatorService;
import com.idomsoft.validatormain.model.PersonValidatorResponse;
import com.idomsoft.validatormain.model.SzemelyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/validation")
@Validated
public class ValidatorController {
    @Autowired
    ValidatorService validatorService;

    @PostMapping
    public PersonValidatorResponse validatePersonWithIdList(@Valid @RequestBody SzemelyDTO requestPerson) {
        return validatorService.validateAndExtendPersonAndIdList(requestPerson);
    }
}

package com.idomsoft.validatormain.service;

import com.idomsoft.validatormain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;

@Service
public class ValidatorService {
        @Value("${person.validation.url}")
        String personValidationUrl;

        @Value("${id.validation.url}")
        String idValidationUrl;

        @Autowired
        RestTemplate restTemplate;

        public PersonValidatorResponse validateAndExtendPersonAndIdList(SzemelyDTO personToBeValidated) {
                IdValidatorRequest idRequest = new IdValidatorRequest(personToBeValidated.getOkmLista(), new ArrayList<>());
                IdValidatorResponse idResponse = restTemplate.postForObject(idValidationUrl, idRequest, IdValidatorResponse.class);
                personToBeValidated.setOkmLista(idResponse.getIdList());
                PersonValidatorRequest personRequest = new PersonValidatorRequest(personToBeValidated, idResponse.getValidationErrors());
                return restTemplate.postForObject(personValidationUrl, personRequest, PersonValidatorResponse.class);
        }
}
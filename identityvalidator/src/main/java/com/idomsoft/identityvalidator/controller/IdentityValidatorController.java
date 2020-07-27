package com.idomsoft.identityvalidator.controller;

import com.idomsoft.identityvalidator.service.IdentityValidatorService;
import com.idomsoft.identityvalidator.model.IdValidatorRequest;
import com.idomsoft.identityvalidator.model.IdValidatorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/id-validation")
@Validated
public class IdentityValidatorController {
    @Autowired
    private IdentityValidatorService identityValidatorService;

    @PostMapping
    public IdValidatorResponse validateAndExtendId(@Valid @RequestBody IdValidatorRequest idRequest) throws IOException {
        return identityValidatorService.validateAndExtendOkmanyDTO(idRequest);
    }
}

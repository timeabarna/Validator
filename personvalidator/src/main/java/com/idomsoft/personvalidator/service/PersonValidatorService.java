package com.idomsoft.personvalidator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idomsoft.personvalidator.errorhandling.ErrorMessage;
import com.idomsoft.personvalidator.model.PersonValidatorRequest;
import com.idomsoft.personvalidator.model.PersonValidatorResponse;
import com.idomsoft.personvalidator.model.SzemelyDTO;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.*;

@Service
public class PersonValidatorService {
    private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("[a-zäíáéűúőóüöA-ZÄÍÁÉŰÚŐÓÜÖ\\.\\/\\- ']*");
    private final Map<String, String> nationalityCodeToCountry = new HashMap<>();

    @PostConstruct
    private void init() throws IOException, URISyntaxException {
        if (nationalityCodeToCountry.isEmpty()){
            String json = readJsonFromFile();
            convertJsonStringToNationalityCodeToCountryMap(json);
        }
    }

    private String readJsonFromFile() throws IOException, URISyntaxException {
        return Files.lines(Paths.get(getClass()
                        .getClassLoader()
                        .getResource("kodszotar21_allampolg.json")
                        .toURI()))
                        .collect(joining("\n"));
    }

    private void convertJsonStringToNationalityCodeToCountryMap(String json) throws IOException {
        JsonNode rows = new ObjectMapper().readTree(json).get("rows");
        rows.iterator().forEachRemaining(element ->
                nationalityCodeToCountry.put(element.get("kod").asText(), element.get("allampolgarsag").asText())
        );
    }

    public PersonValidatorResponse validateAndExtendSzemelyDTO(PersonValidatorRequest personRequest) {
        PersonValidatorResponse response = new PersonValidatorResponse();

        response.setPerson(personRequest.getPerson());
        decodeNationality(response.getPerson());
        response.setValidationErrors(validate(personRequest));

        return response;
    }

    private void decodeNationality(SzemelyDTO personRequest) {
        personRequest.setAllampDekod(nationalityCodeToCountry.get(personRequest.getAllampKod()));
    }

    private List<String> validate(PersonValidatorRequest personRequest) {
        SzemelyDTO person = personRequest.getPerson();
        List<String> errors = personRequest.getValidationErrors();

        logErrorFor(validateNationality(person.getAllampKod()), errors, ErrorMessage.INVALID_NAT_CODE.getErrorMessage());
        logErrorFor(isNationalityDecoded(person.getAllampDekod()), errors, ErrorMessage.INVALID_NAT_DECODE.getErrorMessage());
        logErrorFor(validateGender(person.getNeme()), errors, ErrorMessage.INVALID_GENDER.getErrorMessage());
        logErrorFor(validateBirthDate(person.getSzulDat()), errors, ErrorMessage.INVALID_BIRTH_DATE.getErrorMessage());
        logErrorFor(validateName(person.getaNev()), errors, ErrorMessage.INVALID_MOTHERS_NAME.getErrorMessage());
        logErrorFor(validateName(person.getVisNev()), errors, ErrorMessage.INVALID_NAME.getErrorMessage());
        logErrorFor(validateName(person.getSzulNev()), errors, ErrorMessage.INVALID_BIRTH_NAME.getErrorMessage());

        return errors;
    }

    private void logErrorFor(boolean isValid, List<String> validationErrorList, String errorMessage) {
        if (!isValid) {
            validationErrorList.add(errorMessage);
        }
    }

    private boolean validateNationality(String nationalityCode) {
        return nationalityCodeToCountry.containsKey(nationalityCode);
    }

    private boolean isNationalityDecoded(String nationalityDecode) {
        return nationalityDecode != null;
    }

    private boolean validateGender(String gender) {
        return Objects.equals("F", gender) || Objects.equals("N", gender);
    }

    private boolean validateBirthDate(Date birthDate) {
        LocalDate dateToCompare = birthDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        Period diff = Period.between(dateToCompare, LocalDate.now());
        return diff.getYears() >= 18 && diff.getYears() <= 120;
    }

    private boolean validateName(String name) {
        List<String> names = Arrays.asList(
                name.replaceAll("Dr.", "")
                        .trim()
                        .split("\\s"));
        return name.length() <= 80 && names.size() >= 2 &&
                names.stream().allMatch(item -> ALLOWED_CHARACTERS.matcher(item).matches());
    }
}

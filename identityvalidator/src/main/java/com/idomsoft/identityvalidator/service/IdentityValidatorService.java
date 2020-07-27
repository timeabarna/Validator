package com.idomsoft.identityvalidator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idomsoft.identityvalidator.error.ErrorMessage;
import com.idomsoft.identityvalidator.error.InvalidImageException;
import com.idomsoft.identityvalidator.model.IdValidatorRequest;
import com.idomsoft.identityvalidator.model.IdValidatorResponse;
import com.idomsoft.identityvalidator.model.OkmanyDTO;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.*;

@Service
public class IdentityValidatorService {
    private final Map<String, Pattern> idTypesToValidationPattern = new HashMap<>();

    @PostConstruct
    private void init() throws IOException, URISyntaxException {
        if (idTypesToValidationPattern.isEmpty()){
            String json = readJsonFromFile();
            convertJsonStringToIdTypesToValidationPatternMap(json);
        }
    }

    private String readJsonFromFile() throws IOException, URISyntaxException {
        return Files.lines(Paths.get(getClass()
                .getClassLoader()
                .getResource("kodszotar46_okmanytipus.json")
                .toURI()))
                .collect(joining("\n"));
    }

    private void convertJsonStringToIdTypesToValidationPatternMap(String json) throws IOException {
        JsonNode rows = new ObjectMapper().readTree(json).get("rows");
        rows.iterator().forEachRemaining(element ->
                idTypesToValidationPattern.put(element.get("kod").asText(),
                    IdNumberValidationPattern.valueOf("ID" + element.get("kod").asText()).getValidationPattern()));
    }

    public IdValidatorResponse validateAndExtendOkmanyDTO(IdValidatorRequest idRequest) {
        IdValidatorResponse response = new IdValidatorResponse();

        for (OkmanyDTO id : idRequest.getIdList()) {
           determineExpirationValidity(id);
           validate(id, idRequest.getValidationErrors());
        }
        validateThatOneIdIsActiveOnlyByType(idRequest.getIdList(), idRequest.getValidationErrors());

        response.setIdList(idRequest.getIdList());
        response.setValidationErrors(idRequest.getValidationErrors());
        return response;
    }

    private void determineExpirationValidity(OkmanyDTO idRequest) {
        LocalDate dateToCompare = idRequest.getLejarDat().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        idRequest.setErvenyes(dateToCompare.isAfter(LocalDate.now()));
    }

    private void validate(OkmanyDTO idRequest, List<String> errors) {
        logErrorFor(validateIdType(idRequest.getOkmTipus()), errors,
                ErrorMessage.INVALID_ID_CODE.getErrorMessage() + idRequest.getOkmanySzam());
        logErrorFor(validateIdNumber(idRequest.getOkmTipus(), idRequest.getOkmanySzam()),errors,
                ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() + idRequest.getOkmanySzam());
        logErrorFor(validateImageFormat(idRequest.getOkmanyKep()), errors,
                ErrorMessage.INVALID_IMAGE_FORMAT.getErrorMessage() + idRequest.getOkmanySzam());
        logErrorFor(validateImageSize(idRequest.getOkmanyKep()), errors,
                ErrorMessage.INVALID_IMAGE_SIZE.getErrorMessage() + idRequest.getOkmanySzam());
    }

    private void logErrorFor(boolean isValid, List<String> validationErrorList, String errorMessage) {
        if (!isValid) {
            validationErrorList.add(errorMessage);
        }
    }

    private boolean validateIdType(String IdTypeCode) {
        return idTypesToValidationPattern.containsKey(IdTypeCode);
    }

    private boolean validateIdNumber(String idType, String idNumber) {
        Pattern pattern = idTypesToValidationPattern.get(idType);
        return null != pattern && pattern.matcher(idNumber).matches();
    }

    private void validateThatOneIdIsActiveOnlyByType(List<OkmanyDTO> idList, List<String> errors) {
        logErrorFor(validateIdentificationList(idList), errors,
                ErrorMessage.INVALID_MULTIPLE_ID_ACTIVE.getErrorMessage());
    }

    private boolean validateImageFormat(byte[] image) {
        try {
        Iterator<ImageReader> readers = ImageIO.getImageReaders(getImageInputStream(image));
        ImageReader read = readers.next();
            return Objects.equals("JPEG", read.getFormatName());
        } catch (IOException | NoSuchElementException e) {
            throw new InvalidImageException("Invalid image");
        }
    }

    private boolean validateImageSize(byte[] image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(getImageInputStream(image));
            return 1063 == bufferedImage.getHeight() && 827 == bufferedImage.getWidth();
        } catch (IOException e) {
            throw new InvalidImageException("Invalid image");
        }
    }

    private ImageInputStream getImageInputStream(byte[] image) throws IOException {
        return ImageIO.createImageInputStream(new ByteArrayInputStream(image));
    }

    private boolean validateIdentificationList(List<OkmanyDTO> identificationList) {
        return identificationList.stream()
                .collect(groupingBy(key -> key.getOkmTipus() + key.isErvenyes(), counting()))
                .entrySet()
                .stream()
                .filter(key -> key.getKey().contains(Boolean.TRUE.toString()))
                .allMatch(item -> item.getValue() == 1);
    }
}

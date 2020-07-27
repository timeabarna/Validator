package com.idomsoft.identityvalidator.service;

import com.idomsoft.identityvalidator.error.ErrorMessage;
import com.idomsoft.identityvalidator.model.IdValidatorRequest;
import com.idomsoft.identityvalidator.model.OkmanyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IdentityValidatorServiceTest {
    private static final byte[] GOOD_IMAGE = ImageUtil.createByteArrayFromImage("arckep_jo.jpg", "jpg");
    private static final byte[] BAD_IMAGE_SIZE = ImageUtil.createByteArrayFromImage("arckep_rosz.jpg", "jpg");
    private static final byte[] BAD_IMAGE_FORMAT = ImageUtil.createByteArrayFromImage("arckep_jo.jpg", "png");
    private static final Date DATE_IN_THE_PAST = convertLocalDateToDate(LocalDate.now().minusDays(10L));
    private static final Date DATE_IN_THE_FUTURE = convertLocalDateToDate(LocalDate.now().plusYears(1L));
    private static final int ID_INDEX = 0;
    private static final int PASSPORT_INDEX = 1;
    private static final int DRIVING_LICENSE_INDEX = 2;
    private static final int OTHER_ID_INDEX = 3;

    @Autowired
    private IdentityValidatorService service;
    private IdValidatorRequest idRequest;
    private List<OkmanyDTO> idList;

    @BeforeEach
    void setUp() {
        OkmanyDTO id = new OkmanyDTO("1", "123456AE", GOOD_IMAGE, DATE_IN_THE_FUTURE);
        OkmanyDTO passport = new OkmanyDTO("2", "BC1234567", GOOD_IMAGE, DATE_IN_THE_FUTURE);
        OkmanyDTO drivingLicense = new OkmanyDTO("3", "EF123456", GOOD_IMAGE, DATE_IN_THE_FUTURE);
        OkmanyDTO otherId = new OkmanyDTO("4", "LH123456MN", GOOD_IMAGE, DATE_IN_THE_FUTURE);
        idList = new ArrayList<>(Arrays.asList(id, passport, drivingLicense, otherId));
        idRequest = new IdValidatorRequest(idList, new ArrayList<>());
    }

    @Test
    public void whenExpirationDateIsInThePast_ValidityShouldBeFalse() {
        idList.get(ID_INDEX).setLejarDat(DATE_IN_THE_PAST);
        assertFalse(service.validateAndExtendOkmanyDTO(idRequest).getIdList().get(ID_INDEX).isErvenyes());
    }

    @Test
    public void whenExpirationDateIsInTheFuture_ValidityShouldBeTrue() {
        assertTrue(service.validateAndExtendOkmanyDTO(idRequest).getIdList().get(ID_INDEX).isErvenyes());
    }

    @Test
    public void whenIdTypeIsAValidNumber_ShouldBeFoundInDictionary() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_ID_CODE.getErrorMessage() +
                idList.get(ID_INDEX).getOkmanySzam());
    }

    @Test
    public void whenIdTypeIsALetter_ShouldBeAddedToErrorList() {
        idList.get(ID_INDEX).setOkmTipus("a");
        idList.get(ID_INDEX).setOkmanySzam("123");
        assertThatErrorListContains(ErrorMessage.INVALID_ID_CODE.getErrorMessage() + "123");
    }

    @Test
    public void whenIdTypeIsNotFound_ShouldBeAddedToErrorList() {
        idList.get(ID_INDEX).setOkmTipus("1001");
        idList.get(ID_INDEX).setOkmanySzam("456");
        assertThatErrorListContains(ErrorMessage.INVALID_ID_CODE.getErrorMessage() + "456");
    }

    @Test
    public void whenIdNumberIsValid_ShouldHaveSixDigitsTwoLetters() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() +
                idList.get(ID_INDEX).getOkmanySzam());
    }

    @Test
    public void whenIdNumberIsInvalid_ShouldBeAddedToErrorList() {
        idList.get(ID_INDEX).setOkmanySzam("invalid_id_number");
        assertThatErrorListContains(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() + "invalid_id_number");
    }

    @Test
    public void whenPassportNumberIsAValid_ShouldHaveTwoLettersSevenDigits() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() +
                idList.get(PASSPORT_INDEX).getOkmanySzam());
    }

    @Test
    public void whenPassportNumberIsInvalid_ShouldBeAddedToErrorList() {
        idList.get(PASSPORT_INDEX).setOkmanySzam("invalid_passport_number");
        assertThatErrorListContains(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() + "invalid_passport_number");
    }

    @Test
    public void whenDrivingLicenseNumberIsAValid_ShouldHaveTwoLettersSixDigits() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() +
                idList.get(DRIVING_LICENSE_INDEX).getOkmanySzam());
    }

    @Test
    public void whenDrivingLicenseNumberIsInvalid_ShouldBeAddedToErrorList() {
        idList.get(DRIVING_LICENSE_INDEX).setOkmanySzam("invalid_driving_license_number");
        assertThatErrorListContains(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() +
                "invalid_driving_license_number");
    }

    @Test
    public void whenOtherIdNumberIsValid_ShouldHaveSixCharacters() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() +
                idList.get(OTHER_ID_INDEX).getOkmanySzam());
    }

    @Test
    public void whenOtherIdNumberIsInvalid_ShouldBeAddedToErrorList() {
        idList.get(OTHER_ID_INDEX).setOkmanySzam("invalid_other_id_number");
        assertThatErrorListContains(ErrorMessage.INVALID_ID_NUMBER.getErrorMessage() +
                "invalid_other_id_number");
    }

    @Test
    public void whenImageFormatIsValid_ShouldHaveJpgFormat() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_IMAGE_FORMAT.getErrorMessage() +
                idList.get(ID_INDEX).getOkmanySzam());
    }

    @Test
    public void whenImageFormatIsInvalid_ShouldBeAddedToErrorList() {
        idList.get(ID_INDEX).setOkmanyKep(BAD_IMAGE_FORMAT);
        assertThatErrorListContains(ErrorMessage.INVALID_IMAGE_FORMAT.getErrorMessage() +
                idList.get(ID_INDEX).getOkmanySzam());
    }

    @Test
    public void whenImageSizeIsValid_ShouldBe1063x827() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_IMAGE_SIZE.getErrorMessage() +
                idList.get(ID_INDEX).getOkmanySzam());
    }

    @Test
    public void whenImageSizeIsInvalid_ShouldBeAddedToErrorList() {
        idList.get(ID_INDEX).setOkmanyKep(BAD_IMAGE_SIZE);
        assertThatErrorListContains(ErrorMessage.INVALID_IMAGE_SIZE.getErrorMessage() +
                idList.get(ID_INDEX).getOkmanySzam());
    }

    @Test
    public void whenIdListIsValid_ShouldContainOnlyOneActiveIdFromTheSameType() {
        OkmanyDTO id = new OkmanyDTO("1", "223456AE", GOOD_IMAGE, DATE_IN_THE_PAST);
        OkmanyDTO passport = new OkmanyDTO("2", "BC2234567", GOOD_IMAGE, DATE_IN_THE_PAST);
        idList.add(id);
        idList.add(passport);
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_MULTIPLE_ID_ACTIVE.getErrorMessage());
    }

    @Test
    public void whenIdListIsInvalid_ShouldBeAddedToErrorList() {
        OkmanyDTO id = new OkmanyDTO("1", "223456AE", GOOD_IMAGE, DATE_IN_THE_FUTURE);
        OkmanyDTO passport = new OkmanyDTO("2", "BC2234567", GOOD_IMAGE, DATE_IN_THE_FUTURE);
        idList.add(id);
        idList.add(passport);
        assertThatErrorListContains(ErrorMessage.INVALID_MULTIPLE_ID_ACTIVE.getErrorMessage());
    }

    private static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private void assertThatErrorListContains(String errorMessage) {
        assertTrue(service.validateAndExtendOkmanyDTO(idRequest)
                .getValidationErrors()
                .contains(errorMessage));
    }

    private void assertThatErrorListDoesNotContain(String errorMessage) {
        assertFalse(service.validateAndExtendOkmanyDTO(idRequest)
                .getValidationErrors()
                .contains(errorMessage));
    }
}
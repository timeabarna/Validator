package com.idomsoft.personvalidator.service;

import com.idomsoft.personvalidator.errorhandling.ErrorMessage;
import com.idomsoft.personvalidator.model.PersonValidatorRequest;
import com.idomsoft.personvalidator.model.SzemelyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonValidatorServiceTest {
    private static final Date EIGHTEEN_YEARS_OLD = convertLocalDateToDate(LocalDate.now().minusYears(18L));
    private static final Date HUNDRED_TWENTY_YEARS_OLD = convertLocalDateToDate(LocalDate.now().minusYears(120L));
    private static final Date TWO_HUNDRED_YEARS_OLD = convertLocalDateToDate(LocalDate.now().minusYears(200L));
    private static final Date FIFTY_YEARS_OLD = convertLocalDateToDate(LocalDate.now().minusYears(50L));
    private static final String HUNDRED_S_WITH_SPACES = String.join(" ", Collections.nCopies(100, "s"));
    private static final String VALID_NAME = "Dr. a-zäíáé ű.úőóüö A'ZÄÍÁÉŰ Ú/ŐÓÜÖ Dr.";
    private static final String ONE_NAME_PART = "Iuhg";
    private static final String ONE_NAME_PART_BETWEEN_DR = "Dr. Iuhg Dr.";
    private static final String ONE_NAME_PART_BEFORE_DR = "Iuhg Dr.";
    private static final String ONE_NAME_PART_AFTER_DR = "Dr. Iuhg";

    @Autowired
    private PersonValidatorService service;
    private PersonValidatorRequest personRequest;

    @BeforeEach
    void setUp() {
        SzemelyDTO person = new SzemelyDTO("Küsz K'ösz", "Í/kl áé-bc", "Dr. Abcd Ezsűó Dr.",
                FIFTY_YEARS_OLD, "N", "HUN");
        personRequest = new PersonValidatorRequest(person, new ArrayList<>());
    }

    @Test
    public void whenNationalityCodeIsNotThreeLetters_ShouldBeAddedToErrorList() {
        personRequest.getPerson().setAllampKod("111");
        assertThatErrorListContains(ErrorMessage.INVALID_NAT_CODE);
    }

    @Test
    public void whenNationalityCodeIsValid_ShouldBeFoundInDictionary() {
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_NAT_CODE);
    }

    @Test
    public void whenNationalityCodeIsValidAndNotInDictionary_ShouldBeAddedToErrorList() {
        personRequest.getPerson().setAllampKod("ÍÍÍ");
        assertThatErrorListContains(ErrorMessage.INVALID_NAT_CODE);
    }

    @Test
    public void whenNationalityCodeIsValid_NationalityShouldBeDecoded() {
        assertEquals("MAGYARORSZÁG ÁLLAMPOLGÁRA",
                service.validateAndExtendSzemelyDTO(personRequest).getPerson().getAllampDekod());
    }

    @Test
    public void whenNationalityDecodeNotFound_NationalityDecodeShouldBeAddedToErrorList() {
        personRequest.getPerson().setAllampKod("ÍÍÍ");
        assertThatErrorListContains(ErrorMessage.INVALID_NAT_DECODE);
    }

    @Test
    public void whenGenderIsFemale_ShouldBeCapitalN() {
        personRequest.getPerson().setNeme("N");
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_GENDER);
    }

    @Test
    public void whenGenderIsMale_ShouldBeCapitalF() {
        personRequest.getPerson().setNeme("F");
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_GENDER);
    }

    @Test
    public void whenGenderIsNeitherNNorF_ShouldBeAddedToErrorList() {
        personRequest.getPerson().setNeme("n");
        assertThatErrorListContains(ErrorMessage.INVALID_GENDER);
    }

    @Test
    public void whenGenderIsANumber_ShouldBeAddedToErrorList() {
        personRequest.getPerson().setNeme("1");
        assertThatErrorListContains(ErrorMessage.INVALID_GENDER);
    }

    @Test
    public void whenBirthDateIsValid_ShouldBeBetweenEighteenAndHundredTwenty() {
        personRequest.getPerson().setSzulDat(FIFTY_YEARS_OLD);
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_BIRTH_DATE);
    }

    @Test
    public void whenBirthDateIsValid_ShouldBeEqualsOrGreaterThenEighteen() {
        personRequest.getPerson().setSzulDat(EIGHTEEN_YEARS_OLD);
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_BIRTH_DATE);
    }

    @Test
    public void whenBirthDateIsValid_ShouldBeEqualsOrGreaterThenHundredTwenty() {
        personRequest.getPerson().setSzulDat(HUNDRED_TWENTY_YEARS_OLD);
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_BIRTH_DATE);
    }

    @Test
    public void whenBirthDateIsLessThanEighteen_ShouldBeAddedToErrorList() {
        personRequest.getPerson().setSzulDat(new Date());
        assertThatErrorListContains(ErrorMessage.INVALID_BIRTH_DATE);
    }

    @Test
    public void whenBirthDateIsGreaterThanHundredTwenty_ShouldBeAddedToErrorList() {
        personRequest.getPerson().setSzulDat(TWO_HUNDRED_YEARS_OLD);
        assertThatErrorListContains(ErrorMessage.INVALID_BIRTH_DATE);
    }

    @Test
    public void whenBirthNameIsValid_ShouldBeLessThanEightyCharacters() {
        personRequest.getPerson().setSzulNev(HUNDRED_S_WITH_SPACES);
        assertThatErrorListContains(ErrorMessage.INVALID_BIRTH_NAME);
    }

    @Test
    public void whenBirthNameIsValid_ShouldHaveAtLeastTwoNamePartsBetweenDr() {
        personRequest.getPerson().setSzulNev(ONE_NAME_PART_BETWEEN_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_BIRTH_NAME);
    }

    @Test
    public void whenBirthNameIsValid_ShouldHaveAtLeastTwoNamePartsAfterDr() {
        personRequest.getPerson().setSzulNev(ONE_NAME_PART_AFTER_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_BIRTH_NAME);
    }

    @Test
    public void whenBirthNameIsValid_ShouldHaveAtLeastTwoNamePartsBeforeDr() {
        personRequest.getPerson().setSzulNev(ONE_NAME_PART_BEFORE_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_BIRTH_NAME);
    }

    @Test
    public void whenBirthNameIsValid_ShouldHaveAtLeastTwoNameParts() {
        personRequest.getPerson().setSzulNev(ONE_NAME_PART);
        assertThatErrorListContains(ErrorMessage.INVALID_BIRTH_NAME);
    }

    @Test
    public void whenBirthNameIsValid_ShouldContainAllowedCharactersOnly() {
        personRequest.getPerson().setSzulNev(VALID_NAME);
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_BIRTH_NAME);
    }

    @Test
    public void whenNameIsValid_ShouldBeLessThanEightyCharacters() {
        personRequest.getPerson().setVisNev(HUNDRED_S_WITH_SPACES);
        assertThatErrorListContains(ErrorMessage.INVALID_NAME);
    }

    @Test
    public void whenNameIsValid_ShouldHaveAtLeastTwoNamePartsBetweenDr() {
        personRequest.getPerson().setVisNev(ONE_NAME_PART_BETWEEN_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_NAME);
    }

    @Test
    public void whenNameIsValid_ShouldHaveAtLeastTwoNamePartsAfterDr() {
        personRequest.getPerson().setVisNev(ONE_NAME_PART_AFTER_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_NAME);
    }

    @Test
    public void whenNameIsValid_ShouldHaveAtLeastTwoNamePartsBeforeDr() {
        personRequest.getPerson().setVisNev(ONE_NAME_PART_BEFORE_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_NAME);
    }

    @Test
    public void whenNameIsValid_ShouldHaveAtLeastTwoNameParts() {
        personRequest.getPerson().setVisNev(ONE_NAME_PART);
        assertThatErrorListContains(ErrorMessage.INVALID_NAME);
    }

    @Test
    public void whenNameIsValid_ShouldContainAllowedCharactersOnly() {
        personRequest.getPerson().setVisNev(VALID_NAME);
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_NAME);
    }

    @Test
    public void whenMothersNameIsValid_ShouldBeLessThanEightyCharacters() {
        personRequest.getPerson().setaNev(HUNDRED_S_WITH_SPACES);
        assertThatErrorListContains(ErrorMessage.INVALID_MOTHERS_NAME);
    }

    @Test
    public void whenMothersNameIsValid_ShouldHaveAtLeastTwoNamePartsBetweenDr() {
        personRequest.getPerson().setaNev(ONE_NAME_PART_BETWEEN_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_MOTHERS_NAME);
    }

    @Test
    public void whenMothersNameIsValid_ShouldHaveAtLeastTwoNamePartsAfterDr() {
        personRequest.getPerson().setaNev(ONE_NAME_PART_AFTER_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_MOTHERS_NAME);
    }

    @Test
    public void whenMothersNameIsValid_ShouldHaveAtLeastTwoNamePartsBeforeDr() {
        personRequest.getPerson().setaNev(ONE_NAME_PART_BEFORE_DR);
        assertThatErrorListContains(ErrorMessage.INVALID_MOTHERS_NAME);
    }

    @Test
    public void whenMothersNameIsValid_ShouldHaveAtLeastTwoNameParts() {
        personRequest.getPerson().setaNev(ONE_NAME_PART);
        assertThatErrorListContains(ErrorMessage.INVALID_MOTHERS_NAME);
    }

    @Test
    public void whenMothersNameIsValid_ShouldContainAllowedCharactersOnly() {
        personRequest.getPerson().setaNev(VALID_NAME);
        assertThatErrorListDoesNotContain(ErrorMessage.INVALID_MOTHERS_NAME);
    }

    private void assertThatErrorListContains(ErrorMessage errorMessage) {
        assertTrue(service.validateAndExtendSzemelyDTO(personRequest)
                .getValidationErrors()
                .contains(errorMessage.getErrorMessage()));
    }

    private void assertThatErrorListDoesNotContain(ErrorMessage errorMessage) {
        assertFalse(service.validateAndExtendSzemelyDTO(personRequest)
                .getValidationErrors()
                .contains(errorMessage.getErrorMessage()));
    }

    private static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
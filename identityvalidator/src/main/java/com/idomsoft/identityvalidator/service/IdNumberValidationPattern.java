package com.idomsoft.identityvalidator.service;

import java.util.regex.Pattern;

public enum IdNumberValidationPattern {
    ID1(Pattern.compile("[\\d]{6}[a-zA-Z]{2}")),
    ID2(Pattern.compile("[a-zA-Z]{2}[\\d]{7}")),
    ID3(Pattern.compile("[a-zA-Z]{2}[\\d]{6}")),
    ID4(Pattern.compile("[a-zA-z\\d]{10}")),
    ID5(Pattern.compile("[a-zA-z\\d]{10}")),
    ID6(Pattern.compile("[\\d]{6}[a-zA-Z]{2}"));

    private final Pattern validationPattern;

    IdNumberValidationPattern(Pattern errorMessage) {
        this.validationPattern = errorMessage;
    }

    public Pattern getValidationPattern() {
        return validationPattern;
    }
}

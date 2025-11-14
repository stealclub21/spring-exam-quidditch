package hu.progmasters.finalexam.exception.global;

import lombok.Getter;

@Getter
public class ValidationError {
    private final String field;
    private final String errorMessage;

    public ValidationError(String field, String errorMessage) {
        this.field = field;
        this.errorMessage = errorMessage;
    }
}

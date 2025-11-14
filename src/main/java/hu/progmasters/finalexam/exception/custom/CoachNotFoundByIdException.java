package hu.progmasters.finalexam.exception.custom;

import lombok.Getter;

@Getter
public class CoachNotFoundByIdException extends RuntimeException {
    private final Integer coachId;

    public CoachNotFoundByIdException(Integer coachId) {

        this.coachId = coachId;
    }
}

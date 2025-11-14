package hu.progmasters.finalexam.exception.custom;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClubNotFoundByIdException extends RuntimeException {
    private final Integer clubId;

    public ClubNotFoundByIdException(@NotNull Integer clubId) {
        this.clubId = clubId;
    }
}

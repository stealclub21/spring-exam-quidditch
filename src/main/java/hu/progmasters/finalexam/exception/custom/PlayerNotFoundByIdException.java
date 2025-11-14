package hu.progmasters.finalexam.exception.custom;

import lombok.Getter;

@Getter
public class PlayerNotFoundByIdException extends RuntimeException {
    private final Integer playerId;

    public PlayerNotFoundByIdException(Integer playerId) {
        this.playerId = playerId;
    }
}

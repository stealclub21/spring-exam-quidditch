package hu.progmasters.finalexam.exception.custom;

import lombok.Getter;

@Getter
public class PlayerIsAlreadyInTheClubException extends RuntimeException {
    private final Integer clubId;
    private final Integer playerId;

    public PlayerIsAlreadyInTheClubException(Integer clubId, Integer playerId) {
        this.clubId = clubId;
        this.playerId = playerId;
    }
}

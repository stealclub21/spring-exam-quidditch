package hu.progmasters.finalexam.exception.custom;

import hu.progmasters.finalexam.domain.PlayerType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClubDoNotNeedMoreFromTypeException extends RuntimeException {
    private final PlayerType playerType;
    private final Integer clubId;

    public ClubDoNotNeedMoreFromTypeException(@NotNull Integer clubId, @NotNull PlayerType playerType) {
        this.playerType = playerType;
        this.clubId = clubId;
    }
}

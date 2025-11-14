package hu.progmasters.finalexam.domain;

import lombok.Getter;

@Getter
public enum PlayerType {
    CHASER(3),
    BEATER(2),
    KEEPER(1),
    SEEKER(1);

    private final int value;

    PlayerType(int value) {
        this.value = value;
    }
}

package hu.progmasters.finalexam.exception.custom;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LocalDateInTheFutureException extends Throwable {
    private final LocalDate date;

    public LocalDateInTheFutureException(LocalDate date) {
        this.date = date;
    }
}

package hu.progmasters.finalexam.exception.global;

import hu.progmasters.finalexam.exception.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ValidationError> validationErrorHandler(MethodArgumentNotValidException e) {
        log.error("Validation Failed: {}", e.getMessage());
        return e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
    }

    @ExceptionHandler(ClubNotFoundByIdException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ValidationError> clubNotFoundByIdExceptionHandler(ClubNotFoundByIdException e) {
        log.error("ClubNotFoundByIdException: {}", e.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("clubId", "no club found with id: " + e.getClubId()));
        return errors;
    }

    @ExceptionHandler(LocalDateInTheFutureException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ValidationError> localDateInTheFutureExceptionHandler(LocalDateInTheFutureException e) {
        log.error("LocalDateInTheFutureException: {}", e.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("joined", "must be in the past"));
        return errors;
    }

    @ExceptionHandler(ClubDoNotNeedMoreFromTypeException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ValidationError> clubDoNotNeedMoreFromTypeExceptionHandler(ClubDoNotNeedMoreFromTypeException e) {
        log.error("ClubDoNotNeedMoreFromTypeException: {}", e.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        errors.add(
                new ValidationError(
                        "clubId",
                        "club (id: " + e.getClubId() + ") has enough player from type: " + e.getPlayerType())
        );
        return errors;
    }

    @ExceptionHandler(PlayerNotFoundByIdException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ValidationError> playerNotFoundByIdExceptionHandler(PlayerNotFoundByIdException e) {
        log.error("PlayerNotFoundByIdException: {}", e.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("playerId", "no player found with id: " + e.getPlayerId()));
        return errors;
    }

    @ExceptionHandler(PlayerIsAlreadyInTheClubException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ValidationError> playerIsAlreadyInTheClubExceptionHandler(PlayerIsAlreadyInTheClubException e) {
        log.error("PlayerIsAlreadyInTheClubException: {}", e.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        errors.add(
                new ValidationError(
                        "clubId",
                        "player (id: " + e.getPlayerId() + ") already joined club (id: " + e.getClubId() + ')')
        );
        return errors;
    }

    @ExceptionHandler(CoachNotFoundByIdException.class)
    @ResponseStatus(BAD_REQUEST)
    public List<ValidationError> coachNotFoundByIdExceptionHandler(CoachNotFoundByIdException e) {
        log.error("CoachNotFoundByIdException: {}", e.getMessage());
        List<ValidationError> errors = new ArrayList<>();
        String errorMessage = String.format("no coach found with id: %s", e.getCoachId());
        errors.add(new ValidationError("coachId", errorMessage));
        return errors;
    }

}

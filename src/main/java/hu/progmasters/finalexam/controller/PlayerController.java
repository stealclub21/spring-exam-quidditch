package hu.progmasters.finalexam.controller;

import hu.progmasters.finalexam.dto.incoming.PlayerCommand;
import hu.progmasters.finalexam.dto.outgoing.PlayerInfo;
import hu.progmasters.finalexam.exception.custom.LocalDateInTheFutureException;
import hu.progmasters.finalexam.service.PlayerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@Slf4j
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public PlayerInfo save(@Valid @RequestBody PlayerCommand command) throws LocalDateInTheFutureException {
        log.info("Player save command: {}", command);
        return playerService.save(command);
    }

    @PutMapping("{playerId}/club/{clubId}")
    @ResponseStatus(ACCEPTED)
    public PlayerInfo transfer(@PathVariable Integer playerId, @PathVariable Integer clubId) {
        log.info("Player transfer command: {}{}", playerId, clubId);
        return playerService.transfer(playerId, clubId);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<PlayerInfo> getAll() {
        log.info("Player getAll command");
        return playerService.listPlayers();
    }
}

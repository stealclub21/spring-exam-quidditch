package hu.progmasters.finalexam.controller;

import hu.progmasters.finalexam.dto.incoming.ClubCommand;
import hu.progmasters.finalexam.dto.outgoing.ClubInfo;
import hu.progmasters.finalexam.dto.outgoing.WinnerInfo;
import hu.progmasters.finalexam.service.ClubService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/clubs")
@Slf4j
public class ClubController {
    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ClubInfo save(@Valid @RequestBody ClubCommand command) {
        log.info("Club save command: {}", command);
        return clubService.save(command);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public WinnerInfo update(@PathVariable("id") Integer id) {
        log.info("Club update clubId: {}", id);
        return clubService.update(id);
    }

    @GetMapping("/superstar/{clubId}")
    @ResponseStatus(OK)
    public String getSuperstar(@PathVariable("clubId") Integer clubId) {
        log.info("Club getSuperstar clubId: {}", clubId);
        return clubService.getSuperstar(clubId);
    }
}

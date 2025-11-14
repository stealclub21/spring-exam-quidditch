package hu.progmasters.finalexam.controller;

import hu.progmasters.finalexam.dto.outgoing.CoachStatistic;
import hu.progmasters.finalexam.service.CoachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/coaches/")
@Slf4j
public class CoachController {
    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @DeleteMapping("/{coachId}")
    @ResponseStatus(OK)
    public void delete(@PathVariable int coachId) {
        log.info("Deleting coach with id {}", coachId);
        coachService.delete(coachId);
    }

    @GetMapping("/statistics/{coachId}")
    @ResponseStatus(OK)
    public CoachStatistic statistics(@PathVariable int coachId) {
        log.info("Statistics of coach with id {}", coachId);
        return coachService.statistics(coachId);
    }
}

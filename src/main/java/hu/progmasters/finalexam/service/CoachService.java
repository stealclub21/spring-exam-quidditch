package hu.progmasters.finalexam.service;

import hu.progmasters.finalexam.domain.Coach;
import hu.progmasters.finalexam.dto.outgoing.CoachStatistic;
import hu.progmasters.finalexam.exception.custom.CoachNotFoundByIdException;
import hu.progmasters.finalexam.repository.CoachRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CoachService {

    private final CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public void delete(int coachId) {
        Coach coach = findCoachById(coachId);
        coach.setClub(null);
        coach.setDeleted(true);
    }

    private Coach findCoachById(int coachId) {
        return coachRepository.findById(coachId)
                .orElseThrow(() -> new CoachNotFoundByIdException(coachId));
    }

    public CoachStatistic statistics(int coachId) {
        Coach coach = findCoachById(coachId);
        CoachStatistic statistic = new CoachStatistic();
//        statistic.getClubWins();
        return statistic;
    }
}

package hu.progmasters.finalexam.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoachStatistic {
    private int clubWins;
    private int playerWinsAverage;
    private int playerWithMostWins;
    private int playerWithTheLeastWins;
}

package hu.progmasters.finalexam.dto.outgoing;

import hu.progmasters.finalexam.domain.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerInfo {

    private int id;
    private String name;
    private LocalDate joined;
    private PlayerType playerType;
    private int wins;
    private String clubName;
}

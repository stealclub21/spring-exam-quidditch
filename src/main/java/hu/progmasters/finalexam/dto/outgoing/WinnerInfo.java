package hu.progmasters.finalexam.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WinnerInfo {

    private int id;
    private String name;
    private int wins;
    private String coachName;
    private List<PlayerInfo> players;

}

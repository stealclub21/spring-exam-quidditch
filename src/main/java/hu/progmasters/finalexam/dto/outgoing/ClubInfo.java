package hu.progmasters.finalexam.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubInfo {

    private int id;
    private String name;
    private int wins;
}

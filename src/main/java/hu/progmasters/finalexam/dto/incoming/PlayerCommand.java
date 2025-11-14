package hu.progmasters.finalexam.dto.incoming;

import hu.progmasters.finalexam.domain.PlayerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerCommand {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate joined;

    @NotNull
    private PlayerType playerType;

    private int wins;

    @NotNull
    private Integer clubId;
}

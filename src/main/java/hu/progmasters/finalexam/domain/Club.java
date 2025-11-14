package hu.progmasters.finalexam.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "wins")
    private int wins;

    @Column(name = "players")
    @OneToMany(mappedBy = "club")
    private List<Player> players;

    @OneToOne(mappedBy = "club")
    private Coach coach;
}

package hu.progmasters.finalexam.repository;

import hu.progmasters.finalexam.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    @Query("SELECT p FROM Player p ORDER BY p.joined DESC, p.wins ASC")
    List<Player> getAllPlayers();
}

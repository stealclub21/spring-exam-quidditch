package hu.progmasters.finalexam.service;

import hu.progmasters.finalexam.domain.Club;
import hu.progmasters.finalexam.domain.Player;
import hu.progmasters.finalexam.dto.incoming.ClubCommand;
import hu.progmasters.finalexam.dto.outgoing.ClubInfo;
import hu.progmasters.finalexam.dto.outgoing.PlayerInfo;
import hu.progmasters.finalexam.dto.outgoing.WinnerInfo;
import hu.progmasters.finalexam.exception.custom.ClubNotFoundByIdException;
import hu.progmasters.finalexam.repository.ClubRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final ModelMapper modelMapper;

    public ClubService(ClubRepository clubRepository, ModelMapper modelMapper) {
        this.clubRepository = clubRepository;
        this.modelMapper = modelMapper;
    }

    public ClubInfo save(ClubCommand command) {
        Club club = modelMapper.map(command, Club.class);
        clubRepository.save(club);
        return modelMapper.map(club, ClubInfo.class);
    }

    public WinnerInfo update(Integer clubId) {
        Club club = findClubByID(clubId);
        club.setWins(club.getWins() + 1);
        club.getPlayers().forEach(player -> player.setWins(player.getWins() + 1));

        List<PlayerInfo> players = club.getPlayers().stream()
                .map(player -> modelMapper.map(player, PlayerInfo.class))
                .toList();

        WinnerInfo winnerInfo = new WinnerInfo();
        winnerInfo.setId(clubId);
        winnerInfo.setName(club.getName());
        winnerInfo.setWins(club.getWins());
        winnerInfo.setCoachName(club.getCoach().getName());
        winnerInfo.setPlayers(players);

        return winnerInfo;
    }

    private Club findClubByID(int id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundByIdException(id));
    }

    public String getSuperstar(Integer clubId) {
        Club club = findClubByID(clubId);
        int clubWins = club.getWins();

        Player superStar = club.getPlayers().stream()
                .filter(player -> player.getWins() > clubWins)
                .findFirst()
                .orElse(null);

        if (superStar != null) {
            return "Has a superstar player!";
        } else {
            return "No superstar player.";
        }
    }
}

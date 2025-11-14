package hu.progmasters.finalexam.service;

import hu.progmasters.finalexam.domain.Club;
import hu.progmasters.finalexam.domain.Player;
import hu.progmasters.finalexam.domain.PlayerType;
import hu.progmasters.finalexam.dto.incoming.PlayerCommand;
import hu.progmasters.finalexam.dto.outgoing.PlayerInfo;
import hu.progmasters.finalexam.exception.custom.*;
import hu.progmasters.finalexam.repository.ClubRepository;
import hu.progmasters.finalexam.repository.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;
    private final ModelMapper modelMapper;

    public PlayerService(PlayerRepository playerRepository, ClubRepository clubRepository, ModelMapper modelMapper) {
        this.playerRepository = playerRepository;
        this.clubRepository = clubRepository;
        this.modelMapper = modelMapper;
    }

    public PlayerInfo save(PlayerCommand command) throws LocalDateInTheFutureException {
        if (localDateValidator(command.getJoined()) && playerTypeValidator(command)) {
            playerTypeValidator(command);
            Player player = modelMapper.map(command, Player.class);
            playerRepository.save(player);
            PlayerInfo playerInfo = modelMapper.map(player, PlayerInfo.class);
            Club club = findClubById(command.getClubId());
            playerInfo.setClubName(club.getName());
            return playerInfo;
        } else if (!localDateValidator(command.getJoined())) {
            throw new LocalDateInTheFutureException(command.getJoined());
        } else {
            throw new ClubDoNotNeedMoreFromTypeException(command.getClubId(), command.getPlayerType());
        }
    }

    public PlayerInfo transfer(Integer playerId, Integer clubId) {
        Club club = findClubById(clubId);
        Player player = findPlayerById(playerId);

        if (isPlayerAlreadyJoined(player)) {
            throw new PlayerIsAlreadyInTheClubException(clubId, playerId);
        }

        if (playerTypeValidatorForTransfer(playerId, clubId)) {
            player.getClub().getPlayers().removeIf(p -> Objects.equals(p.getId(), playerId));
            player.setJoined(LocalDate.now());
            player.setClub(club);
            club.getPlayers().add(player);

            PlayerInfo playerInfo = modelMapper.map(player, PlayerInfo.class);
            playerInfo.setClubName(club.getName());

            return playerInfo;
        } else {
            throw new ClubDoNotNeedMoreFromTypeException(clubId, player.getPlayerType());
        }
    }

    private boolean isPlayerAlreadyJoined(Player player) {
        return player.getClub().getPlayers().stream()
                .anyMatch(p -> Objects.equals(p.getId(), player.getId()));
    }

    private boolean playerTypeValidatorForTransfer(Integer playerId, Integer clubId) {
        Player player = findPlayerById(playerId);
        Club club = findClubById(clubId);
        PlayerType playerType = player.getPlayerType();

        long typeCounter = findClubById(club.getId()).getPlayers().stream()
                .filter(p -> p.getPlayerType().equals(playerType))
                .count();

        return typeCounter < playerType.getValue();
    }

    private boolean localDateValidator(LocalDate localDate) {
        return localDate.isBefore(LocalDate.now());
    }


    private boolean playerTypeValidator(PlayerCommand command) {
        PlayerType playerType = command.getPlayerType();
        long typeCounter = findClubById(command.getClubId()).getPlayers().stream()
                .filter(player -> player.getPlayerType().equals(playerType))
                .count();

        return typeCounter < playerType.getValue();
    }

    private Club findClubById(int id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundByIdException(id));
    }

    private Player findPlayerById(int id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundByIdException(id));
    }

    public List<PlayerInfo> listPlayers() {
        return playerRepository.getAllPlayers().stream()
                .map(player -> modelMapper.map(player, PlayerInfo.class))
                .toList();
    }
}
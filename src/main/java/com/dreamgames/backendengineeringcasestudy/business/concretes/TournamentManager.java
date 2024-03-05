package com.dreamgames.backendengineeringcasestudy.business.concretes;

import com.dreamgames.backendengineeringcasestudy.business.abstracts.TournamentService;
import com.dreamgames.backendengineeringcasestudy.business.rules.TournamentRules;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.entities.Tournament;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TournamentManager implements TournamentService {

    private TournamentRepository tournamentRepository;
    private TournamentRules tournamentRules;
    @Override
    @Scheduled(cron = "0 0 9 * * ?", zone = "UTC")
    public void createDailyTournament() {

        LocalDate today = LocalDate.now();
        Tournament newTournament = new Tournament();
        newTournament.setStartDate(today.atTime(00,00));
        newTournament.setEndDate(today.atTime(20,00));
        newTournament.setEnded(false);
        this.tournamentRepository.save(newTournament);
    }

    @Override
    @Scheduled(cron = "0 0 20 * * ?", zone = "UTC")
    public void stopActiveTournament() {
        LocalDate today = LocalDate.now();
        Tournament ongoingTournament = tournamentRepository.findByIsEnded(false);
        if (ongoingTournament!=null){
            ongoingTournament.setEnded(true);
            this.tournamentRepository.save(ongoingTournament);
        }
    }

    public void createTournamentForTest() {

        this.tournamentRules.checkIfAnActiveTournamentExists();
        LocalDate today = LocalDate.now();
        Tournament newTournament = new Tournament();
        newTournament.setStartDate(today.atTime(00,00));
        newTournament.setEndDate(today.atTime(20,00));
        newTournament.setEnded(false);
        this.tournamentRepository.save(newTournament);

    }
}

package com.dreamgames.backendengineeringcasestudy.business.abstracts;

import com.dreamgames.backendengineeringcasestudy.entities.Tournament;

import java.util.Optional;

public interface TournamentService {
    void createDailyTournament();
    void stopActiveTournament();

    void createTournamentForTest();

}

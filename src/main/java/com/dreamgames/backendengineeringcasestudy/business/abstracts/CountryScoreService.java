package com.dreamgames.backendengineeringcasestudy.business.abstracts;

import com.dreamgames.backendengineeringcasestudy.dtos.responses.CountryLeaderboardResponse;

import java.util.List;

public interface CountryScoreService {
    List<CountryLeaderboardResponse> getCountryLeaderboardByTournamentId(Long tournametId);
}

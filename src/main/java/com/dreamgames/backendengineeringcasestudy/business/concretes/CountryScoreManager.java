package com.dreamgames.backendengineeringcasestudy.business.concretes;

import com.dreamgames.backendengineeringcasestudy.business.abstracts.CountryScoreService;
import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.BusinessException;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.CountryScoreRepository;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.CountryLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.entities.CountryScore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CountryScoreManager implements CountryScoreService {

    private final CountryScoreRepository countryScoreRepository;
    @Override
    public List<CountryLeaderboardResponse> getCountryLeaderboardByTournamentId(Long tournamentId) {
        Optional<List<CountryScore>> results = this.countryScoreRepository.findByTournamentId(tournamentId);
        if (results.get().isEmpty()) {
            throw new BusinessException("Result not found for this tournament id! Be sure to send right tournament id.");
        }

        List<CountryLeaderboardResponse> response = results.get().stream()
                .map(r -> CountryLeaderboardResponse.builder()
                        .country(r.getCountry())
                        .score(r.getScore())
                        .build())
                .sorted(Comparator.comparing(CountryLeaderboardResponse::getScore).reversed()) // Descending order
                .collect(Collectors.toList());

        return response;
    }
}

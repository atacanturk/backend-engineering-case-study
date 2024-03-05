package com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts;

import com.dreamgames.backendengineeringcasestudy.entities.Country;
import com.dreamgames.backendengineeringcasestudy.entities.CountryScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryScoreRepository extends JpaRepository<CountryScore,Long> {
    Optional<List<CountryScore>>  findByTournamentId(Long tournamentId);
    Optional<CountryScore> findByTournamentIdAndCountry(Long tournamentId, Country country);
}

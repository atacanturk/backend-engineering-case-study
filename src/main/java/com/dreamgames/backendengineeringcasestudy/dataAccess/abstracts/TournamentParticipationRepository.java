package com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts;

import com.dreamgames.backendengineeringcasestudy.entities.TournamentParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TournamentParticipationRepository extends JpaRepository<TournamentParticipation,Long> {

   // @Query("SELECT tp FROM TournamentParticipation tp WHERE tp.tournament.id = ?1 AND tp.user.id = ?2")
    TournamentParticipation findByTournamentIdAndUserId(Long tournamentId, Long userId);

    boolean existsByUserId(Long userId);

    Optional<List<TournamentParticipation>> findByUserId(Long userId);

    Optional<List<TournamentParticipation>> findByGroupIdAndTournamentId(Long groupId, Long tournamentId);

    Optional<List<TournamentParticipation>> findByGroupId(Long groupId);
    boolean existsByTournamentIdAndUserId(Long tournamentId, Long userId);

    TournamentParticipation findLastByUserId(Long userId);
}

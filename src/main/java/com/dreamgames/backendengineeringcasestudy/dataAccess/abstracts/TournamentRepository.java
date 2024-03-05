package com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts;

import com.dreamgames.backendengineeringcasestudy.entities.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament,Long> {
    Tournament findByIsEnded(boolean isEnded);
    boolean existsByIsEnded(boolean isEnded);

    Tournament readById(Long id);


}

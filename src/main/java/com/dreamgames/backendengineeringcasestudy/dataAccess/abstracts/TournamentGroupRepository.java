package com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts;

import com.dreamgames.backendengineeringcasestudy.entities.TournamentGroup;import com.dreamgames.backendengineeringcasestudy.entities.TournamentParticipation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long> {

}

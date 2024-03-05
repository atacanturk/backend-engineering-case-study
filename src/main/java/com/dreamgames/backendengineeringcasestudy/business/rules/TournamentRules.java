package com.dreamgames.backendengineeringcasestudy.business.rules;

import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.BusinessException;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.TournamentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TournamentRules {
    private final TournamentRepository tournamentRepository;

    public void checkIfAnActiveTournamentExists(){
        if (this.tournamentRepository.existsByIsEnded(false)){
            throw new BusinessException("There is already an active tournament!");
        }
    }
}

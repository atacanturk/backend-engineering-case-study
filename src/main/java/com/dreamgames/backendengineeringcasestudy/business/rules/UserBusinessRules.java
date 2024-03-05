package com.dreamgames.backendengineeringcasestudy.business.rules;

import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.BusinessException;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.CountryScoreRepository;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.TournamentParticipationRepository;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.UserRepository;
import com.dreamgames.backendengineeringcasestudy.entities.CountryScore;
import com.dreamgames.backendengineeringcasestudy.entities.Tournament;
import com.dreamgames.backendengineeringcasestudy.entities.TournamentParticipation;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserBusinessRules {
    private UserRepository userRepository;
    private TournamentParticipationRepository tournamentParticipationRepository;
    private TournamentRepository tournamentRepository;
    private CountryScoreRepository countryScoreRepository;
    public void checkIfUserNotExists(Long id){
        if(!this.userRepository.existsById(id)){
            throw new BusinessException("User not found");
        }
    }

    public void checkIfUserInActiveTournamentThenIncrementTournamentScore(Long userId){

        if(this.tournamentRepository.existsByIsEnded(false)){
            Tournament tournament =this.tournamentRepository.findByIsEnded(false);

            TournamentParticipation participant = this.tournamentParticipationRepository.findByTournamentIdAndUserId(tournament.getId(), userId);
            if (participant != null){
                Optional<CountryScore> countryScore = this.countryScoreRepository.findByTournamentIdAndCountry(tournament.getId(), participant.getCountry());

                if (countryScore.isPresent()){
                    countryScore.get().setScore(countryScore.get().getScore()+1);
                    this.countryScoreRepository.save(countryScore.get());
                }
                participant.setScore(participant.getScore()+1);
                this.tournamentParticipationRepository.save(participant);
            }
        }
    }
}

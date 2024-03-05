package com.dreamgames.backendengineeringcasestudy.business.rules;

import com.dreamgames.backendengineeringcasestudy.business.constants.TournamentParticipationDefaultValues;
import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.BusinessException;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.TournamentParticipationRepository;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.TournamentRepository;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.UserRepository;
import com.dreamgames.backendengineeringcasestudy.entities.RewardStatus;
import com.dreamgames.backendengineeringcasestudy.entities.Tournament;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TournamentParticipationRules {
    private UserRepository userRepository;
    private TournamentRepository tournamentRepository;
    private TournamentParticipationRepository tournamentParticipationRepository;

    public void checkIfActiveTournamentNotExists(){
        if (!this.tournamentRepository.existsByIsEnded(false)){
            throw new BusinessException("There is no active tournament yet!");
        }
    }

    public void checkIfUserNotEnoughLevel(Long userId){
        if (this.userRepository.findById(userId).get().getLevel()<TournamentParticipationDefaultValues.NECCESSARY_LEVEL){
            throw new BusinessException("Your level must be at least 20");
        }
    }

    public void checkIfUserNotEnoughCoin(Long userId){
        if(this.userRepository.findById(userId).get().getCoin()< TournamentParticipationDefaultValues.PAYMENT){
            throw new BusinessException("You have not enough coin to join!");
        }
    }

    public void checkIfUserNotExists(Long userId){
        if(!this.userRepository.existsById(userId)){
            throw new BusinessException("User not found");
        }
    }

    public void checkIfUserHasNonClaimedReward(Long userId){
        if(isUserHasAnyTournamentExperince(userId)){
            this.tournamentParticipationRepository.findByUserId(userId).get().stream().forEach(p ->{
                if (p.getStatus() != RewardStatus.CLAIMED_OR_NO_REWARD){
                    throw new BusinessException("First you must claim last tournament reward to join new tournament");
                }
            });
        }
    }

    private boolean isUserHasAnyTournamentExperince(Long userId) {
        return tournamentParticipationRepository.existsByUserId(userId);
    }

    public void checkIfUserAlreadyInActiveTournament(Long userId) {
        Long activeTournamentId = this.tournamentRepository.findByIsEnded(false).getId();
        if (this.tournamentParticipationRepository.existsByTournamentIdAndUserId(activeTournamentId,userId)){
            throw new BusinessException("You already in an active tournament!");
        }
    }


}

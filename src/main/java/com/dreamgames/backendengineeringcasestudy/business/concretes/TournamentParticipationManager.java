package com.dreamgames.backendengineeringcasestudy.business.concretes;

import com.dreamgames.backendengineeringcasestudy.business.abstracts.TournamentParticipationService;
import com.dreamgames.backendengineeringcasestudy.business.constants.TournamentParticipationDefaultValues;
import com.dreamgames.backendengineeringcasestudy.business.rules.TournamentParticipationRules;
import com.dreamgames.backendengineeringcasestudy.core.utilities.exceptions.BusinessException;
import com.dreamgames.backendengineeringcasestudy.core.utilities.mappers.ModelMapperService;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.*;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.GroupLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entities.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TournamentParticipationManager implements TournamentParticipationService {

    private final UserRepository userRepository;
    private final TournamentParticipationRepository tournamentParticipationRepository;
    private final GroupMatcher groupMatcher;
    private final TournamentRepository tournamentRepository;
    private final TournamentGroupRepository tournamentGroupRepository;
    private final TournamentParticipationRules tournamentParticipationRules;
    private final CountryScoreRepository countryScoreRepository;
    private ModelMapperService modelMapperService;


    /*
    * Checks business rules, Calls group matcher
    * If matched, saves group to database, returns groupLeaderboard of user
    */
    public List<GroupLeaderboardResponse> enterTournament(Long userId){
        //run business logic
        this.tournamentParticipationRules.checkIfUserNotExists(userId);
        this.tournamentParticipationRules.checkIfActiveTournamentNotExists();
        this.tournamentParticipationRules.checkIfUserNotEnoughLevel(userId);
        this.tournamentParticipationRules.checkIfUserNotEnoughCoin(userId);
        this.tournamentParticipationRules.checkIfUserHasNonClaimedReward(userId);
        this.tournamentParticipationRules.checkIfUserAlreadyInActiveTournament(userId);

        User user =this.userRepository.findById(userId).get();

        //find group for this user
        Optional<List<Long>> groupMembers = groupMatcher.addUserToQueueAndTryMatch(userId, user.getCountry());

        if (groupMembers.isPresent()) {
            List<Long> userIds = groupMembers.get();

            Tournament activeTournament = this.tournamentRepository.findByIsEnded(false);

            checkIfCountryScoresNotCreatedThenCreateForAllCountries(activeTournament);
            setUsersCoinForTournamentPayment(userIds);

            TournamentGroup group = createGroup(userIds);

            group.setTournament(activeTournament);
            TournamentGroup savedGroup = this.tournamentGroupRepository.save(group);

            List<TournamentParticipation> participants = savedGroup.getParticipants();

            participants.stream().forEach(p-> {
                p.setGroup(savedGroup);
                p.setStatus(RewardStatus.NOT_CALCULATED);
            });

            this.tournamentParticipationRepository.saveAll(participants);

            participants.sort(Comparator.comparing(TournamentParticipation::getScore));

            List<GroupLeaderboardResponse> response = participants.stream()
                    .map(participant->this.modelMapperService.forResponse()
                            .map(participant,GroupLeaderboardResponse.class)).toList();

            //Generic model mapper servisi tam eşleşme sağlayamadığı için id'leri düzelt
            for (int i = 0; i<response.size();i++) {
                response.get(i).setId(participants.get(i).getUser().getId());
            }

            //sort response by score

            return response;
        }else throw new BusinessException("Not enough players yet! You've been added to the waiting queue.");

    }

    private void checkIfCountryScoresNotCreatedThenCreateForAllCountries(Tournament activeTournament) {
        Optional<List<CountryScore>> countryScore = this.countryScoreRepository.findByTournamentId(activeTournament.getId());
        if (countryScore.get().isEmpty()){
            List<CountryScore> scores = new ArrayList<>();
            for (Country c : Country.values()){
                scores.add(CountryScore.builder()
                        .country(c)
                        .tournament(activeTournament)
                        .score(0).build());
            }
            this.countryScoreRepository.saveAll(scores);
        }
    }
    public UpdateLevelResponse claimReward(Long userId){
        TournamentParticipation tournamentParticipation = this.tournamentParticipationRepository.findLastByUserId(userId);
        User user = this.userRepository.findById(userId).get();
        if (tournamentParticipation == null){
            throw new BusinessException("You do not have any tournament record!");
        }

        if (tournamentParticipation.getStatus()==RewardStatus.NOT_CALCULATED){
            updateStatusForAllGroupMembers(tournamentParticipation, user);
        }
        this.tournamentParticipationRules.checkIfUserAlreadyInActiveTournament(userId);
        switch (tournamentParticipation.getStatus()){
            case FIRST -> {
                user.setCoin(user.getCoin()+TournamentParticipationDefaultValues.FIRST_REWARD);
                this.userRepository.save(user);
                tournamentParticipation.setStatus(RewardStatus.CLAIMED_OR_NO_REWARD);
                this.tournamentParticipationRepository.save(tournamentParticipation);
                break;
            }
            case SECOND -> {
                user.setCoin(user.getCoin()+TournamentParticipationDefaultValues.SECOND_REWARD);
                this.userRepository.save(user);
                tournamentParticipation.setStatus(RewardStatus.CLAIMED_OR_NO_REWARD);
                this.tournamentParticipationRepository.save(tournamentParticipation);
                break;
            }
            case CLAIMED_OR_NO_REWARD -> {
                throw new BusinessException("You don't have any reward.");
            }
        }
        return UpdateLevelResponse.builder().level(user.getLevel()).coin(user.getCoin()).build();

    }

    private void updateStatusForAllGroupMembers(TournamentParticipation tournamentParticipant, User user){
        List<TournamentParticipation> groupMembers = tournamentParticipant.getGroup().getParticipants();
        groupMembers.sort(Comparator.comparing(TournamentParticipation::getScore).reversed());
        for (int i = 0; i<groupMembers.size(); i++){
            if (i==0){
                groupMembers.get(i).setStatus(RewardStatus.FIRST);
            } else if (i==1) {
                groupMembers.get(i).setStatus(RewardStatus.SECOND);
            }else {
                groupMembers.get(i).setStatus(RewardStatus.CLAIMED_OR_NO_REWARD);
            }
        }

        this.tournamentParticipationRepository.saveAll(groupMembers);

    }

    public int getGroupRank(Long tournamentId, Long userId) {
        TournamentParticipation participant = this.tournamentParticipationRepository.findByTournamentIdAndUserId(tournamentId, userId);
        Long groupId = participant.getGroup().getId();
        Optional<List<TournamentParticipation>> groupParticipants = this.tournamentParticipationRepository.findByGroupIdAndTournamentId(groupId, tournamentId);
        if (groupParticipants.isPresent()) {
            List<TournamentParticipation> sortedGroup = groupParticipants.get();
            sortedGroup.sort(Comparator.comparing(TournamentParticipation::getScore));
            for (int i = sortedGroup.size()-1; i > 0; i--) {
                if (sortedGroup.get(i).getUser().getId().equals(userId)) {
                    return (5-i);
                }
            }
        }else throw new BusinessException("Record not found.");

        return 0;
    }

    public List<GroupLeaderboardResponse> getGroupLeaderboard(Long groupId){
        Optional<List<TournamentParticipation>> request = this.tournamentParticipationRepository.findByGroupId(groupId);
        if (!request.isPresent()){
            throw new BusinessException("Group not found");
        }

        List<TournamentParticipation> sortedGroup = request.get();
        sortedGroup.sort(Comparator.comparing(TournamentParticipation::getScore));
        Collections.reverse(sortedGroup);

        return sortedGroup.stream().map(temp ->{
            return GroupLeaderboardResponse.builder()
                    .id(temp.getUser().getId())
                    .userName(temp.getUser().getName())
                    .score(temp.getScore())
                    .country(temp.getCountry())
                    .build();
        }).collect(Collectors.toList());
    }

    private void setUsersCoinForTournamentPayment(List<Long> userIds){

        List<User> users = new ArrayList<>();
        for (Long userId: userIds) {
            users.add(this.userRepository.findById(userId).get());
        }

        users.stream().forEach(user1 -> user1.setCoin(user1.getCoin()- TournamentParticipationDefaultValues.PAYMENT));
        this.userRepository.saveAll(users);
    }

    private TournamentGroup createGroup(List<Long> userIds) {

        //create new tournamentParticipation list
        List<TournamentParticipation> participants = new ArrayList<>();
        Tournament activeTournament = this.tournamentRepository.findByIsEnded(false);

        for (Long userId:userIds) {
            User user = this.userRepository.findById(userId).get();
            participants.add(TournamentParticipation.builder()
                    .tournament(activeTournament)
                    .country(user.getCountry())
                    .user(user)
                    .build());
        }

        return TournamentGroup.builder().participants(participants).build();
    }
}

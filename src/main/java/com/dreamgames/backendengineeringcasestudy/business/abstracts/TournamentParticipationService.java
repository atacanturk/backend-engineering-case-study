package com.dreamgames.backendengineeringcasestudy.business.abstracts;

import com.dreamgames.backendengineeringcasestudy.dtos.responses.GroupLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.UpdateLevelResponse;

import java.util.List;

public interface TournamentParticipationService {
    List<GroupLeaderboardResponse> enterTournament(Long userId);

    int getGroupRank(Long tournamentId, Long userId);
    List<GroupLeaderboardResponse> getGroupLeaderboard(Long groupId);

    UpdateLevelResponse claimReward(Long userId);
}

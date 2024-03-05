package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.business.abstracts.CountryScoreService;
import com.dreamgames.backendengineeringcasestudy.business.abstracts.TournamentParticipationService;
import com.dreamgames.backendengineeringcasestudy.business.abstracts.TournamentService;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.TournamentGroupRepository;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.CountryLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.GroupLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entities.TournamentParticipation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/tournament")
public class TournamentParticipationsController {
    private  TournamentParticipationService tournamentParticipationService;
    private TournamentService tournamentService; // to create fake torunament for API test
    private CountryScoreService countryScoreService;


    //Normalde POST işlemlerinde değişkeni @RequestBody olarak almak daha sağlıklı
    //fakat çeşitli kullanım örnekleri olması amacıyla bu şekilde kullandım.
    @PostMapping("/{id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<GroupLeaderboardResponse> enterTournamentRequest(@PathVariable Long id){return this.tournamentParticipationService.enterTournament(id);
    }

    @GetMapping("/getGroupRank")
    @ResponseStatus(code = HttpStatus.CREATED)
    public int getGroupRankRequest( @RequestParam Long tournamentId, @RequestParam Long userId){
        return this.tournamentParticipationService.getGroupRank(tournamentId, userId);
    }

    @GetMapping("/getGroupLeaderboard")
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<GroupLeaderboardResponse> getGroupLeaderboardRequest(@RequestParam Long groupId){
        return this.tournamentParticipationService.getGroupLeaderboard(groupId);
    }

    @PostMapping("/newFakeTournament")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createFakeTournament(){
        this.tournamentService.createTournamentForTest();
        return "created successfully";
    }

    @GetMapping("/getCountryLeaderboard")
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<CountryLeaderboardResponse> getCountryLeaderboardRequest(@RequestParam Long tournamentId){
        return this.countryScoreService.getCountryLeaderboardByTournamentId(tournamentId);
    }

    @PostMapping("/claimReward")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public UpdateLevelResponse claimRewardRequest(@RequestParam Long userId){
        return this.tournamentParticipationService.claimReward(userId);
    }
}

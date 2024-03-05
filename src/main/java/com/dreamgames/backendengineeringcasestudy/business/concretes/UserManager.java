package com.dreamgames.backendengineeringcasestudy.business.concretes;

import com.dreamgames.backendengineeringcasestudy.business.abstracts.UserService;
import com.dreamgames.backendengineeringcasestudy.business.constants.UserDefaultValues;
import com.dreamgames.backendengineeringcasestudy.business.rules.UserBusinessRules;
import com.dreamgames.backendengineeringcasestudy.core.utilities.mappers.ModelMapperService;
import com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts.UserRepository;
import com.dreamgames.backendengineeringcasestudy.dtos.requests.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.CreateUserResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.GetAllUsersWithoutParticipationsResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.GroupLeaderboardResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entities.Country;
import com.dreamgames.backendengineeringcasestudy.entities.User;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Random;

@Service
@AllArgsConstructor
public class UserManager implements UserService {

    private final UserRepository userRepository;
    private final ModelMapperService modelMapperService;
    private final UserBusinessRules userBusinessRules;

    @Override
    public CreateUserResponse createUser(String userName) {
        Country country = getRandomCountry();
        User user = User.builder()
                .name(userName)
                .level(UserDefaultValues.LEVEL)
                .coin(UserDefaultValues.COIN)
                .country(country)
                .build();

        User savedUser = userRepository.save(user);

        CreateUserResponse response = this.modelMapperService.forResponse().map(savedUser, CreateUserResponse.class);

        return response;
    }

    @Override
    public UpdateLevelResponse updateLevel(UpdateLevelRequest updateLevelRequest) {

        //iş kuralları
        this.userBusinessRules.checkIfUserNotExists(updateLevelRequest.getId());
        this.userBusinessRules.checkIfUserInActiveTournamentThenIncrementTournamentScore(updateLevelRequest.getId());

        User user = findById(updateLevelRequest.getId());
        user.setCoin(user.getCoin()+25);
        user.setLevel(user.getLevel()+1);

        User updatedUser = this.userRepository.save(user);

        UpdateLevelResponse response = this.modelMapperService.forResponse().map(updatedUser,UpdateLevelResponse.class);
        return response;
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id).get();
    }


    private Country getRandomCountry(){
        return Country.values()[new Random().nextInt(Country.values().length)];
    }
    @Override
    public List<GetAllUsersWithoutParticipationsResponse> getAll(){
        List<User> users= this.userRepository.findAllWithoutParticipations();

        List<GetAllUsersWithoutParticipationsResponse> response = users.stream()
                .map(user->this.modelMapperService.forResponse()
                        .map(user,GetAllUsersWithoutParticipationsResponse.class)).toList();
        return response;
    }
}

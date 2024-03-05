package com.dreamgames.backendengineeringcasestudy.business.abstracts;

import com.dreamgames.backendengineeringcasestudy.dtos.requests.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.CreateUserResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.GetAllUsersWithoutParticipationsResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entities.User;

import java.util.List;

public interface UserService {
    CreateUserResponse createUser(String userName);
    UpdateLevelResponse updateLevel(UpdateLevelRequest updateLevelRequest);

    User findById(Long id);

    List<GetAllUsersWithoutParticipationsResponse> getAll();
}

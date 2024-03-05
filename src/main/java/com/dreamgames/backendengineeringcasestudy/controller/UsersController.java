package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.business.abstracts.UserService;
import com.dreamgames.backendengineeringcasestudy.dtos.requests.UpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.CreateUserResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.GetAllUsersWithoutParticipationsResponse;
import com.dreamgames.backendengineeringcasestudy.dtos.responses.UpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping(params = "userName")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CreateUserResponse create(@RequestParam String userName){

        return userService.createUser(userName);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public UpdateLevelResponse update(@RequestBody UpdateLevelRequest updateLevelRequest){
        return userService.updateLevel(updateLevelRequest);
    }

    @GetMapping("/getAll")
    public List<GetAllUsersWithoutParticipationsResponse> getAll(){

        return userService.getAll();
    }

}

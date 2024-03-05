package com.dreamgames.backendengineeringcasestudy.dataAccess.abstracts;

import com.dreamgames.backendengineeringcasestudy.entities.TournamentParticipation;
import com.dreamgames.backendengineeringcasestudy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u")
    List<User> findAllWithoutParticipations();

}

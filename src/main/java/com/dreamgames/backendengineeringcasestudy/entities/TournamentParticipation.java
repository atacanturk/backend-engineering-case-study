package com.dreamgames.backendengineeringcasestudy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TournamentParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private TournamentGroup group;

    @ManyToOne
    private Tournament tournament;

    private Country country;

    private int score = 0; // Track user's score within the tournament

    @Enumerated(EnumType.STRING)
    private RewardStatus status;


}

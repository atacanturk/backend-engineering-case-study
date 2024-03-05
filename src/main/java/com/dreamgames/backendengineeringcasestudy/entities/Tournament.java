package com.dreamgames.backendengineeringcasestudy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @OneToMany(mappedBy = "tournament") // Mapped by defines field in TournamentParticipation class
    private List<TournamentParticipation> participants;

    @OneToMany(mappedBy = "tournament")
    private List<CountryScore> countryScores;

    @OneToMany(mappedBy = "tournament")
    private List<TournamentGroup> tournamentGroups;

    private boolean isEnded;
}

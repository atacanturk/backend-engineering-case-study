package com.dreamgames.backendengineeringcasestudy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "")
    private String name;

    private int level = 1;

    private int coin = 5000;

    @Enumerated(EnumType.STRING)
    private Country country;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // Mapped by defines field in Tournament class
    private List<TournamentParticipation> tournamentParticipations;

}

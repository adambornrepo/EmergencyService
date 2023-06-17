package com.tech.security.token;

import com.tech.entites.abstracts.Employee;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_generator"
    )
    @SequenceGenerator(
            name = "token_generator",
            sequenceName = "token_sequence",
            allocationSize = 1
    )
    public Long id;

    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    public Employee employee;
}

package kz.soft.finalrpo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}

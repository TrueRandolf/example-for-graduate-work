package ru.skypro.homework.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false)
    @NotBlank
    private String userName;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "first_name", length = 16)
    private String firstName;

    @Column(name = "last_name", length = 16)
    private String lastName;

    @Column(name = "phone")
    @NotBlank
    private String phone;

    @Column(name = "user_image")
    private String userImage;


}

package ru.skypro.homework.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "ads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "price")
    @PositiveOrZero
    private int price;

    @Column(name = "ad_image")
    private String adImage;

    @Column(name = "title", length = 64)
    private String title;

}

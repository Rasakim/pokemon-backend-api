package im.rasak.pokemon.backend.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity author;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(nullable = false)
    private String content = "";

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be between 0 and 5")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be between 0 and 5")
    private double rating;

    public Review() {

    }
    
    public Review(Pokemon pokemon, UserEntity author, String content, double rating) {
        this.pokemon = pokemon;
        this.author = author;
        this.content = content;
        this.rating = rating;
    }
}

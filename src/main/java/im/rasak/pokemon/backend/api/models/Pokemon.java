package im.rasak.pokemon.backend.api.models;

import im.rasak.pokemon.backend.api.enums.PokemonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private int pokedexId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int generation;

    @ElementCollection(targetClass = PokemonType.class)
    @CollectionTable(name = "pokemon_entity_types", joinColumns = @JoinColumn(name = "pokemon_id", nullable = false))
    @Enumerated(EnumType.STRING)
    private Set<PokemonType> types = new HashSet<>();

    @Column(nullable = false, length = 1024)
    private String description = "";

    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @Column(nullable = false)
    private String imageUrl = "";

    public Pokemon() {

    }
    
    // Constructor to create a Pokemon
    public Pokemon(int pokedexId, String name, int generation, Set<PokemonType> types, String description, String imageUrl) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.generation = generation;
        this.types = types;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Pokemon(int pokedexId, String name, int generation, Set<PokemonType> types, String description, Set<Review> reviews, String imageUrl) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.generation = generation;
        this.types = types;
        this.description = description;
        this.reviews = reviews;
        this.imageUrl = imageUrl;
    }

    @Transient
    public double getAverageRating() {
        double rating = 0.0;

        if (reviews.isEmpty()) {
            return rating;
        } else {
            for (Review r : reviews) {
                rating += r.getRating();
            }

            return rating / reviews.size();
        }
    }
}

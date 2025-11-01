package im.rasak.pokemon.backend.api.models;

import jakarta.persistence.*;

@Entity
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private int stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_entity_pokedex_id")
    private PokemonEntity pokemonEntity;

    public ReviewEntity() {

    }

    public ReviewEntity(int id, String title, String content, int starts) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.stars = starts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public PokemonEntity getPokemonEntity() {
        return pokemonEntity;
    }

    public void setPokemonEntity(PokemonEntity pokemonEntity) {
        this.pokemonEntity = pokemonEntity;
    }
}

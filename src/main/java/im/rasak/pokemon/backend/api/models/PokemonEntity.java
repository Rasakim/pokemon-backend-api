package im.rasak.pokemon.backend.api.models;

import im.rasak.pokemon.backend.api.enums.PokemonEntityType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class PokemonEntity {

    @Id
    private int pokedexId;

    private String name;

    @ElementCollection(targetClass = PokemonEntityType.class)
    @CollectionTable(name = "pokemon_entity_types", joinColumns = @JoinColumn(name = "pokedex_id"))
    @Enumerated(EnumType.STRING)
    private Set<PokemonEntityType> types = new HashSet<>();

    private String imageUrl;

    @OneToMany(mappedBy = "pokemonEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();

    public PokemonEntity() {
    }

    public PokemonEntity(int pokedexId, String name, Set<PokemonEntityType> types, String imageUrl) {

        this.pokedexId = pokedexId;
        this.name = name;
        this.types = types;
        this.imageUrl = imageUrl;
    }

    public int getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(int pokedexId) {
        this.pokedexId = pokedexId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PokemonEntityType> getTypes() {
        return types;
    }

    public void setTypes(Set<PokemonEntityType> types) {
        this.types = types;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

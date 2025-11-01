package im.rasak.pokemon.backend.api.dto;

import im.rasak.pokemon.backend.api.enums.PokemonEntityType;

import java.util.HashSet;
import java.util.Set;

public class PokemonEntityDTO {

    private Integer pokedexId;
    private String name;
    private Set<PokemonEntityType> types = new HashSet<>();
    private String imageUrl;

    public PokemonEntityDTO() {

    }

    public PokemonEntityDTO(int pokedexId, String name, Set<PokemonEntityType> types, String imageUrl) {
        this.pokedexId = pokedexId;
        this.name = name;
        this.types = types;
        this.imageUrl = imageUrl;
    }

    public Integer getPokedexId() {
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

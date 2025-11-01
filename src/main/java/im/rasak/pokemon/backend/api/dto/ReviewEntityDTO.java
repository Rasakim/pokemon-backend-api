package im.rasak.pokemon.backend.api.dto;

public class ReviewEntityDTO {

    private int id;
    private Integer pokedexId;
    private String title;
    private String content;
    private int stars;


    public ReviewEntityDTO() {

    }

    public ReviewEntityDTO(int id, Integer pokedexId, String title, String content, int stars) {
        this.id = id;
        this.pokedexId = pokedexId;
        this.title = title;
        this.content = content;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(Integer pokedexId) {
        this.pokedexId = pokedexId;
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
}

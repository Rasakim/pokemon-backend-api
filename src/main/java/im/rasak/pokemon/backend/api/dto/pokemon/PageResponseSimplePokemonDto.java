package im.rasak.pokemon.backend.api.dto.pokemon;

import java.util.List;

public class PageResponseSimplePokemonDto {

    private List<SimplePokemonDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;

    public PageResponseSimplePokemonDto() {

    }

    public PageResponseSimplePokemonDto(List<SimplePokemonDto> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean lastPage) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.lastPage = lastPage;
    }

    public List<SimplePokemonDto> getContent() {
        return content;
    }

    public void setContent(List<SimplePokemonDto> content) {
        this.content = content;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }
}

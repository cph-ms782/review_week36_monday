package dto;

import entities.Movie;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author msi
 */
public class MovieDTO {

    private Long id;
    private String title;
    private int year;
    private List<Movie> list;

    public MovieDTO(Movie m) {
        this.id = m.getId();
        this.title = m.getTitle();
        this.year = m.getYear();
    }

    public MovieDTO(List<Movie> listMovies) {
        list = new ArrayList();
        for (Movie movie : listMovies) {
            this.list.add(movie);
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setList(List<Movie> listMovies) {
        for (Movie movie : listMovies) {
            this.list.add(movie);
        }
    }

}

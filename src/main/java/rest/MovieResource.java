package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.MovieDTO;
import entities.Movie;
import utils.EMF_Creator;
import facades.MovieFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("movies")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/movie",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    private static final MovieFacade FACADE =  MovieFacade.getMovieFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllMovies() {
//    public Response getAllMovies() {
        List<Movie> list = FACADE.allMovies();
        return GSON.toJson(new MovieDTO(list));
//        return Response.ok().entity(GSON.toJson(new MovieDTO(list))).build();
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMovieCount() {
        long count = FACADE.getMovieCount();
        System.out.println("--------------->"+count);
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
    
    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMoviesPerYear(@PathParam("id") Long id) {
        Movie movie = FACADE.findByID(id);
        return GSON.toJson(new MovieDTO(movie));
    }
    
    @Path("year/{year}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMoviesPerYear(@PathParam("year") int year) {
        List<Movie> list = FACADE.findByYear(year);
        return GSON.toJson(new MovieDTO(list));
    }
    
    @Path("movie/{title}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMoviesPerTitle(@PathParam("title") String title) {
        List<Movie> list = FACADE.findByTitle(title);
        return GSON.toJson(new MovieDTO(list));
    }
    
    @Path("fill")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMoviesPerTitle() {
        FACADE.addMovie("Olsenbanden Derudaf", 1976);
        FACADE.addMovie("Olsenbanden Gør det", 1977);
        FACADE.addMovie("Olsenbanden Hjemmefra", 1978);
        FACADE.addMovie("Olsenbanden I fængsel", 1979);
        FACADE.addMovie("En mand ser rødt", 1979);
        return GSON.toJson("Database filled");
    }

    
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Movie entity) {
        throw new UnsupportedOperationException();
    }
    
    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void update(Movie entity, @PathParam("id") int id) {
        throw new UnsupportedOperationException();
    }
    
    
    
    
}

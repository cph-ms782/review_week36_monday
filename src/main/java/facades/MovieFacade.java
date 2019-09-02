package facades;

import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private MovieFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public long getMovieCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long renameMeCount = (long)em.createQuery("SELECT COUNT(r) FROM Movie r").getSingleResult();
            return renameMeCount;
        }finally{  
            em.close();
        }
    }


    public Movie addMovie(String title, int year) {
        Movie movie = new Movie(title, year);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        } finally {
            em.close();
        }
    }

    public Movie findByID(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Movie book = em.find(Movie.class, id);
            return book;
        } finally {
            em.close();
        }
    }

    public Long getNumberOfMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> num = em.createQuery("Select COUNT(c) from Movie c", Long.class);
            return num.getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Movie> allMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query
                    = em.createQuery("Select movie from Movie movie", Movie.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Movie> findByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query
                    = em.createQuery("Select movie from Movie movie where movie.title = :title", Movie.class)
                    .setParameter("title", title);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Movie> findByYear(int year) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query
                    = em.createQuery("Select movie from Movie movie where movie.year = :year", Movie.class)
                    .setParameter("year", year);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    
    public static void main(String[] args) {
        
    }
    
    
}

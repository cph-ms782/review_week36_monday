package rest;

import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    //Read this line from a settings-file  since used several places
    private static final String TEST_DB = "jdbc:mysql://localhost:3307/movie_test";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private final Movie m1 = new Movie("Some txt", 1978);
    private final Movie m2 = new Movie("aaa", 1975);

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);

        //NOT Required if you use the version of EMF_Creator.createEntityManagerFactory used above        
        //System.setProperty("IS_TEST", TEST_DB);
        //We are using the database on the virtual Vagrant image, so username password are the same for all dev-databases
        httpServer = startServer();

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;

        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(m1);
            em.persist(m2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    /**
     * Create a test that verifies that the server is up (similar to the 
     * “Hello World” response)
     */
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given()
                .when()
                .get("/movies")
                .then()
                .statusCode(200);
    }

    /**
     * This test assumes the database contains two rows
     * 
     * @throws Exception 
     */
    @Test
    public void testDummyMsg() throws Exception {
        given()
                .contentType("application/json")
                .get("/movies/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", equalTo("Hello World"));
    }

    /**
     * Create a test for the endpoint: api/movie/count (expected result, 
     * depends on how many movies you created before each test ).
     * 
     * @throws Exception 
     */
    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/movies/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
    
    /**
     * Create a test for the endpoint api/movie/all and assert that the body 
     * contains an actor named Freddy Frøstrup (or just an actor added by your 
     * BEFORE code)
     * 
     * OBS Jeg brugte andre fields end i opgaven. Så har søgt på årstal istedet
     * 
     * @throws Exception 
     */
    @Test
    public void testGetAll() throws Exception {
        given()
                .contentType("application/json")
                .get("/movies/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("list.year", hasItems(1975,1978));
        
    } 
            
    /**
     * Create a test for an endpoint: api/movie/name/{name}. Use a name you know
     * exists, and (for red students) also try with a name that does not exist
     * (obviously this requires that you know what you return in such a case)
     * 
     * OBS søger på film titel i stedet for skuespiller navn
     * @throws Exception 
     */
    @Test
    public void testGetName() throws Exception {
        given()
                .contentType("application/json")
                .get("/movies/movie/aaa").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("list.title", hasItems("aaa"));
    } 
    @Test
    public void testGetWrongName() throws Exception {
        List<Movie> list = new ArrayList();
        given()
                .contentType("application/json")
                .get("/movies/movie/Hansens").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("list.title", hasItems(list.toArray()));
    } 

    /**
     * Create a test for the an endpoint:  api/movie/{id} and verify that you get the expected Movie 
     * @throws Exception 
     */
    @Disabled
    @Test
    public void testGetID() throws Exception {
        List<Movie> list = new ArrayList();
        given()
                .contentType("application/json")
                .get("/movies/15").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("list.id", equalTo(15));
    } 
    
}

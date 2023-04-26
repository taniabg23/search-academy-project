package co.empathy.academy.search.services;

import co.empathy.academy.search.model.*;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Service
public class MoviesService {

    @Autowired
    private ElasticClient client;
    private BufferedReader basicsBF;
    private BufferedReader akasBF;
    private BufferedReader principalsBF;
    private BufferedReader ratingsBF;
    private BufferedReader crewBF;
    private boolean endMovies = false;
    private static final int MAX_NUM_MOVIES = 30000;
    private List<Movie> movies = new LinkedList<>();

    /**
     * Method that reads and indexes all the IMDb files info into an Elasticsearch index
     *
     * @param basics     basics' file
     * @param akas       akas' file
     * @param principals principals' file
     * @param ratings    ratings' file
     * @param crew       crew's file
     * @return number of movies indexed
     * @throws IOException
     */
    public int readData(MultipartFile basics, MultipartFile akas,
                        MultipartFile principals, MultipartFile ratings,
                        MultipartFile crew) throws IOException {

        inicializeBuffereds(basics, akas, principals, ratings, crew);

        int moviesReaden = 0;
        int totalMoviesIndexed = 0;
        String tconst;
        Movie basic;

        client.createIndex();

        // mientras queden películas
        while (!this.endMovies) {
            //leemos de 30.000 en 30.000
            while (moviesReaden < MAX_NUM_MOVIES) {
                //leer los basics
                String lineBasic = this.basicsBF.readLine();
                this.basicsBF.mark(3000);
                String nextLineBasic = this.basicsBF.readLine();
                this.basicsBF.reset();
                if (lineBasic == null) {
                    this.endMovies = true;
                    break;
                }
                String[] valuesBasic = lineBasic.split("\t");
                if (Integer.parseInt(valuesBasic[4]) == 1) {
                    continue;
                }
                tconst = valuesBasic[0];
                basic = (nextLineBasic == null)
                        ? getMovie(tconst, valuesBasic, null)
                        : getMovie(tconst, valuesBasic, nextLineBasic.split("\t")[0]);
                movies.add(basic);
                moviesReaden++;
                totalMoviesIndexed++;
            }
            client.bulkMovies(movies);
            movies.clear();
            moviesReaden = 0;
        }
        return totalMoviesIndexed;
    }

    /**
     * Method that inicialize the BufferedReaders of all the files and reads its headers
     *
     * @param basics     basics' file
     * @param akas       akas' file
     * @param principals principals' file
     * @param ratings    ratings' file
     * @param crew       crew's file
     * @throws IOException
     */
    private void inicializeBuffereds(MultipartFile basics, MultipartFile akas, MultipartFile principals, MultipartFile ratings, MultipartFile crew) throws IOException {
        //establecemos los bufferedreaders
        this.basicsBF = new BufferedReader(new InputStreamReader(basics.getInputStream()));
        this.akasBF = new BufferedReader(new InputStreamReader(akas.getInputStream()));
        this.principalsBF = new BufferedReader(new InputStreamReader(principals.getInputStream()));
        this.ratingsBF = new BufferedReader(new InputStreamReader(ratings.getInputStream()));
        this.crewBF = new BufferedReader(new InputStreamReader(crew.getInputStream()));

        //leemos las cabeceras
        this.basicsBF.readLine();
        this.akasBF.readLine();
        this.principalsBF.readLine();
        this.ratingsBF.readLine();
        this.crewBF.readLine();
    }

    /**
     * Method that returns a Movie object with all its values, akas, principals and directors
     *
     * @param tconst      tconst of the movie we want to create
     * @param valuesBasic values of the movie we want to create
     * @param nextTconst  tconst of the next movie
     * @return a complete Movie object
     * @throws IOException
     */
    private Movie getMovie(String tconst, String[] valuesBasic, String nextTconst) throws IOException {
        String titleType, primaryTitle, originalTitle;
        int startYear, endYear, runTimeMinutos;
        List<String> genres;
        Movie basic;
        //leer el resto
        //leer akas
        System.out.println("Leyendo película: " + tconst + ", next: " + nextTconst);
        System.out.println("Leyendo akas");
        List<Aka> akasBasic = readAkas(tconst, nextTconst);


        //leer principals
        System.out.println("Leyendo principals");
        List<Principal> principalsBasic = readPrincipals(tconst, nextTconst);


        //leer rating
        String lineRating = this.ratingsBF.readLine();
        this.ratingsBF.mark(3000);
        double averageRating = 0.0;
        int numVotes = 0;
        while (lineRating != null) {
            String[] valuesRating = lineRating.split("\t");
            if (tconst.equals(valuesRating[0])) {
                averageRating = Double.parseDouble(valuesRating[1]);
                numVotes = Integer.parseInt(valuesRating[2]);
                break;
            } else if (nextTconst.equals(valuesRating[0])) {
                averageRating = 0.0;
                numVotes = 0;
                this.ratingsBF.reset();
                break;
            }
            this.ratingsBF.mark(3000);
            lineRating = this.ratingsBF.readLine();
        }

        //leer crew
        String lineCrew = this.crewBF.readLine();
        this.crewBF.mark(3000);
        List<String> directorsConst;
        List<Director> directors = new LinkedList<>();

        while (lineCrew != null) {
            String[] valuesCrew = lineCrew.split("\t");
            if (tconst.equals(valuesCrew[0])) {
                if (!valuesCrew[1].equals("\\N")) {
                    directorsConst = List.of(valuesCrew[1].split(","));
                    for (String nconst : directorsConst) {
                        directors.add(new Director(nconst));
                    }
                } else {
                    directors = new LinkedList<>();
                }
                break;
            } else if (nextTconst.equals(valuesCrew[0])) {
                directors = new LinkedList<>();
                this.crewBF.reset();
                break;
            }
            this.crewBF.mark(3000);
            lineCrew = this.crewBF.readLine();
        }

        //crear movie
        titleType = valuesBasic[1];
        primaryTitle = valuesBasic[2];
        originalTitle = valuesBasic[3];
        startYear = !(valuesBasic[5].equals("\\N"))
                ? Integer.parseInt(valuesBasic[5])
                : 0;
        endYear = !(valuesBasic[6].equals("\\N"))
                ? Integer.parseInt(valuesBasic[6])
                : 0;
        runTimeMinutos = !(valuesBasic[7].equals("\\N"))
                ? Integer.parseInt(valuesBasic[7])
                : 0;
        genres = List.of(valuesBasic[8].split(","));
        basic = new Movie(tconst, titleType, primaryTitle, originalTitle, false, startYear, endYear, runTimeMinutos, genres, averageRating, numVotes, akasBasic, directors, principalsBasic);
        return basic;
    }

    /**
     * Method that returns a list of the Akas of the movie with the param tconst
     *
     * @param tconst     tconst of the movie we want to know its akas
     * @param nextTconst tconst of the next movie we are going to look for
     * @return the list of the tconst movie akas
     * @throws IOException
     */
    private List<Aka> readAkas(String tconst, String nextTconst) throws IOException {
        List<Aka> akas = new LinkedList<>();
        this.akasBF.mark(3000);
        String lineAka = this.akasBF.readLine();

        String[] valuesAka;
        String tconstA;
        Aka aka;

        while (lineAka != null) {
            valuesAka = lineAka.split("\t");
            tconstA = valuesAka[0];
            if (tconstA.equals(tconst)) {
                while (tconstA.equals(tconst) && lineAka != null) {
                    aka = getAka(valuesAka);
                    akas.add(aka);
                    this.akasBF.mark(3000);
                    lineAka = this.akasBF.readLine();
                    if (lineAka == null) break;
                    valuesAka = lineAka.split("\t");
                    tconstA = valuesAka[0];
                }
                this.akasBF.reset();
                return akas;
            } else {
                while (lineAka != null && !tconst.equals(tconstA)) {
                    this.akasBF.mark(3000);
                    lineAka = this.akasBF.readLine();
                    if (lineAka == null) {
                        break;
                    }
                    valuesAka = lineAka.split("\t");
                    tconstA = valuesAka[0];
                    if (nextTconst != null && nextTconst.equals(tconstA)) {
                        this.akasBF.reset();
                        return new LinkedList<>();
                    }
                }
            }
        }
        return new LinkedList<>();
    }

    /**
     * Method that returns a list of the Principals of the movie with the param tconst
     *
     * @param tconst     tconst of the movie we want to know its principals
     * @param nextTconst tconst of the next movie we are going to look for
     * @return the list of the tconst movie principals
     * @throws IOException
     */
    private List<Principal> readPrincipals(String tconst, String nextTconst) throws IOException {
        List<Principal> principals = new LinkedList<>();
        this.principalsBF.mark(3000);
        String linePrincipal = this.principalsBF.readLine();

        String[] valuesPrincipal;
        String tconstP;
        Principal principal;

        while (linePrincipal != null) {
            valuesPrincipal = linePrincipal.split("\t");
            tconstP = valuesPrincipal[0];
            if (tconstP.equals(tconst)) {
                while (tconstP.equals(tconst) && linePrincipal != null) {
                    principal = getPrincipal(valuesPrincipal);
                    principals.add(principal);
                    this.principalsBF.mark(3000);
                    linePrincipal = this.principalsBF.readLine();
                    if (linePrincipal == null) break;
                    valuesPrincipal = linePrincipal.split("\t");
                    tconstP = valuesPrincipal[0];
                }
                this.principalsBF.reset();
                return principals;
            } else {
                while (linePrincipal != null && !tconst.equals(tconstP)) {
                    this.principalsBF.mark(3000);
                    linePrincipal = this.principalsBF.readLine();
                    if (linePrincipal == null) {
                        break;
                    }
                    valuesPrincipal = linePrincipal.split("\t");
                    tconstP = valuesPrincipal[0];
                    if (nextTconst != null && nextTconst.equals(tconstP)) {
                        this.principalsBF.reset();
                        return new LinkedList<>();
                    }
                }
            }
        }
        return new LinkedList<>();
    }

    /**
     * Method that returns a Principal object with the param values
     *
     * @param valuesPrincipal values of the Principal object we want to create
     * @return a Principal object
     */
    private Principal getPrincipal(String[] valuesPrincipal) {
        String nconst;
        Principal principal;
        String characters;
        nconst = valuesPrincipal[1];
        characters = valuesPrincipal[4];
        principal = new Principal(new Name(nconst), characters);
        return principal;
    }

    /**
     * Method that returns an Aka object with the param values
     *
     * @param valuesAka values of the Aka object we want to create
     * @return an Aka object
     */
    private Aka getAka(String[] valuesAka) {
        String title;
        Aka aka;
        String region;
        boolean isOriginalTitle;
        String language;
        title = valuesAka[2];
        region = valuesAka[3];
        language = valuesAka[4];
        isOriginalTitle = valuesAka[7].equals("\\N") || Integer.parseInt(valuesAka[7]) == 0
                ? false
                : true;
        aka = new Aka(title, region, language, isOriginalTitle);
        return aka;
    }
}

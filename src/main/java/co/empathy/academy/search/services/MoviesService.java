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
    private BufferedReader episodesBF;
    private boolean endMovies = false;
    private static final int MAX_NUM_MOVIES = 30000;
    private List<Movie> movies = new LinkedList<>();

    public int readData(MultipartFile basics, MultipartFile akas,
                        MultipartFile principals, MultipartFile ratings,
                        MultipartFile crew, MultipartFile episodes) throws IOException {

        inicializeBuffereds(basics, akas, principals, ratings, crew, episodes);

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
                if (lineBasic == null) {
                    this.endMovies = true;
                    break;
                }
                String[] valuesBasic = lineBasic.split("\t");
                if (Integer.parseInt(valuesBasic[4]) == 1) {
                    continue;
                }
                tconst = valuesBasic[0];

                basic = getBasic(tconst, valuesBasic);
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

    private Movie getBasic(String tconst, String[] valuesBasic) throws IOException {
        String titleType, primaryTitle, originalTitle;
        int startYear, endYear, runTimeMinutos;
        List<String> genres;
        Movie basic;
        //leer el resto
        //leer akas
        List<Aka> akasBasic = readAkas(tconst);

        //leer principals
        List<Principal> principalsBasic = readPrincipals(tconst);

        //leer rating
        String lineRating = this.ratingsBF.readLine();
        double averageRating;
        int numVotes;
        if (lineRating != null) {
            String[] valuesRating = lineRating.split("\t");
            averageRating = Double.parseDouble(valuesRating[1]);
            numVotes = Integer.parseInt(valuesRating[2]);
        } else {
            averageRating = 0.0;
            numVotes = 0;
        }

        //leer crew
        String lineCrew = this.crewBF.readLine();
        List<String> directorsConst;
        List<Director> directors = new LinkedList<>();
        if (lineCrew != null) {
            String[] valuesCrew = lineCrew.split("\t");
            directorsConst = valuesCrew[1].equals("\\N") ? new LinkedList<>() : List.of(valuesCrew[1].split(","));
            for (String nconst : directorsConst) {
                directors.add(new Director(nconst));
            }
        }

        //crear movie
        titleType = valuesBasic[1];
        primaryTitle = valuesBasic[2];
        originalTitle = valuesBasic[3];
        startYear = !(valuesBasic[5].equals("\\N")) ? Integer.parseInt(valuesBasic[5]) : 0;
        endYear = !(valuesBasic[6].equals("\\N")) ? Integer.parseInt(valuesBasic[6]) : 0;
        runTimeMinutos = !(valuesBasic[7].equals("\\N")) ? Integer.parseInt(valuesBasic[7]) : 0;
        genres = List.of(valuesBasic[8].split(","));
        basic = new Movie(tconst, titleType, primaryTitle, originalTitle, false, startYear, endYear, runTimeMinutos, genres, averageRating, numVotes, akasBasic, directors, principalsBasic);
        return basic;
    }

    private void inicializeBuffereds(MultipartFile basics, MultipartFile akas, MultipartFile principals, MultipartFile ratings, MultipartFile crew, MultipartFile episodes) throws IOException {
        //establecemos los bufferedreaders
        this.basicsBF = new BufferedReader(new InputStreamReader(basics.getInputStream()));
        this.akasBF = new BufferedReader(new InputStreamReader(akas.getInputStream()));
        this.principalsBF = new BufferedReader(new InputStreamReader(principals.getInputStream()));
        this.ratingsBF = new BufferedReader(new InputStreamReader(ratings.getInputStream()));
        this.crewBF = new BufferedReader(new InputStreamReader(crew.getInputStream()));
        this.episodesBF = new BufferedReader(new InputStreamReader(episodes.getInputStream()));

        //leemos las cabeceras
        this.basicsBF.readLine();
        this.akasBF.readLine();
        this.principalsBF.readLine();
        this.ratingsBF.readLine();
        this.crewBF.readLine();
        this.episodesBF.readLine();
    }

    private List<Aka> readAkas(String tconst) throws IOException {
        List<Aka> akas = new LinkedList<>();
        this.akasBF.mark(3000);
        String lineAka = this.akasBF.readLine();

        String[] valuesAka;
        String title, region, language;
        boolean isOriginalTitle;
        Aka aka;

        while (lineAka != null) {
            valuesAka = lineAka.split("\t");
            if (valuesAka[0].equals(tconst)) {
                //creo el aka
                title = valuesAka[2];
                region = valuesAka[3];
                language = valuesAka[4];
                isOriginalTitle = Integer.parseInt(valuesAka[7]) == 1 ? true : false;
                aka = new Aka(title, region, language, isOriginalTitle);
                akas.add(aka);
                //marco y leo otra línea
                this.akasBF.mark(3000);
                lineAka = this.akasBF.readLine();
            } else {
                this.akasBF.reset();
                break;
            }
        }
        return akas;
    }

    private List<Principal> readPrincipals(String tconst) throws IOException {
        List<Principal> principals = new LinkedList<>();
        this.principalsBF.mark(3000);
        String linePrincipal = this.principalsBF.readLine();

        String[] valuesPrincipal;
        String nconst, characters;
        Principal principal;

        while (linePrincipal != null) {
            valuesPrincipal = linePrincipal.split("\t");
            if (valuesPrincipal[0].equals(tconst)) {
                //creo el aka
                nconst = valuesPrincipal[1];
                characters = valuesPrincipal[4];
                principal = new Principal(new Name(nconst), characters);
                principals.add(principal);
                //marco y leo otra línea
                this.principalsBF.mark(3000);
                linePrincipal = this.principalsBF.readLine();
            } else {
                this.principalsBF.reset();
                break;
            }
        }
        return principals;
    }
}

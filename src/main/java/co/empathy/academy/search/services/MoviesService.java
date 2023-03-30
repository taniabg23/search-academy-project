package co.empathy.academy.search.services;

import co.empathy.academy.search.model.Aka;
import co.empathy.academy.search.model.Basic;
import co.empathy.academy.search.model.Episode;
import co.empathy.academy.search.model.Principal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Service
public class MoviesService {

    private BufferedReader basicsBF;
    private BufferedReader akasBF;
    private BufferedReader principalsBF;
    private BufferedReader ratingsBF;
    private BufferedReader crewBF;
    private BufferedReader episodesBF;
    private boolean endMovies = false;
    private static final int MAX_NUM_MOVIES = 200;
    private List<Basic> movies = new LinkedList<>();

    public List<Basic> readData(MultipartFile basics, MultipartFile akas,
                                MultipartFile principals, MultipartFile ratings,
                                MultipartFile crew, MultipartFile episodes) throws IOException {

        inicializeBuffereds(basics, akas, principals, ratings, crew, episodes);

        int moviesReaden = 0;
        String tconst;
        Basic basic;

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
        }
        return movies;
    }

    private Basic getBasic(String tconst, String[] valuesBasic) throws IOException {
        String titleType, primaryTitle, originalTitle;
        int startYear, endYear, runTimeMinutos;
        List<String> genres;
        Basic basic;
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
        List<String> directorsConst, writersConst;
        if (lineCrew != null) {
            String[] valuesCrew = lineCrew.split("\t");
            directorsConst = valuesCrew[1].equals("\\N") ? new LinkedList<>() : List.of(valuesCrew[1].split(","));
            writersConst = valuesCrew[2].equals("\\N") ? new LinkedList<>() : List.of(valuesCrew[2].split(","));
        } else {
            directorsConst = new LinkedList<>();
            writersConst = new LinkedList<>();
        }

        //leer episodes
        List<Episode> episodesBasic = readEpisodes(tconst);

        //crear movie
        titleType = valuesBasic[1];
        primaryTitle = valuesBasic[2];
        originalTitle = valuesBasic[3];
        startYear = !(valuesBasic[5].equals("\\N")) ? Integer.parseInt(valuesBasic[5]) : -1;
        endYear = !(valuesBasic[6].equals("\\N")) ? Integer.parseInt(valuesBasic[6]) : -1;
        runTimeMinutos = !(valuesBasic[7].equals("\\N")) ? Integer.parseInt(valuesBasic[7]) : -1;
        genres = List.of(valuesBasic[8].split(","));
        basic = new Basic(tconst, titleType, primaryTitle, originalTitle, false, startYear, endYear, runTimeMinutos, genres, akasBasic, principalsBasic, directorsConst, writersConst, episodesBasic, averageRating, numVotes);
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

    private List<Episode> readEpisodes(String tconst) throws IOException {
        List<Episode> episodes = new LinkedList<>();
        this.episodesBF.mark(3000);
        String lineEpisode = this.episodesBF.readLine();

        String[] valuesEpisode;
        String episodeConst;
        int seasonNumber, episodeNumber;
        Episode episode;

        while (lineEpisode != null) {
            valuesEpisode = lineEpisode.split("\t");
            if (valuesEpisode[1].equals(tconst)) {
                //creo el aka
                episodeConst = valuesEpisode[1];
                seasonNumber = !(valuesEpisode[2].equals("\\N")) ? Integer.parseInt(valuesEpisode[2]) : -1;
                episodeNumber = !(valuesEpisode[3].equals("\\N")) ? Integer.parseInt(valuesEpisode[3]) : -1;
                episode = new Episode(episodeConst, seasonNumber, episodeNumber);
                episodes.add(episode);
                //marco y leo otra línea
                this.episodesBF.mark(3000);
                lineEpisode = this.episodesBF.readLine();
            } else {
                this.episodesBF.reset();
                break;
            }
        }
        return episodes;
    }

    private List<Aka> readAkas(String tconst) throws IOException {
        List<Aka> akas = new LinkedList<>();
        this.akasBF.mark(3000);
        String lineAka = this.akasBF.readLine();

        String[] valuesAka;
        String title, region, language;
        List<String> types, attributes;
        boolean isOriginalTitle;
        Aka aka;

        while (lineAka != null) {
            valuesAka = lineAka.split("\t");
            if (valuesAka[0].equals(tconst)) {
                //creo el aka
                title = valuesAka[2];
                region = !(valuesAka[3].equals("\\N")) ? valuesAka[3] : null;
                language = !(valuesAka[4].equals("\\N")) ? valuesAka[4] : null;
                types = !(valuesAka[5].equals("\\N")) ? List.of(valuesAka[5].split(",")) : null;
                attributes = !(valuesAka[6].equals("\\N")) ? List.of(valuesAka[6].split(",")) : null;
                isOriginalTitle = Integer.parseInt(valuesAka[7]) == 1 ? true : false;
                aka = new Aka(title, region, language, types, attributes, isOriginalTitle);
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
        String nconst, category, job, characters;
        Principal principal;

        while (linePrincipal != null) {
            valuesPrincipal = linePrincipal.split("\t");
            if (valuesPrincipal[0].equals(tconst)) {
                //creo el aka
                nconst = valuesPrincipal[1];
                category = valuesPrincipal[2];
                job = valuesPrincipal[3];
                characters = valuesPrincipal[4];
                principal = new Principal(nconst, category, job, characters);
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

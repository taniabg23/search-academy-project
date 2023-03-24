package co.empathy.academy.search.services;

import co.empathy.academy.search.model.Basic;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Service
public class MoviesService {

    private LinkedList<Basic> basics = new LinkedList<>();

    public LinkedList<Basic> reedBasicsTsv(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        int counter = 0;
        String line;
        String[] values;
        Basic basic;
        while ((line = bufferedReader.readLine()) != null) {
            if (counter > 0) {
                values = line.split("\t");
                String tconst = values[0];
                String titleType = values[1];
                String primaryTitle = values[2];
                String originalTitle = values[3];
                boolean isAdult = Integer.parseInt(values[4]) == 1 ? true : false;
                int startYear = Integer.parseInt(values[5]);
                int endYear = !(values[6].equals("\\N")) ? Integer.parseInt(values[6]) : -1;
                int runTimeMinutos = !(values[7].equals("\\N")) ? Integer.parseInt(values[7]) : -1;
                List<String> genres = getGenres(values[8]);
                basic = new Basic(tconst, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runTimeMinutos, genres);
                basics.add(basic);
            }
            counter++;
        }
        bufferedReader.close();
        return basics;
    }

    private List<String> getGenres(String genres) {
        String[] genresSplit = genres.split(",");
        List<String> genresList = new LinkedList<>();
        for (String genre : genresSplit) {
            genresList.add(genre);
        }
        return genresList;
    }
}

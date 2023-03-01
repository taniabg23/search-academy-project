package co.empathy.academy.search.services;

import co.empathy.academy.search.ElasticClient;

import java.io.IOException;

public class SearchServiceImpl implements SearchService {

    private ElasticClient elastic;

    public SearchServiceImpl(ElasticClient ec) {
        this.elastic = ec;
    }

    @Override
    public String getJsonQueryAndClusterName(String query) {
        String ret = "{\n";
        ret += "\t\"query\": \"" + query + "\",\n";
        try {
            ret += "\t\"clusterName\": \"" + elastic.getClusterName() + "\"\n";
        } catch (IOException e) {
            throw new RuntimeException("IOException cause by the elastic client");
        }
        ret += "}";
        return ret;
    }
}

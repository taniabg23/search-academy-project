package co.empathy.academy.search.services;

import co.empathy.academy.search.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticClient elastic;

    @Override
    public String getJsonQueryAndClusterName(String query) {
        String ret = "{\n";
        ret += "\t\"query\": \"" + query + "\",\n";
        ret += "\t\"clusterName\": \"" + elastic.getClusterName() + "\"\n";
        ret += "}";
        return ret;
    }
}

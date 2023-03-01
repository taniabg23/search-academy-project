package co.empathy.academy.search;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class SearchController {

    private ElasticClient elastic = new ElasticClient();

    @RequestMapping("/search")
    public String search(@RequestParam String query) throws IOException {
        String ret = "{\n";
        ret += "\t\"query\": \"" + query + "\",\n";
        ret += "\t\"clusterName\": \"" + elastic.getClusterName() + "\"\n";
        ret += "}";
        return ret;
    }
}

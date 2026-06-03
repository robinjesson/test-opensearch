package fr.robinjesson.testelasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "fr.robinjesson.testelasticsearch.repo")
public class TestelasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestelasticsearchApplication.class, args);
    }

}

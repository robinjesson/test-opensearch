package fr.robinjesson.testelasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "fr.robinjesson.testelasticsearch.repo")
@EnableAsync
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class TestelasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestelasticsearchApplication.class, args);
    }

}

package fr.robinjesson.testelasticsearch.model.postgres;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ex: "FAN", "SF"

    private String name; // ex: "Fantasy", "Science Fiction"
}
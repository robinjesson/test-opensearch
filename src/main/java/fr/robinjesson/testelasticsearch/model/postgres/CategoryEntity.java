package fr.robinjesson.testelasticsearch.model.postgres;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "categories")
@Getter
@Setter
@Audited
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ex: "FAN", "SF"

    private String name; // ex: "Fantasy", "Science Fiction"
}
package fr.robinjesson.testelasticsearch.model.postgres;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "books_categories",
            joinColumns = @JoinColumn(name = "fk_book_id"),          // <-- Clé étrangère pointant vers la table books
            inverseJoinColumns = @JoinColumn(name = "fk_category_id")
    )
    private Set<CategoryEntity> categories;
}
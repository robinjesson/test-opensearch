# Test Elasticsearch

Une application Spring Boot pour explorer et apprendre Elasticsearch.

## Architecture

- **Spring Boot 4.0.6** avec Spring Data Elasticsearch
- **Elasticsearch 7.17.16** (via Docker)
- **Kibana 7.17.16** (Interface de gestion Elasticsearch - optionnel)
- **REST API** pour créer, rechercher et supprimer des documents

## Prérequis

- Java 17+
- Maven 3.8+
- Docker et Docker Compose
- Port 8080 (Spring Boot), 9200 (Elasticsearch), 5601 (Kibana)

## Démarrage rapide

### 1. Lancer Elasticsearch avec Docker Compose

```bash
docker-compose up -d
```

Cela démarre :
- **Elasticsearch** sur http://localhost:9200
- **Kibana** sur http://localhost:5601

Vérifiez que Elasticsearch est opérationnel :

```bash
curl http://localhost:9200
```

### 2. Compiler l'application

```bash
mvn clean install
```

### 3. Démarrer l'application Spring Boot

```bash
java -jar target/testelasticsearch-0.0.1-SNAPSHOT.jar
```

L'application démarre sur http://localhost:8080

## API Endpoints

### Créer un document

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "id":"1",
    "title":"Elasticsearch Guide",
    "content":"A comprehensive guide to Elasticsearch",
    "author":"John Doe"
  }'
```

**Réponse:**
```json
{
  "id":"1",
  "title":"Elasticsearch Guide",
  "content":"A comprehensive guide to Elasticsearch",
  "author":"John Doe"
}
```

### Rechercher des documents

```bash
curl -X GET "http://localhost:8080/api/books/search?query=Elasticsearch"
```

**Réponse:**
```json
[
  {
    "id":"1",
    "title":"Elasticsearch Guide",
    "content":"A comprehensive guide to Elasticsearch",
    "author":"John Doe"
  }
]
```

### Supprimer un document

```bash
curl -X DELETE http://localhost:8080/api/books/1
```

## Structure du projet

```
src/main/
├── java/fr/robinjesson/testelasticsearch/
│   ├── TestelasticsearchApplication.java    # Point d'entrée
│   ├── api/
│   │   └── BookController.java              # REST endpoints
│   ├── model/
│   │   └── Book.java                        # Entité Elasticsearch
│   ├── repo/
│   │   └── BookRepository.java              # Repository Elasticsearch
│   ├── service/
│   │   └── BookService.java                 # Logique métier
│   └── config/
│       └── ElasticsearchConfig.java         # Configuration
└── resources/
    ├── application.properties                # Config Spring
    └── elasticsearch/mappings/
        └── books.json                        # Mapping Elasticsearch
```

## Configuration

Les paramètres Elasticsearch sont configurés dans `application.properties` :

```properties
spring.elasticsearch.uris=http://localhost:9200
spring.elasticsearch.ssl.verification-mode=none
```

## Arrêter l'infrastructure

### Arrêter l'application Spring Boot

```bash
# Si lancée au premier plan : Ctrl+C
# Si lancée en background : kill <PID>
```

### Arrêter Elasticsearch et Kibana

```bash
docker-compose down
```

Pour supprimer aussi les données :

```bash
docker-compose down -v
```

## Dépannage

### Elasticsearch ne se connecte pas

1. Vérifiez que le conteneur est en cours d'exécution :
   ```bash
   docker-compose ps
   ```

2. Vérifiez la connexion :
   ```bash
   curl http://localhost:9200
   ```

3. Consultez les logs :
   ```bash
   docker-compose logs elasticsearch
   ```

### Port 8080 ou 9200 déjà utilisé

Modifiez les ports dans `docker-compose.yml` ou `application.properties`, ou arrêtez les services qui les utilisent.

### Erreurs de compilation

Assurez-vous que Java 17 ou supérieur est utilisé :

```bash
java -version
```

## Ressources

- [Elasticsearch Documentation](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/index.html)
- [Spring Data Elasticsearch](https://spring.io/projects/spring-data-elasticsearch)
- [Kibana](https://www.elastic.co/guide/en/kibana/7.17/index.html)

## Notes de développement

- L'index `books` est créé automatiquement au premier démarrage
- Les documents sont stockés en JSON dans Elasticsearch
- Les recherches utilisent l'analyseur "standard" pour la tokenisation
- Le champ `author` est un keyword (pas analysé)
- Les champs `title` et `content` sont des text fields (analysés)

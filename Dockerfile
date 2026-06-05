FROM amazoncorretto:25-alpine
ADD target/testelasticsearch*.jar /testelasticsearch.jar
EXPOSE 8080
CMD java -jar /testelasticsearch.jar
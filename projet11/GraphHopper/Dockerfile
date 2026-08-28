FROM eclipse-temurin:21-jre

WORKDIR /graphhopper

COPY graphhopper-web-11.0.jar .
COPY config.yml .
COPY ile-de-france-260810.osm.pbf .

EXPOSE 8989

ENTRYPOINT ["java", "-jar", "graphhopper-web-11.0.jar", "server", "config.yml"]
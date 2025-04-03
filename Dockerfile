# build
FROM maven:3.8.8-amazoncorretto-21-al2023 as build-stage
WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests

# run
FROM amazoncorretto:21.0.5
WORKDIR /app

COPY --from=build-stage ./build/target/*.jar ./ssbbank.jar

EXPOSE 8080
EXPOSE 9090

ENV URL_DATABASE=''
ENV DATABASE_USERNAME=''
ENV DATABASE_PASSWORD=''
ENV JWT_SECRET=''
ENV PORT_NUMBER=''

ENV SPRING_PROFILES_ACTIVE='default'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT java -jar ssbbank.jar

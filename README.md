# World of Warcraft Auctions - Batch Application

## Pre Requisites

* [OpenJDK 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
* [Maven 3.6.3](https://maven.apache.org/download.cgi)
* [Docker](https://hub.docker.com/search/?type=edition&offering=community)
* [Blizzard API Key](https://develop.battle.net/access/clients)

## Project Structure

The project uses Blizzard API for the popular game World of Warcraft. It downloads the World of Warcraft Auction House
data and provides statistics about items prices evolution. Since the data may be huge, the project uses Batch 
processing to analyze the information. 

* batch - the batch code that integrates with Blizzard API and process the data.   
* quarkus-batch - integration with Quarkus to support the JBatch implementation (with Jberet).
* itest - integrations tests.

## Libraries and Infra

The project uses [Quarkus](https://quarkus.io) as the Java stack, and the built in [SmallRye](https://smallrye.io) 
components as the [MicroProfile](https://microprofile.io) implementations.

The required infrastructure provided by Docker includes:

* Postgres Database 

## Build

Use Maven to build the project with the following command from the project root:

```bash
mvn install
```

## Run

Start the required infrastructure to run the application from the project root: 

```bash
docker-compose up -d
```

To execute the Batch Application, run the following command from each `batch` folder root:

```bash
java -Dapi.battle.net.clientId=[REPLACE_ME] -Dapi.battle.net.clientSecret=[REPLACE_ME] -jar target/auctions-batch-runner.jar
```

Remember to replace the placeholders in the command by the real clientId and clientSecret required to access 
[Blizzard API](https://develop.battle.net/access/clients).

## Test the Applications

The Application provides a REST endpoint to call the required batch processes.

### Prepare

The `Prepare Batch` is used to retrieve the initial metadata and create the Batch structure to process auction data:

```bash
curl http://localhost:8080/batch/prepare
```

### Process

The `Process Batch` process the auction data for a set of Realms. Requires the region and the realm id to process:

```bash
curl http://localhost:8080/batch/process/us/11
```

This will process the auction data for the Realm `Tichondrius` in the `US` region. It may take a while to finish. After 
the processing is done you can check the `AUCTIONSTATISTICS` table in the database.

## Blog posts ##

* [Java EE 7 Batch Processing and World of Warcraft – Part 1](http://www.radcortez.com/java-ee-7-batch-processing-and-world-of-warcraft-part-1)

* [Java EE 7 Batch Processing and World of Warcraft – Part 2](http://www.radcortez.com/java-ee-7-batch-processing-and-world-of-warcraft-part-2)

## How to run ? ##


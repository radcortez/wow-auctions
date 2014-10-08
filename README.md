# World of Warcraft Auctions - Java EE 7 Batch Application #

## How to run ? ##

* You need JDK 8, Maven 3 and Wildfly 8.1.0 to run the application.

* Build the code using Maven with the command: `mvn clean install`.

* Run Wildfly.

* Move to the `batch` project folder and execute `mvn wildfly:deploy-only`

* Go to http://localhost:8080/auctions-batch/ for search screen.

* Go to http://localhost:8080/auctions-batch/batchs.html for search screen.

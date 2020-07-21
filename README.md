This Spring Boot app uses Java 11 and Maven and requires lombok to be enabled in your IDE of choice. It also supports Docker.

This app consists of one get endpoint that will get a grouped and sorted list of the most recent buses to arrive at Great Portland Street Station, both stops G and H.
The details provided in the response are the bus number, time in minutes to arrival, the final destination of the bus and the full date and time of arrival of the bus.

After cloning the repository from GitHub the project must be built using mvn clean install to generate the bus.times-0.0.1-SNAPSHOT.jar in the target folder.

Then the app can be run using the docker-compose.yml file if Docker is up and running on your device. Run the "docker-compose up" command in the directory that contains the docker-compose.yml file. 
This will build the app image and run it as a container. An alternate way to run the application is to run the BusTimeApplication.java file in your IDE.

After running with either Docker or from BusTimeApplication.java the app will be running on port 8080.

Springdoc openapi ui can be accessed using the following link:

http://localhost:8080/swagger-ui.html

To retrieve the bus times a GET request must be sent to the following url:

http://localhost:8080/greatPortlandStreetTimes

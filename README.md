## com.example.BuildingTracker

### The brief
In simple terms, you need to create a Java or Kotlin SpringBoot Application (with
Gradle) to allow the user to add, view, update, and delete their buildings.


Allow the user to add new buildings with the parameters:
● Name of the building
● Street
● Number
● Postal code
● City
● Country
● Description


The backend should take a list of buildings to save (could be one or multiple
buildings at the time), and update and delete should be done to one building.
Please make sure to store the data in the database, for example, PostgreSQL.
Before saving the building, find coordinates for the provided building via the
forward-geocoding https://www.geoapify.com/geocoding-api api (You can create a
free account to use in your application), and save the coordinates together with other
parameters to the Database.


Allow users to get all properties with any sorting, paging, etc that you think are
necessary. All the properties such as Database URI, and external HTTP API are
configurable in the YAML file.


### Build
The command to build a new jar is:
- .\gradlew.bat clean bootJar if you're on windows
- ./gradlew clean bootJar if you're on a sensible OS

To build a docker image you need to run
- .\gradlew.bat clean buildImage if you're on windows
- ./gradlew clean buildImage if you're on a sensible OS

Then you should be able to see a new image when you run 
`docker image ls`

You can run it with 
`docker run -p 8080:8080 -e LOCATION_API_KEY=[your api key here] --name test -d test/building-tracker-1 `

### Prerequisites
You will need to set a local environment variable called LOCATION_API_KEY containing an API key for geoapify.com.
Visit https://www.geoapify.com/ to make a free account and get your api key.

### Run 
java -jar [path to jar file]

You can find the built jar in build/libs.

### API Documentation
When you run the app, api documentation can be found at:
- http://localhost:8080/v3/api-docs in json format
- http://localhost:8080/swagger-ui/index.html with a swagger UI

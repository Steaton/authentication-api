# Authentication API

### Configuration
Use environment variasbles to specify the location of the keystore
files, the keystore passwords and the database password. Kubernetes secrets
can be used to inject these from the continuous integration pipeline. 


The available configuration variables available are:

    H2_PASSWORD
    JWT_KEYSTORE_PASSWORD
    JWT_KEYSTORE_PATH
    CLIENT_KEYSTORE_PATH
    CLIENT_KEYSTORE_PASSWORD
    SERVER_KEYSTORE_PATH
    SERVER_KEYSTORE_PASSWORD
    SERVER_TRUSTSTORE_PATH
    SERVER_TRUSTSTORE_PASSWORD

Note: Set the spring profile to local and change your `application.yml` local profile for
convienience.
    
    spring.profiles.active=local
    
### Running the Application

### API Documentation
    https://localhost:8445/swagger-ui.html    

### Health Endpoint
    https://localhost:8445/actuator/health

### Design Notes
- Uses Spring Boot 2
- Actuator enabled for health check
- Logging always contains username for easy filtering
- H2 database with Spring Data used to store accounts in memory only
- Keystore password is loaded from an environment variable and should be injected by Kubernetes from a secret
- Mock MVC is used to unit test controllers
- Exceptions use a rest architecture with the appropriate HttpStatus code
- Passwords are encoded before being placed in the database
- Input validation is done simply, only using JSR-303 annotations
- No passwords or secrets should be hard-coded
- A separarate keystore is used to hold the key for generating the JWT signature
- Added swagger2 with information about the possible error codes


ToDo:
- Exceptions to include only the code and message to avoid exposing call stack trace
- Exception content type set to JSON
- Health check to also check status of Account API and database


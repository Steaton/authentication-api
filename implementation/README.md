# Authentication API


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
- No passwords or secrets should be hard-coded or checked into Git


ToDo:
- Exceptions to include only the code and message to avoid exposing call stack trace
- Exception content type set to JSON
- Health check to also check status of Account API and database


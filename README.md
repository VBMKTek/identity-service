# Identity Service

Identity Service is a service to manage users and them roles.

## Installation

Checkout projects for libraries was used: 

```bash
git checkout git@github.com:vvloi/libraries.git
git checkout git@github.com:vvloi/common.git
```

## Usage

1. Run docker-compose on common.git
```docker-compose
cd common
docker-compose up -d
```

2. Checking nexus started on Docker Desktop
3. Open build.gradle of libraries project and change it from:
```
nexusUrl = uri("https://wren-firm-formerly.ngrok-free.app/repository/maven-releases")
```
to
```
nexusUrl = uri("http://localhost:28082/repository/maven-releases")
```
4. Build libraries if needed
```docker-compose
cd libraries
./gradlew spotlessApply
./gradlew clean build publishToNexusRepo
```
5. Open this project on IDE and start it. I maybe take some time because of download dependencies

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
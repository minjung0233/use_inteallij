# use_inteallij

A small Java and Spring Boot learning repository. The runnable sample project is in [`spring/demo`](spring/demo).

## Sample stack

- Java 11
- Spring Boot 2.7.7 with Gradle Wrapper
- Spring MVC, Thymeleaf, and Bean Validation
- MyBatis and MariaDB JDBC
- WebJars for Bootstrap and related frontend assets

## Run the sample

From the repository root:

```bash
cd spring/demo
./gradlew bootRun
```

On Windows, run `gradlew.bat bootRun` instead. Configure the sample's database connection for your local MariaDB instance before starting it if database access is enabled in the application configuration.

## Layout

```text
spring/demo/
├── build.gradle
├── gradlew
└── src/
    ├── main/
    └── test/
```

This repository is a learning workspace rather than a packaged application; the code and notes are kept in their current sample-project structure.

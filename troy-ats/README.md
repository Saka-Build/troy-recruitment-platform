#STEP-1
After creation of DB name troy_ats, run the below command to create tables in DB.

CREATE USER ats_admin WITH PASSWORD 'TroyAts_&1947';
GRANT CONNECT ON DATABASE troy_ats TO ats_admin;
GRANT USAGE ON SCHEMA public TO ats_admin;
GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO ats_admin;

GRANT USAGE, SELECT, UPDATE
ON ALL SEQUENCES IN SCHEMA public
TO ats_admin;

#STEP-2
Run the below command to create tables in DB.
/db/troy_ats.sql

#STEP-3 - Run the application (no Docker required)
mvnw spring-boot:run

Or build and run the jar:
mvnw clean package
java -jar target/troy-ats-0.0.1-SNAPSHOT.jar

Everything the app needs is already in src/main/resources/application.yaml, so a plain
Maven build works out of the box. Only Postgres must be running on localhost:5432.

#Configuration
application.yaml holds the defaults; every value can be overridden with an environment
variable when needed (Docker, another machine, a different DB):

| Env variable            | Default                                        |
|-------------------------|------------------------------------------------|
| DB_URL                  | jdbc:postgresql://localhost:5432/troy_ats      |
| DB_USER                 | ats_admin                                      |
| DB_PASSWORD             | TroyAts_&1947                                  |
| JWT_SECRET              | built-in dev secret (change for production)    |
| LOG_DIR                 | logs                                           |
| MAIL_USERNAME           | (empty)                                        |
| MAIL_PASSWORD           | (empty)                                        |
| SERVER_PORT             | 8080                                           |
| SPRING_PROFILES_ACTIVE  | database                                       |
| REDIS_HOST / REDIS_PORT | localhost / 6379                               |
| UPLOAD_ORIGINAL_CV_DIR  | C:/work/Docs-Troy-ats/upload/originalcv        |
| UPLOAD_TROY_CV_DIR      | C:/work/Docs-Troy-ats/upload/troycv            |
| UPLOAD_PHOTO_DIR        | C:/work/Docs-Troy-ats/upload/employee/photo    |

#Docker (optional)
docker compose up -d
The env file in docker-compose.yml is optional; without it the container uses the same
application.yaml defaults.

#Swagger API Documentation
http://localhost:8080/swagger-ui/index.html

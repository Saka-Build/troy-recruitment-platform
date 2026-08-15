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

#Update application.yml file with details of DB, username and password.
#Swagger API Documentation
http://localhost:8080/swagger-ui/index.html


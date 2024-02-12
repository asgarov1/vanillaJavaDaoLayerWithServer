FROM postgres
ENV POSTGRES_USER admin
ENV POSTGRES_PASSWORD admin
ENV POSTGRES_DB daodb

COPY src/main/resources/schema.sql /docker-entrypoint-initdb.d/
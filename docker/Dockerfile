FROM postgres:16

# Hasło dla użytkownika postgres
ENV POSTGRES_PASSWORD=changeitlater

# Skrypt inicjalizacyjny
COPY init-db.sh /docker-entrypoint-initdb.d/init-db.sh
RUN chmod +x /docker-entrypoint-initdb.d/init-db.sh

#!/bin/bash
set -e

# Tworzenie użytkownika i bazy danych
psql -v ON_ERROR_STOP=1 --username "postgres" <<-EOSQL
    CREATE USER backend WITH PASSWORD 'VdjzymyUHSwBsDFAs2yel0fPsxVLWrA6';
    CREATE DATABASE backend_database OWNER backend;
EOSQL

# Gestion Fournisseurs - Backend

Backend API développé avec Spring Boot pour la gestion des fournisseurs.

## Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- MySQL 8.0+ (ou H2 pour le développement)

## Installation

1. Cloner le projet
2. Configurer la base de données dans `application.properties`
3. Exécuter : `mvn clean install`
4. Lancer l'application : `mvn spring-boot:run`

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/gestionfournisseurs/
│   │       ├── GestionFournisseursApplication.java
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── model/
│   │       └── config/
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test/
    └── java/
```

## Port par défaut

L'application démarre sur le port **8080**.

## Swagger / OpenAPI

Avec l’application démarrée : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (spec JSON : `/v3/api-docs`).

## Tests

```bash
mvn test
```

Les tests utilisent **H2 en mémoire** (`src/test/resources/application.properties`).

## Docker

Image API : `Dockerfile` à la racine du dossier `Backend`. Voir le **`README.md` à la racine du dépôt** et `docker-compose.yml` pour l’ensemble stack (MySQL + API + front).

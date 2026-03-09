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

L'application démarre sur le port **8080**

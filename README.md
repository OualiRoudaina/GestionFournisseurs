# Gestion des achats et des fournisseurs

Application web **Sujet 3** : gestion des fournisseurs, des commandes d’achat, de l’historique et comparaison d’offres. API REST **Spring Boot 3** et interface **Angular**.

## Technologies

| Couche | Technologie |
|--------|-------------|
| Back-end | Java 17, Spring Boot 3.2, Spring Data JPA, MySQL, Jakarta Validation |
| Front-end | Angular, TypeScript, Nginx (image de production) |
| API | REST, documentation **Swagger / OpenAPI** (springdoc) |
| Déploiement | Docker, **docker-compose** (MySQL + API + front), [GCP (Cloud Run + Cloud SQL)](docs/deploy-gcp.md) |

## Prérequis

- **Docker** et **Docker Compose** (recommandé pour tout lancer), ou  
- **Java 17**, **Maven 3.9+**, **Node.js 18+**, **MySQL 8** pour un lancement local classique.

## Démarrage avec Docker Compose

À la racine du dépôt :

```bash
docker compose up --build
```

- **Interface** : [http://localhost:8082](http://localhost:8082) (Nginx sert l’Angular et proxifie `/api` vers le back-end).  
- **Swagger UI** (recommandé avec Docker) : [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) — même origine que l’app, proxifié par Nginx.  
- **API directe sur l’hôte** : [http://localhost:8090/api](http://localhost:8090/api) (le port **8090** évite un conflit si **8080** est déjà utilisé par un autre programme).  
- **Swagger direct** (sans passer par Nginx) : [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)  
- **MySQL** : port hôte `3306`, base `gestion_fournisseurs`, utilisateur `root`, mot de passe `root` (profil docker ; à changer en production).

> Les ports **80** et **8085** ne sont pas utilisés pour le front Docker de ce projet. Si le navigateur affiche une ancienne application sur `localhost`, utilisez `http://localhost:8082`, ou en dev local **8080** pour l’API seule.

Arrêt : `docker compose down` (ajouter `-v` pour supprimer le volume MySQL).

## Développement local (sans Docker)

1. **MySQL** : créer la base `gestion_fournisseurs` (ou laisser Hibernate la créer si `ddl-auto=update`).  
2. **Back-end** : éditer `Backend/src/main/resources/application.properties` (URL, utilisateur, mot de passe), puis :

   ```bash
   cd Backend
   mvn spring-boot:run
   ```

   API sur le port **8080** par défaut.

3. **Front-end** :

   ```bash
   cd frontend
   npm install
   ng serve
   ```

   L’URL de l’API en dev est définie dans `frontend/src/environments/environment.ts` (`http://localhost:8080/api`).

## Tests (back-end)

```bash
cd Backend
mvn test
```

## Déploiement cloud (GCP, etc.)

- Construire et pousser les images Docker du dossier `Backend` et `frontend` vers un registre (Artifact Registry, Docker Hub).  
- Déployer les conteneurs (ex. **Cloud Run** : un service pour le back-end, un pour le front avec la même idée de reverse proxy `/api` vers l’URL du back-end).  
- Adapter `app.cors.allowed-origins` dans les propriétés Spring à l’URL publique du front-end.  
- Utiliser des secrets pour `SPRING_DATASOURCE_*` et ne pas commiter de mots de passe.

## Structure du dépôt

- `Backend/` — API Spring Boot, `Dockerfile` Maven + JRE.  
- `frontend/` — SPA Angular, `Dockerfile` build Node + Nginx.  
- `docker-compose.yml` — orchestration **mysql**, **backend**, **frontend**.

## Licence / usage

Projet pédagogique — adapter la configuration et les secrets avant toute mise en production.

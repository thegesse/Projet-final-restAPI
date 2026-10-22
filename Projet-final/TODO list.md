# Liste des tâches

Travail restant d'après `05-Projet-Fil-Rouge-v3.pdf` (la configuration du projet, les repositories, les données initiales, la configuration OpenAPI et les débuts de classes fournis sont déjà en place).

## Sprint 1 — Auteurs

- [ ] Q12–Q14 : Créer les entités JPA `Auteur`, `Livre` et `Emprunt` avec les attributs et annotations indiqués dans le PDF.
- [ ] Q17 : Créer les DTO `AuteurRequest` et `AuteurResponse` avec les validations requises et l'ordre des champs JSON.
- [ ] Q18 : Compléter `AuteurMapper` avec les conversions entité/réponse et la conversion de liste ; conserver la méthode `versPage` fournie.
- [ ] Q19 : Implémenter les méthodes de `AuteurService`.
- [ ] Q20 : Implémenter les endpoints 1 à 5 des auteurs dans `AuteurController`.
- [ ] Q25 : Ajouter l'endpoint des livres d'un auteur (endpoint 6).
- [ ] Les données de Q16 sont déjà présentes dans `src/main/resources/data.sql`.

## Sprint 2 — Livres et emprunts

- [ ] Q21 : Créer les DTO `LivreRequest` et `LivreResponse` avec les validations et l'ordre des champs JSON.
- [ ] Q22 : Créer `LivreMapper` et compléter les conversions des livres.
- [ ] Q23 : Implémenter les méthodes de `LivreService`.
- [ ] Q24 : Implémenter les endpoints 7 à 14 des livres dans `LivreController`.
- [ ] Q26 : Créer les DTO `EmpruntRequest` et `EmpruntResponse` ; ne pas inclure l'adresse e-mail de l'emprunteur dans les réponses.
- [ ] Q27 : Créer `EmpruntMapper` avec les conversions demandées et le calcul des retards.
- [ ] Q28 : Implémenter les méthodes de `EmpruntService`.
- [ ] Q29 : Implémenter les endpoints 15 à 20 des emprunts dans `EmpruntController`.

## Sprint 3 — Documentation et client

- [ ] Q31 : Ajouter les tags Swagger, les résumés des opérations et la documentation des réponses aux 20 endpoints.
- [ ] Q32 : Créer la collection Postman contenant les 46 requêtes de recette ou préparer `RECETTE.pdf` avec les résultats de leur exécution.
- [ ] Q33 : Créer `client/ClientBibliotheque.java` avec Java `HttpClient` et réaliser le scénario demandé en 10 étapes.

## Livrables finaux

- [ ] Ajouter `CONCEPTION.pdf` avec les réponses aux questions Q1 à Q9.
- [ ] Exécuter les 46 requêtes Postman dans l'ordre sur une API fraîchement redémarrée et corriger les écarts.
- [ ] Vérifier que Swagger UI affiche les trois groupes de ressources et les 20 endpoints.
- [ ] Vérifier que le client Java affiche un bilan de 10/10 étapes conformes.
- [ ] Préparer l'archive de rendu sans le dossier `target/`.

# Note — Atelier 5 : API REST, tests Postman et documentation Swagger

## A. Endpoints

| Contrôleur | Verbe et Route | Service appelé | Succès | Erreurs |
|------------|----------------|----------------|--------|---------|
| ContratController | GET /contrats | IContratService.findAll() | 200 | — |
| ContratController | GET /contrats/{id} | IContratService.findById() | 200 | 404 |
| ContratController | POST /contrats | IContratService.create() | 201 | 400 |
| ContratController | DELETE /contrats/{id} | IContratService.deleteById() | 204 | 404 |
| AgenceController | GET /agences | IAgenceService.findAll() | 200 | — |
| AgenceController | GET /agences/{id} | IAgenceService.findById() | 200 | 404 |
| AgenceController | POST /agences | IAgenceService.create() | 201 | 400 |
| AgenceController | DELETE /agences/{id} | IAgenceService.deleteById() | 204 | 404 |
| ClientController | GET /clients | IClientService.findAll() | 200 | — |
| ClientController | GET /clients/{id} | IClientService.findById() | 200 | 404 |
| ClientController | POST /clients | IClientService.create() | 201 | 400 |
| ClientController | DELETE /clients/{id} | IClientService.deleteById() | 204 | 404 |
| VehiculeController | GET /vehicules | IVehiculeService.findAll() | 200 | — |
| VehiculeController | GET /vehicules/{id} | IVehiculeService.findById() | 200 | 404 |
| VehiculeController | POST /vehicules | IVehiculeService.create() | 201 | 400 |
| VehiculeController | DELETE /vehicules/{id} | IVehiculeService.deleteById() | 204 | 404 |
| EmployeController | GET /employes | IEmployeService.findAll() | 200 | — |
| EmployeController | GET /employes/{id} | IEmployeService.findById() | 200 | 404 |
| EmployeController | POST /employes | IEmployeService.create() | 201 | 400 |
| EmployeController | DELETE /employes/{id} | IEmployeService.deleteById() | 204 | 404 |
| ReservationController | GET /reservations | IReservationService.findAll() | 200 | — |
| ReservationController | GET /reservations/{id} | IReservationService.findById() | 200 | 404 |
| ReservationController | POST /reservations | IReservationService.create() | 201 | 400 |
| ReservationController | DELETE /reservations/{id} | IReservationService.deleteById() | 204 | 404 |
| EquipementController | GET /equipements | IEquipementService.findAll() | 200 | — |
| EquipementController | GET /equipements/{id} | IEquipementService.findById() | 200 | 404 |
| EquipementController | POST /equipements | IEquipementService.create() | 201 | 400 |
| EquipementController | DELETE /equipements/{id} | IEquipementService.deleteById() | 204 | 404 |
| MaintenanceController | GET /maintenances | IMaintenanceService.findAll() | 200 | — |
| MaintenanceController | GET /maintenances/{id} | IMaintenanceService.findById() | 200 | 404 |
| MaintenanceController | POST /maintenances | IMaintenanceService.create() | 201 | 400 |
| MaintenanceController | DELETE /maintenances/{id} | IMaintenanceService.deleteById() | 204 | 404 |
| PaiementController | GET /paiements | IPaiementService.findAll() | 200 | — |
| PaiementController | GET /paiements/{id} | IPaiementService.findById() | 200 | 404 |

## B. Annotations Jackson

### Contrat ↔ Paiement (Composition)
- **Annotation retenue** : `@JsonManagedReference("contrat-paiements")` sur `Contrat.paiements` et `@JsonBackReference("contrat-paiements")` sur `Paiement.contrat`
- **Justification** : Permet la création en cascade des paiements via le JSON du contrat sans boucle infinie lors de la sérialisation. Le parent (Contrat) sérialise ses enfants (Paiements), mais les enfants ne resérialisent pas le parent.

### Différence entre @JsonBackReference et @JsonIgnore
- **@JsonBackReference** : Marque la référence comme "enfant" dans une relation parent-enfant. L'enfant est ignoré lors de la sérialisation du parent, mais peut être désérialisé (utile pour POST avec corps imbriqué).
- **@JsonIgnore** : Ignore complètement la propriété dans les deux sens (sérialisation ET désérialisation). Utilisé pour les associations inverses non essentielles (ex: `Agence.vehicules`).

## C. Postman et Swagger

### Postman
- **Apport** : Tester les requêtes HTTP de manière interactive, vérifier les codes de statut, mémoriser des variables d'environnement (ex: `contratId`), rejouer les scénarios.
- **Exemple concret** : Création d'un contrat avec POST `/api/contrats`, récupération automatique de l'`idContrat` généré via un script After Response, puis consultation avec GET `/api/contrats/{{contratId}}`. Test de la cascade : création d'un contrat avec paiements imbriqués en une seule requête.

### Swagger UI
- **Apport** : Documenter l'API de manière synchronisée avec le code (pas de doc obsolète), permettre aux développeurs de découvrir et tester les endpoints sans Postman, visualiser les schémas JSON.
- **Exemple concret** : Visualisation des groupes « Contrats », « Paiements », « Agences » avec leurs descriptions (`@Tag`), résumés d'endpoints (`@Operation`) et codes de réponse documentés (`@ApiResponse`). Section Schemas montrant la structure complète des entités JPA.

## D. Anomalies SonarQube for IDE

| # | Règle | Explication | Correction apportée |
|---|-------|-------------|---------------------|
| 1 | **S6813** — Field injection is not recommended | L'injection par attribut (`@Autowired` sur un champ) rend le champ non `final` et cache les dépendances. | Remplacement par injection par constructeur avec `@RequiredArgsConstructor` et champ `private final`. |
| 2 | **S1128** — Unused imports should be removed | Des imports inutilisés alourdissent le code et nuisent à la lisibilité. | Suppression des imports inutiles via `Ctrl+Shift+O` (Optimize Imports). |
| 3 | **S112** — Generic exceptions should never be thrown | Lever `RuntimeException` au lieu d'une exception métier masque l'intention. | Utilisation de `ResourceNotFoundException` et `IllegalArgumentException` explicites. |

## E. Questions de compréhension

### 1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?
Le **DispatcherServlet** reçoit toutes les requêtes HTTP entrantes. Il consulte le **RequestMappingHandlerMapping** qui maintient une map associant les combinaisons (URL + verbe HTTP) aux méthodes des contrôleurs annotées avec `@GetMapping`, `@PostMapping`, etc. Une fois la méthode trouvée, il invoque le contrôleur, puis écrit la valeur retournée dans la réponse (JSON grâce à `@RestController`).

### 2. Pourquoi ContratController dépend-il de IContratService et non de ContratRepository ni de ContratServiceImpl ?
Pour respecter le **principe d'inversion des dépendances** (principe D de SOLID) : le contrôleur doit dépendre d'une abstraction (interface), pas d'une implémentation concrète. Cela permet de :
- Changer l'implémentation du service sans modifier le contrôleur.
- Faciliter les tests unitaires en injectant un mock de `IContratService`.
- Respecter la séparation des couches : le contrôleur ne doit jamais accéder directement au repository.

### 3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?
- **201 Created** : Convention REST indiquant qu'une nouvelle ressource a été créée avec succès. Le corps contient la représentation de la ressource créée (avec son ID généré).
- **204 No Content** : Indique que la suppression a réussi mais qu'il n'y a pas de corps à renvoyer (la ressource n'existe plus).
- **404 Not Found** : Erreur **client** (la ressource demandée n'existe pas). C'est une erreur attendue, gérée proprement par `ResourceNotFoundException` traduite en 404 par le `GlobalExceptionHandler`.
- **500 Internal Server Error** : Erreur **serveur** (bug dans le code, NullPointerException, etc.). Une ressource absente n'est pas un bug serveur, c'est une situation normale que le client doit gérer.

### 4. Pourquoi PaiementController n'expose-t-il que des GET ? Quelle réponse reçoit un POST /api/paiements ?
Parce que le cycle de vie d'un `Paiement` est **entièrement géré par le Contrat** via `CascadeType.ALL` et `orphanRemoval=true`. Créer/modifier/supprimer un paiement directement contournerait cette règle métier et pourrait laisser des orphelins en base ou créer des incohérences. Un POST sur `/api/paiements` retournerait **405 Method Not Allowed** car aucune méthode `@PostMapping` n'est définie dans `PaiementController`.

### 5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi Contrat.paiements utilise-t-il @JsonManagedReference alors que Agence.vehicules utilise @JsonIgnore ?
- **Risques de l'exposition directe** : Couplage fort entre l'API et la base de données, exposition de champs internes (ex: `hibernateLazyInitializer`), boucles infinies de sérialisation sur les associations bidirectionnelles, difficulté à faire évoluer l'API indépendamment du modèle JPA.
- **@JsonManagedReference sur Contrat.paiements** : Permet la **création en cascade** des paiements via le JSON du contrat (POST avec corps imbriqué). Le parent sérialise ses enfants, les enfants ne resérialisent pas le parent.
- **@JsonIgnore sur Agence.vehicules** : Évite la boucle infinie (Vehicule → Agence → Vehicules...) et allège considérablement la réponse JSON. Les véhicules d'une agence sont consultables via leur propre endpoint `/api/vehicules`.
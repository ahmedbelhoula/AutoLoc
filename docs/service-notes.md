# Note — Atelier 4 : IoC, DI et couche Service

## A. Modes d'injection comparés

| Critère | Injection par constructeur | Injection par attribut (@Autowired) |
|---------|---------------------------|-------------------------------------|
| Champ `final` possible ? | Oui, le champ est déclaré `final` | Non, le champ ne peut pas être `final` |
| Dépendance visible dans la signature ? | Oui, visible dans le constructeur | Non, cachée dans le corps de la classe |
| Utilisable avec `new` hors conteneur ? | Oui, en passant la dépendance en paramètre | Non, la dépendance serait `null` |
| Signalé par SonarQube (S6813) ? | Non, c'est la bonne pratique | Oui, règle S6813 déclenchée |
| Objet toujours dans un état valide ? | Oui, toutes les dépendances sont fournies à la création | Non, risque d'objet partiellement construit |

**Conclusion** : L'injection par constructeur est la bonne pratique retenue pour AutoLoc.

---

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---------|----------------------|------|---------------|
| ContratServiceImpl | IContratRepository | Constructeur | Le service dépend d'une abstraction (interface) pour rester découplé de l'implémentation Spring Data. |
| AgenceServiceImpl | IAgenceRepository | Constructeur | Injection par constructeur avec champ `final`, convention AutoLoc. |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Injection par constructeur avec champ `final`, convention AutoLoc. |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Injection par constructeur avec champ `final`, convention AutoLoc. |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Injection par constructeur avec champ `final`, convention AutoLoc. |
| ClientServiceImpl | IClientRepository | Constructeur | Injection par constructeur avec champ `final`, convention AutoLoc. |
| ReservationServiceImpl | IReservationRepository | Constructeur | Injection par constructeur avec champ `final`, convention AutoLoc. |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Lecture seule : le cycle de vie des paiements est géré par ContratServiceImpl (CascadeType.ALL). |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Injection par constructeur avec champ `final`, convention AutoLoc. |

---

## C. Messages d'erreur du conteneur

### Message 1 : `NoSuchBeanDefinitionException`

**Description** : `Parameter 0 of constructor in ContratController required a bean of type 'IContratService' that could not be found.`

**Causes probables** :
1. La classe `ContratServiceImpl` n'est pas annotée `@Service` (donc non détectée par le scan).
2. La classe est placée hors du package `tn.esprit.autoloc` (hors du scan).

**Correction** : Ajouter `@Service` sur `ContratServiceImpl` et vérifier qu'elle est dans le package `tn.esprit.autoloc.service`.

### Message 2 : `NoUniqueBeanDefinitionException`

**Description** : `required a single bean, but 2 were found: emailNotificateur, smsNotificateur`

**Deux corrections possibles** :
1. **`@Primary`** : Placé sur la classe du bean à privilégier (ex. `@Primary` sur `EmailNotificateur`). Désigne le candidat par défaut.
2. **`@Qualifier("nomDuBean")`** : Placé sur le paramètre du constructeur du consommateur (ex. `@Qualifier("smsNotificateur")`). Permet un choix explicite.

### Message 3 : Dépendance circulaire

**Description** : `clientServiceImpl ↔ reservationServiceImpl`

**Pourquoi l'injection par constructeur rend ce cycle impossible ?**
Avec l'injection par constructeur, Spring doit créer `ClientServiceImpl` avant `ReservationServiceImpl` (car ce dernier en a besoin), et inversement. C'est un deadlock à l'instanciation.

**Piste de conception pour supprimer le cycle** :
Extraire la responsabilité commune dans un troisième service (ex. `ReservationValidationService`), ou utiliser un événement Spring (`@EventListener`) pour découpler les deux services.

---

## D. Anomalies SonarQube for IDE

| # | Règle | Explication | Correction apportée |
|---|-------|-------------|---------------------|
| 1 | **S6813** — Field injection is not recommended | L'injection par attribut (`@Autowired` sur un champ) rend le champ non `final` et cache les dépendances. | Remplacement par injection par constructeur avec `@RequiredArgsConstructor` et champ `final`. |
| 2 | **S112** — Generic exceptions should never be thrown | Lever `RuntimeException` au lieu d'une exception métier masque l'intention et rend le débogage difficile. | Remplacement par `ResourceNotFoundException` (exception métier explicite). |
| 3 | **S1128** — Unused imports should be removed | Des imports inutilisés alourdissent le code et nuisent à la lisibilité. | Suppression des imports inutiles via `Ctrl+Shift+O` (Optimize Imports). |

---

## E. Questions de compréhension

### 1. Où est le `new` de `ContratServiceImpl` et qui l'exécute ?

Il n'y a **aucun `new ContratServiceImpl(...)`** dans le code. C'est le **conteneur Spring IoC** (via `ApplicationContext`) qui instancie le bean au démarrage, en appelant le constructeur généré par Lombok (`@RequiredArgsConstructor`) et en lui injectant le proxy `IContratRepository`.

### 2. Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?

Pour respecter le **principe de dépendance aux abstractions** (principe D de SOLID). Le contrôleur dépend d'une interface, ce qui permet de changer l'implémentation (ex. mock pour les tests) sans modifier le contrôleur.

### 3. Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.

Un singleton est **partagé par tous les threads** (requêtes HTTP concurrentes). S'il porte un état modifiable (ex. un champ `contratCourant`), deux requêtes simultanées pourraient écraser mutuellement cet état, provoquant des **incohérences de données** (race condition).

### 4. Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` ?

Pour deux raisons :
- **Sécurité** : éviter d'écraser des colonnes non modifiables (ex. l'identifiant).
- **Cascade** : avec `orphanRemoval=true`, passer directement l'objet reçu à `save` pourrait vider la collection de paiements si elle n'est pas renseignée dans la requête.

### 5. Différences entre annotations et cas du Paiement

- **`@Component` vs `@Bean`** : `@Component` est une annotation de classe détectée par le scan. `@Bean` est utilisé dans une classe `@Configuration` pour déclarer explicitement un bean (utile pour les classes externes qu'on ne peut pas annoter).
- **`@Primary` vs `@Qualifier`** : `@Primary` désigne un bean par défaut au niveau de la classe. `@Qualifier` permet un choix explicite au niveau du point d'injection.
- **Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?** Parce que le cycle de vie d'un `Paiement` est **entièrement géré par le `Contrat`** (via `CascadeType.ALL` et `orphanRemoval=true`). Créer/modifier/supprimer un paiement directement contournerait cette règle métier et pourrait laisser des orphelins en base.
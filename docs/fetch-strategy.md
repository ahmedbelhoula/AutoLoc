# Stratégies Fetch et Cascade - AutoLoc

## Justification des choix d'associations JPA

| Association | Fetch | Cascade | Justification |
|-------------|-------|---------|---------------|
| Contrat → Paiement | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat (composition). Supprimer le contrat supprime ses paiements. Retirer un paiement de la collection le supprime en base. |
| Agence → Vehicule | LAZY | Aucune | Un véhicule survit à la suppression de son agence (il peut être transféré). |
| Agence → Employe | LAZY | Aucune | Un employé peut changer d'agence sans être supprimé. |
| Vehicule ↔ Equipement | LAZY | Aucune | Équipements partagés entre plusieurs véhicules. La suppression d'un véhicule ne doit pas supprimer les équipements. |
| Client → Reservation | LAZY | PERSIST | Une réservation dépend d'un client existant. PERSIST permet de créer une réservation en même temps que le client. |
| Reservation → Vehicule | LAZY | Aucune | Un véhicule peut avoir plusieurs réservations à des dates différentes. |
| Reservation ↔ Contrat | LAZY | ALL côté Reservation | Un contrat dépend entièrement d'une réservation. Supprimer la réservation supprime son contrat. |
| Vehicule → Maintenance | LAZY | PERSIST | Une maintenance dépend d'un véhicule. PERSIST permet d'enregistrer une maintenance en même temps que le véhicule. |

## Pourquoi LAZY partout ?

Selon les recommandations du cours :
- **@ManyToOne** : par défaut EAGER, mais forcer LAZY pour éviter de charger toute la chaîne d'objets liés.
- **@OneToOne** : par défaut EAGER, mais forcer LAZY sauf besoin systématique.
- **@OneToMany / @ManyToMany** : déjà LAZY par défaut, à conserver.

LAZY évite les requêtes SQL inutiles et améliore les performances. Les objets liés ne sont chargés que si on y accède explicitement.
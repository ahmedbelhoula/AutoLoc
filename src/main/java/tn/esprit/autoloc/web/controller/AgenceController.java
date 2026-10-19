package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/agences") // Pas de "/api" ici, il est géré par application.properties
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion des agences de location de véhicules")
public class AgenceController {

    private final IAgenceService agenceService;

    @GetMapping
    @Operation(summary = "Lister toutes les agences", description = "Retourne la liste complète des agences")
    @ApiResponse(responseCode = "200", description = "Liste des agences récupérée avec succès")
    public ResponseEntity<List<Agence>> getAll() {
        return ResponseEntity.ok(agenceService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une agence", description = "Retourne une agence par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agence trouvée"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    public ResponseEntity<Agence> getById(
            @Parameter(description = "Identifiant de l'agence à consulter") @PathVariable Long id) {
        return ResponseEntity.ok(agenceService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer une agence", description = "Crée une nouvelle agence")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agence créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (ex: nom vide)")
    })
    public ResponseEntity<Agence> create(
            @Parameter(description = "Données de l'agence à créer") @RequestBody Agence agence) {
        Agence createdAgence = agenceService.create(agence);
        return new ResponseEntity<>(createdAgence, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une agence", description = "Supprime une agence par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agence supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Agence introuvable")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de l'agence à supprimer") @PathVariable Long id) {
        agenceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
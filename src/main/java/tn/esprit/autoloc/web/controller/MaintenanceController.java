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
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion des maintenances des véhicules")
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    @GetMapping
    @Operation(summary = "Lister toutes les maintenances", description = "Retourne la liste complète des maintenances")
    @ApiResponse(responseCode = "200", description = "Liste des maintenances récupérée avec succès")
    public ResponseEntity<List<Maintenance>> getAll() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une maintenance", description = "Retourne une maintenance par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance trouvée"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public ResponseEntity<Maintenance> getById(
            @Parameter(description = "Identifiant de la maintenance à consulter") @PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer une maintenance", description = "Crée une nouvelle maintenance")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Maintenance créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (coût négatif ou dates incohérentes)")
    })
    public ResponseEntity<Maintenance> create(
            @Parameter(description = "Données de la maintenance à créer") @RequestBody Maintenance maintenance) {
        Maintenance createdMaintenance = maintenanceService.create(maintenance);
        return new ResponseEntity<>(createdMaintenance, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une maintenance", description = "Supprime une maintenance par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Maintenance supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Maintenance introuvable")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de la maintenance à supprimer") @PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
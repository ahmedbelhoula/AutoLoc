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
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "Gestion des véhicules de location")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @GetMapping
    @Operation(summary = "Lister tous les véhicules", description = "Retourne la liste complète des véhicules disponibles")
    @ApiResponse(responseCode = "200", description = "Liste des véhicules récupérée avec succès")
    public ResponseEntity<List<Vehicule>> getAll() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un véhicule", description = "Retourne un véhicule par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public ResponseEntity<Vehicule> getById(
            @Parameter(description = "Identifiant du véhicule à consulter") @PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un véhicule", description = "Crée un nouveau véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (tarif négatif, immatriculation vide)")
    })
    public ResponseEntity<Vehicule> create(
            @Parameter(description = "Données du véhicule à créer") @RequestBody Vehicule vehicule) {
        Vehicule createdVehicule = vehiculeService.create(vehicule);
        return new ResponseEntity<>(createdVehicule, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un véhicule", description = "Supprime un véhicule par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant du véhicule à supprimer") @PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
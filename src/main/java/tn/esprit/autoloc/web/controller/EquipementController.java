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
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Équipements", description = "Gestion des équipements des véhicules")
public class EquipementController {

    private final IEquipementService equipementService;

    @GetMapping
    @Operation(summary = "Lister tous les équipements", description = "Retourne la liste complète des équipements")
    @ApiResponse(responseCode = "200", description = "Liste des équipements récupérée avec succès")
    public ResponseEntity<List<Equipement>> getAll() {
        return ResponseEntity.ok(equipementService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un équipement", description = "Retourne un équipement par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<Equipement> getById(
            @Parameter(description = "Identifiant de l'équipement à consulter") @PathVariable Long id) {
        return ResponseEntity.ok(equipementService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un équipement", description = "Crée un nouvel équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Équipement créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (libellé vide)")
    })
    public ResponseEntity<Equipement> create(
            @Parameter(description = "Données de l'équipement à créer") @RequestBody Equipement equipement) {
        Equipement createdEquipement = equipementService.create(equipement);
        return new ResponseEntity<>(createdEquipement, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un équipement", description = "Supprime un équipement par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Équipement supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de l'équipement à supprimer") @PathVariable Long id) {
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
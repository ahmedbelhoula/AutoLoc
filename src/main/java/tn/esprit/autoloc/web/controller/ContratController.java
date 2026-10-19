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
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/contrats") // ← IMPORTANT : Pas de "/api" ici, car il est déjà dans application.properties
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location de véhicules")
public class ContratController {

    private final IContratService contratService;

    @GetMapping
    @Operation(summary = "Lister tous les contrats", description = "Retourne la liste complète des contrats")
    @ApiResponse(responseCode = "200", description = "Liste des contrats récupérée avec succès")
    public ResponseEntity<List<Contrat>> getAll() {
        return ResponseEntity.ok(contratService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un contrat", description = "Retourne un contrat par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Contrat> getById(
            @Parameter(description = "Identifiant du contrat à consulter") @PathVariable Long id) {
        return ResponseEntity.ok(contratService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un contrat", description = "Crée un nouveau contrat de location")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrat créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (ex: identifiant déjà présent)")
    })
    public ResponseEntity<Contrat> add(
            @Parameter(description = "Données du contrat à créer") @RequestBody Contrat contrat) {
        Contrat createdContrat = contratService.create(contrat);
        return new ResponseEntity<>(createdContrat, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat", description = "Supprime un contrat et ses paiements associés (cascade)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant du contrat à supprimer") @PathVariable Long id) {
        contratService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
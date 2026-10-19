package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;

import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "Consultation des paiements (gérés en cascade par les Contrats)")
public class PaiementController {

    private final IPaiementService paiementService;

    @GetMapping
    @Operation(summary = "Lister tous les paiements", description = "Retourne la liste de tous les paiements existants")
    @ApiResponse(responseCode = "200", description = "Liste des paiements récupérée avec succès")
    public ResponseEntity<List<Paiement>> getAll() {
        return ResponseEntity.ok(paiementService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un paiement", description = "Retourne un paiement par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement introuvable")
    })
    public ResponseEntity<Paiement> getById(
            @Parameter(description = "Identifiant du paiement à consulter") @PathVariable Long id) {
        return ResponseEntity.ok(paiementService.findById(id));
    }

    // Note : Pas de @PostMapping, @PutMapping ou @DeleteMapping ici.
    // Si on tente un POST sur /api/paiements, Spring renverra automatiquement un "405 Method Not Allowed",
    // ce qui est le comportement attendu selon le PDF.
}
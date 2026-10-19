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
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employés", description = "Gestion des employés des agences")
public class EmployeController {

    private final IEmployeService employeService;

    @GetMapping
    @Operation(summary = "Lister tous les employés", description = "Retourne la liste complète des employés")
    @ApiResponse(responseCode = "200", description = "Liste des employés récupérée avec succès")
    public ResponseEntity<List<Employe>> getAll() {
        return ResponseEntity.ok(employeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un employé", description = "Retourne un employé par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Employe> getById(
            @Parameter(description = "Identifiant de l'employé à consulter") @PathVariable Long id) {
        return ResponseEntity.ok(employeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un employé", description = "Crée un nouvel employé")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employé créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides (nom ou prénom vide)")
    })
    public ResponseEntity<Employe> create(
            @Parameter(description = "Données de l'employé à créer") @RequestBody Employe employe) {
        Employe createdEmploye = employeService.create(employe);
        return new ResponseEntity<>(createdEmploye, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un employé", description = "Supprime un employé par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employé supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identifiant de l'employé à supprimer") @PathVariable Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
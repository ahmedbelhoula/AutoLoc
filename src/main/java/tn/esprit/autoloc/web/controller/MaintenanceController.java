package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final IMaintenanceService maintenanceService;

    // 1. ADD (Ajouter une maintenance)
    @PostMapping
    public ResponseEntity<Maintenance> add(@RequestBody Maintenance maintenance) {
        Maintenance createdMaintenance = maintenanceService.create(maintenance);
        return new ResponseEntity<>(createdMaintenance, HttpStatus.CREATED);
    }

    // 2. GET ALL (Récupérer toutes les maintenances)
    @GetMapping
    public ResponseEntity<List<Maintenance>> getAll() {
        return ResponseEntity.ok(maintenanceService.findAll());
    }

    // 3. GET BY ID (Récupérer une maintenance par son ID)
    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id));
    }

    // 4. DELETE (Supprimer une maintenance)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
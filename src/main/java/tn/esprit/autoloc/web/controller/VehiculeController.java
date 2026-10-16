package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    // 1. ADD (Ajouter un véhicule)
    @PostMapping
    public ResponseEntity<Vehicule> add(@RequestBody Vehicule vehicule) {
        Vehicule createdVehicule = vehiculeService.create(vehicule);
        return new ResponseEntity<>(createdVehicule, HttpStatus.CREATED);
    }

    // 2. GET ALL (Récupérer tous les véhicules)
    @GetMapping
    public ResponseEntity<List<Vehicule>> getAll() {
        return ResponseEntity.ok(vehiculeService.findAll());
    }

    // 3. GET BY ID (Récupérer un véhicule par son ID)
    @GetMapping("/{id}")
    public ResponseEntity<Vehicule> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.findById(id));
    }

    // 4. DELETE (Supprimer un véhicule)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehiculeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
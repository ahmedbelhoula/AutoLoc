package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/api/equipements")
@RequiredArgsConstructor
public class EquipementController {

    private final IEquipementService equipementService;

    // 1. ADD (Ajouter un équipement)
    @PostMapping
    public ResponseEntity<Equipement> add(@RequestBody Equipement equipement) {
        Equipement createdEquipement = equipementService.create(equipement);
        return new ResponseEntity<>(createdEquipement, HttpStatus.CREATED);
    }

    // 2. GET ALL (Récupérer tous les équipements)
    @GetMapping
    public ResponseEntity<List<Equipement>> getAll() {
        return ResponseEntity.ok(equipementService.findAll());
    }

    // 3. GET BY ID (Récupérer un équipement par son ID)
    @GetMapping("/{id}")
    public ResponseEntity<Equipement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(equipementService.findById(id));
    }

    // 4. DELETE (Supprimer un équipement)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
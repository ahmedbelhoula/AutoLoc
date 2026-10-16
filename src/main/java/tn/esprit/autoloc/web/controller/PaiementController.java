package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final IPaiementService paiementService;

    // 1. GET ALL (Récupérer tous les paiements) - Lecture seule
    @GetMapping
    public ResponseEntity<List<Paiement>> getAll() {
        return ResponseEntity.ok(paiementService.findAll());
    }

    // 2. GET BY ID (Récupérer un paiement par son ID) - Lecture seule
    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.findById(id));
    }

    // Pas de POST, PUT, DELETE :
    // Les paiements sont créés/modifiés/supprimés via le Contrat (CascadeType.ALL)
}
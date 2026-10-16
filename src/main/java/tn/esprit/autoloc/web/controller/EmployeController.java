package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.service.IEmployeService;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final IEmployeService employeService;

    // 1. ADD (Ajouter un employé)
    @PostMapping
    public ResponseEntity<Employe> add(@RequestBody Employe employe) {
        Employe createdEmploye = employeService.create(employe);
        return new ResponseEntity<>(createdEmploye, HttpStatus.CREATED);
    }

    // 2. GET ALL (Récupérer tous les employés)
    @GetMapping
    public ResponseEntity<List<Employe>> getAll() {
        return ResponseEntity.ok(employeService.findAll());
    }

    // 3. GET BY ID (Récupérer un employé par son ID)
    @GetMapping("/{id}")
    public ResponseEntity<Employe> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeService.findById(id));
    }

    // 4. DELETE (Supprimer un employé)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.service.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;

    // 1. ADD (Ajouter une réservation)
    @PostMapping
    public ResponseEntity<Reservation> add(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.create(reservation);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    // 2. GET ALL (Récupérer toutes les réservations)
    @GetMapping
    public ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    // 3. GET BY ID (Récupérer une réservation par son ID)
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    // 4. DELETE (Supprimer une réservation)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
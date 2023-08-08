package com.example.hospedatemporada.controller;

import com.example.hospedatemporada.domain.Reserva;
import com.example.hospedatemporada.dto.ReservaDto;
import com.example.hospedatemporada.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> create(@RequestBody ReservaDto reserva) {
        Reserva obj = reservaService.fromDto(reserva);
        obj = reservaService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> findAll() {
        List<Reserva> list = reservaService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Integer id) {
        Reserva reserva = reservaService.findById(id);
        return ResponseEntity.ok().body(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> update(@RequestBody ReservaDto dto, @PathVariable Integer id) {
        Reserva obj = reservaService.fromDto(dto);
        obj.setId(id);
        reservaService.update(obj);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Reserva> cancel(@PathVariable Integer id) {
        Reserva obj = reservaService.cancel(id);
        return ResponseEntity.ok().body(obj);
    }

    @DeleteMapping
    public void deleteAll(){
        reservaService.deleteAll();
    }

}

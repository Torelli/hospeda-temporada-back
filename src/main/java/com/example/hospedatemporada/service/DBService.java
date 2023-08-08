package com.example.hospedatemporada.service;

import com.example.hospedatemporada.domain.Reserva;
import com.example.hospedatemporada.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void instanciaBancoDeDados() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Reserva r1 = new Reserva(null, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");
        Reserva r2 = new Reserva(null, "Ciclano da Silva", sdf.parse("2023-09-01"), sdf.parse("2023-09-05"), 2, "PENDENTE");
        reservaRepository.saveAll(Arrays.asList(r1,r2));
    }
}

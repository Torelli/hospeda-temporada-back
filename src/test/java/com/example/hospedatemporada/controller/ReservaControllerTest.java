package com.example.hospedatemporada.controller;

import com.example.hospedatemporada.domain.Reserva;
import com.example.hospedatemporada.dto.ReservaDto;
import com.example.hospedatemporada.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    @Test
    public void testFindByIdExistente() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer reservaId = 1;
        Reserva reservaEncontrada = new Reserva(reservaId, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");

        when(reservaService.findById(reservaId)).thenReturn(reservaEncontrada);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeHospede").value("Fulano de tal"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantidadePessoas").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CONFIRMADA"));
    }

    @Test
    public void testFindAll() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Reserva r1 = new Reserva(null, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");
        Reserva r2 = new Reserva(null, "Ciclano da Silva", sdf.parse("2023-09-01"), sdf.parse("2023-09-05"), 2, "PENDENTE");
        List<Reserva> list = new ArrayList<>();
        list.add(r1);
        list.add(r2);

        when(reservaService.findAll()).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCancelarReserva() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Reserva r1 = new Reserva(1, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CANCELADA");

        when(reservaService.cancel(1)).thenReturn(r1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CANCELADA"));
    }

}

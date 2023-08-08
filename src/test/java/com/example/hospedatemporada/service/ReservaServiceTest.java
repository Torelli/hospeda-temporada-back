package com.example.hospedatemporada.service;

import com.example.hospedatemporada.domain.Reserva;
import com.example.hospedatemporada.dto.ReservaDto;
import com.example.hospedatemporada.repository.ReservaRepository;
import com.example.hospedatemporada.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReservaServiceTest {
    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    public void testInsertReserva() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Reserva reserva = new Reserva(1, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva novaReserva = reservaService.create(reserva);

        assertNotNull(novaReserva);
        assertEquals(reserva.getNomeHospede(), novaReserva.getNomeHospede());
        assertEquals(reserva.getDataInicio(), novaReserva.getDataInicio());
        assertEquals(reserva.getDataFim(), novaReserva.getDataFim());
        assertEquals(reserva.getQuantidadePessoas(), novaReserva.getQuantidadePessoas());
        assertEquals(reserva.getStatus(), novaReserva.getStatus());
        assertNull(novaReserva.getId());

        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    public void testFindAll() throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Reserva r1 = new Reserva(null, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");
        Reserva r2 = new Reserva(null, "Ciclano da Silva", sdf.parse("2023-09-01"), sdf.parse("2023-09-05"), 2, "PENDENTE");
        List<Reserva> list = new ArrayList<>();
        list.add(r1);
        list.add(r2);

        when(reservaRepository.findAll()).thenReturn(list);

        List<Reserva> resultado = reservaService.findAll();

        assertNotNull(resultado);
        assertEquals(resultado.size(), list.size());
        assertEquals(resultado, list);

        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    public void testFindReservaByIdExistente() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer reservaId = 1;
        Reserva reserva = new Reserva(reservaId, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        Reserva reservaEncontrada = reservaService.findById(reservaId);

        assertNotNull(reservaEncontrada);
        assertEquals(reserva.getNomeHospede(), reservaEncontrada.getNomeHospede());
        assertEquals(reserva.getDataInicio(), reservaEncontrada.getDataInicio());
        assertEquals(reserva.getDataFim(), reservaEncontrada.getDataFim());
        assertEquals(reserva.getQuantidadePessoas(), reservaEncontrada.getQuantidadePessoas());
        assertEquals(reserva.getStatus(), reservaEncontrada.getStatus());

        verify(reservaRepository, times(1)).findById(reservaId);

    }

    @Test
    public void testFindReservaByIdNaoExistente() {
        Integer reservaId = 99;

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> reservaService.findById(reservaId));
        verify(reservaRepository, times(1)).findById(reservaId);
    }

    @Test
    public void testAtualizarReserva() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer reservaId = 1;
        Reserva reservaExistente = new Reserva(reservaId, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");
        Reserva reservaAtualizada = new Reserva(reservaId, "Fulano da Silva", sdf.parse("2023-08-12"), sdf.parse("2023-08-18"), 3, "PENDENTE");

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaAtualizada);

        Reserva resultado = reservaService.update(reservaAtualizada);

        assertNotNull(resultado);
        assertEquals(resultado.getNomeHospede(), reservaAtualizada.getNomeHospede());
        assertEquals(resultado.getDataInicio(), reservaAtualizada.getDataInicio());
        assertEquals(resultado.getDataFim(), reservaAtualizada.getDataFim());
        assertEquals(resultado.getQuantidadePessoas(), reservaAtualizada.getQuantidadePessoas());
        assertEquals(resultado.getStatus(), reservaAtualizada.getStatus());

        verify(reservaRepository, times(1)).findById(reservaId);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    public void testCancelarReserva() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Integer reservaId = 1;
        Reserva reservaExistente = new Reserva(reservaId, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");
        Reserva reservaCancelada = new Reserva(reservaId, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CANCELADA");

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reservaExistente));
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaCancelada);

        Reserva resultado = reservaService.cancel(reservaId);

        assertNotNull(resultado);
        assertEquals(resultado.getStatus(), reservaCancelada.getStatus());

        verify(reservaRepository, times(2)).findById(reservaId);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    public void testFromDto() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ReservaDto dto = new ReservaDto(1, "Fulano de tal", sdf.parse("2023-08-10"), sdf.parse("2023-08-15"), 4, "CONFIRMADA");

        Reserva resultado = reservaService.fromDto(dto);

        assertNotNull(resultado);
        assertEquals(dto.getNomeHospede(), resultado.getNomeHospede());
        assertEquals(dto.getDataInicio(), resultado.getDataInicio());
        assertEquals(dto.getDataFim(), resultado.getDataFim());
        assertEquals(dto.getQuantidadePessoas(), resultado.getQuantidadePessoas());
        assertEquals(dto.getStatus(), resultado.getStatus());
    }
}

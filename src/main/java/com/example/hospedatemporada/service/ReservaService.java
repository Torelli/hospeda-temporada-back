package com.example.hospedatemporada.service;

import com.example.hospedatemporada.domain.Reserva;
import com.example.hospedatemporada.dto.ReservaDto;
import com.example.hospedatemporada.repository.ReservaRepository;
import com.example.hospedatemporada.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva create(Reserva obj) {
        obj.setId(null);
        obj.setStatus("CONFIRMADA");
        return reservaRepository.save(obj);
    }
    public List<Reserva> findAll() {
        List<Reserva> list = reservaRepository.findAll();
        return list;
    }
    public Reserva findById(Integer id) {
        Optional<Reserva> obj = reservaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado: " + id + ", Tipo: " + Reserva.class.getName()));
    }

    public Reserva update(Reserva obj) {
        Reserva reservaAtualizada = findById(obj.getId());
        updateData(reservaAtualizada, obj);
        return reservaRepository.save(reservaAtualizada);
    }

    private void updateData(Reserva nova, Reserva antiga) {
        nova.setNomeHospede(antiga.getNomeHospede());
        nova.setDataInicio(antiga.getDataInicio());
        nova.setDataFim(antiga.getDataFim());
        nova.setQuantidadePessoas(antiga.getQuantidadePessoas());
        nova.setStatus(antiga.getStatus());
    }

    public Reserva cancel(Integer id) {
        Reserva reservaCancelada = findById(id);
        reservaCancelada.setStatus("CANCELADA");
        update(reservaCancelada);
        return reservaCancelada;
    }

    public void deleteAll() {
        reservaRepository.deleteAll();
    }

    public Reserva fromDto(ReservaDto objDto) {
        return new Reserva(objDto.getId(), objDto.getNomeHospede(), objDto.getDataInicio(), objDto.getDataFim(), objDto.getQuantidadePessoas(), objDto.getStatus());
    }
}

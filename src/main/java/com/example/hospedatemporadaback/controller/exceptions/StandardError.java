package com.example.hospedatemporadaback.controller.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StandardError implements Serializable {

    private Integer status;

    private Long horario;

    private String mensagem;

}

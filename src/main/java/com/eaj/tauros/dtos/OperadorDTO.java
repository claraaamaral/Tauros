package com.eaj.tauros.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperadorDTO {
    private String nome;
    private Integer qtdChamados;
}

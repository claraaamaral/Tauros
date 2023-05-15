package com.eaj.tauros.dtos;

import com.eaj.tauros.models.Problema;
import com.eaj.tauros.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RelatorioSetorDTO {
    private List<OperadorDTO> operadores;
    private List<Problema> problemas;
}

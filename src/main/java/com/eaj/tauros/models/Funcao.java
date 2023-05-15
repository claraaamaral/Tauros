package com.eaj.tauros.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "funcoes")
public class Funcao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "funcao_id")
    private int id;
    @Column(name = "funcao")
    private String descricao;
}

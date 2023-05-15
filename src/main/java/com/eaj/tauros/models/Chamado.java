package com.eaj.tauros.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "chamado_id")
    private Long id;

    private String status;

    private String descricao;

    private String solucao;

    private String descricaoProblema;

    private String data;

    private Date delete;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "problema_id")
    Problema problema;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "setor_id")
    Setor setor;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario_abertura_id")
    User usuarioAbertura;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario_atendimento_id")
    User usuarioAtendimento;


}

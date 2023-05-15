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
public class Problema {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "problema_id")
    private Long id;

    private String nome;

    private String descricao;

    private Date delete;

    @OneToOne
    Setor setor;
}

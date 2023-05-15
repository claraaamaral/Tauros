package com.eaj.tauros.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Size(min=2, max=30)
    private String userName;

    @NotNull
    @Size(min=2, max=30)
    private String email;

    @NotNull
    private String nome;

    @NotBlank
    private String senha;

    @NotBlank
    private String confirmacaoSenha;

    private Boolean ativo;

    private LocalDate delete;

    @OneToOne
    @JoinColumn(name = "funcao_id")
    private Funcao funcao;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_setor")
    private Setor setor;

}

package br.com.pcoe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profissional")
public class Profissional implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String nome;
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "profissionais_especialidades",
            joinColumns = @JoinColumn(name = "profissional_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidade_id"))
    private List<Especialidade> especialidade;

    private boolean ativo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FolhaPagamento> folhaPagamento;
}

package br.com.pcoe.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private String cpf;

    @JsonBackReference
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "profissionais_especialidades",
            joinColumns = @JoinColumn(name = "profissional_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidade_id"))
    private List<Especialidade> especialidade;

    private boolean ativo;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    private List<FolhaPagamento> folhaPagamento;
}

package br.com.pcoe.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "caixas_movimentos_especialidades")
public class CaixaMovimentoEspecialidade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinTable(name = "especialidade_id")
    @JsonManagedReference
    private Especialidade especialidade;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "caixa_id")
    @JsonBackReference
    private Caixa caixa;

    private BigDecimal valorMovimento;
}

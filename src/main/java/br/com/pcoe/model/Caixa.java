package br.com.pcoe.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "caixas")
public class Caixa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CaixaMovimento> caixaMovimento;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CaixaMovimentoEspecialidade> caixaMovimentoEspecialidade;

    private LocalDate data;

    private boolean aberto;

    private BigDecimal valorTotal;

    private BigDecimal valorRetirada;

    private BigDecimal valorQuebra;

    private String observacao;
}

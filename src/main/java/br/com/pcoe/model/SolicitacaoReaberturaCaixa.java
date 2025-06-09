package br.com.pcoe.model;

import br.com.pcoe.enums.StatusSolicitacao;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitacoes_reaberturas_caixas")
public class SolicitacaoReaberturaCaixa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Caixa caixa;

    private String motivo;

    private LocalDate dataSolicitacao;

    private LocalDate dataResposta;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusReabertura;

    @JsonManagedReference("solicitante")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Usuario solicitante;

    @JsonManagedReference("movimentador")
    @ManyToOne(cascade = CascadeType.MERGE)
    private Usuario administrador;
}

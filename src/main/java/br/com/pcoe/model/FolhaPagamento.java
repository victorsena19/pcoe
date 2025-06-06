package br.com.pcoe.model;


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
@Table(name = "folhas_pagamentos")
public class FolhaPagamento implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDate dataLancamentoFolhaPagamento;
    private LocalDate dataInicialPrestacaoSevico;
    private LocalDate dataFinalPrestacaoSevico;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private Profissional profissional;

    private BigDecimal valorBase;
    private BigDecimal valorAcrescimo;
    private BigDecimal valorDesconto;
    private BigDecimal valorBonus;
    private BigDecimal valorLiquido;

}

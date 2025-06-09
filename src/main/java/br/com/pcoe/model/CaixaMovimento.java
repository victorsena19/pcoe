package br.com.pcoe.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "caixa_movimento")
public class CaixaMovimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "forma_pagamento")
    private FormaPagamento formaPagamento;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "caixa_id")
    @JsonBackReference
    private Caixa caixa;

    private BigDecimal valorMovimento;
}

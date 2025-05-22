package br.com.pcoe.repository;

import br.com.pcoe.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, UUID> {
    boolean existsByNomeIgnoreCase(String nome);
}

package br.com.pcoe.repository;

import br.com.pcoe.model.CaixaMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaixaMovimentoRepository extends JpaRepository<CaixaMovimento, UUID> {
    boolean existsByFormaPagamentoId(UUID id);
}

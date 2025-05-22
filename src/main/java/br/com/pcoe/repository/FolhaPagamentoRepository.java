package br.com.pcoe.repository;

import br.com.pcoe.model.FolhaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FolhaPagamentoRepository extends JpaRepository<FolhaPagamento, UUID> {

    boolean existsByProfissionalId(UUID id);

}

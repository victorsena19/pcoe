package br.com.pcoe.repository;

import br.com.pcoe.enums.StatusSolicitacao;
import br.com.pcoe.model.SolicitacaoReaberturaCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitacaoReaberturaCaixaRepository extends JpaRepository<SolicitacaoReaberturaCaixa, UUID> {
    boolean existsByCaixaId(UUID id);

    List<SolicitacaoReaberturaCaixa> findByStatusReabertura(StatusSolicitacao statusReabertura);
    List<SolicitacaoReaberturaCaixa> findByStatusReaberturaAndSolicitanteId(StatusSolicitacao statusReabertura, UUID solicitante);
}

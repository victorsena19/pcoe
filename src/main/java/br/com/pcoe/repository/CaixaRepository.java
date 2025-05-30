package br.com.pcoe.repository;

import br.com.pcoe.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CaixaRepository extends JpaRepository<Caixa, UUID> {

    List<Caixa> findByUsuarioId(UUID id);

    Caixa findByUsuarioIdAndData(UUID id, LocalDate data);

    boolean existsByUsuarioId(UUID id);

    List<Caixa> findByData(LocalDate data);

    @Query("SELECT c FROM Caixa c WHERE c.data BETWEEN :data1 AND :data2")
    List<Caixa> findByEntreData(@Param("data1") LocalDate dataInicial, @Param("data2") LocalDate dataFinal);

    List<Caixa> findByUsuarioIdAndDataBetween(UUID id, LocalDate dataInicial, LocalDate dataFinal);
}

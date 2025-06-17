package br.com.pcoe.repository;

import br.com.pcoe.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, UUID> {

    boolean existsByNomeIgnoreCase(String nome);

    Especialidade findByNomeIgnoreCase(String nome);

}

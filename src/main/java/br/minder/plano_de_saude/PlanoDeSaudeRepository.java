package br.minder.plano_de_saude;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanoDeSaudeRepository extends JpaRepository<PlanoDeSaude, PlanoDeSaudeId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from plano_de_saude", value = "select * from plano_de_saude where id_usuario = :usuarioId")
	Page<PlanoDeSaude> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from plano_de_saude", value = "select * from plano_de_saude where id_usuario = :usuarioId and id = :id")
	PlanoDeSaude findById(@Param("id") String id, @Param("usuarioId") String usuarioId);
}

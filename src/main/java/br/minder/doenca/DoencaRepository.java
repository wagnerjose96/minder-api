package br.minder.doenca;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoencaRepository extends JpaRepository<Doenca, DoencaId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from doenca", value = "select * from doenca where id_usuario = :usuarioId")
	Page<Doenca> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from doenca", value = "select * from doenca where id_usuario = :usuarioId")
	List<Doenca> findAll(@Param("usuarioId") String usuarioId);
}
package br.minder.alergia;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlergiaRepository extends JpaRepository<Alergia, AlergiaId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from alergia", value = "select * from alergia where id_usuario = :usuarioId")
	Page<Alergia> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from alergia", value = "select * from alergia where id_usuario = :usuarioId")
	List<Alergia> findAll(@Param("usuarioId") String usuarioId);

}

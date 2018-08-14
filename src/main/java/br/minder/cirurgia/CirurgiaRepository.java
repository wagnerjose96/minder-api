package br.minder.cirurgia;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.minder.cirurgia.Cirurgia;

public interface CirurgiaRepository extends JpaRepository<Cirurgia, CirurgiaId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from cirurgia", value = "select * from cirurgia where id_usuario = :usuarioId")
	Page<Cirurgia> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from cirurgia", value = "select * from cirurgia where id_usuario = :usuarioId")
	List<Cirurgia> findAll(@Param("usuarioId") String usuarioId);

}

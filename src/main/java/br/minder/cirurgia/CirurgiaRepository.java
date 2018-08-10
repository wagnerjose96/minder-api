package br.minder.cirurgia;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.minder.cirurgia.Cirurgia;

public interface CirurgiaRepository extends JpaRepository<Cirurgia, CirurgiaId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from cirurgia where id_usuario = :usuarioId", value = "select c.*, m.* from cirurgia_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join cirurgia c on a.id_cirurgia = c.id "
			+ "group by c.id, m.id_medicamento having c.id_usuario = :usuarioId " + "order by c.id")
	Page<Cirurgia> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from cirurgia where id_usuario = :usuarioId and tipo_cirurgia ilike :searchTerm", value = "select c.*, m.* from cirurgia_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join cirurgia c on a.id_cirurgia = c.id "
			+ "group by c.id, m.id_medicamento having c.id_usuario = :usuarioId "
			+ "and c.tipo_cirurgia ilike :searchTerm order by c.id")
	Page<Cirurgia> findAll(Pageable pageable, @Param("searchTerm") String searchTerm,
			@Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from cirurgia where id_usuario = :usuarioId", value = "select c.*, m.* from cirurgia_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join cirurgia c on a.id_cirurgia = c.id "
			+ "group by c.id, m.id_medicamento having c.id_usuario = :usuarioId " + "order by c.id")
	List<Cirurgia> findAll(@Param("usuarioId") String usuarioId);

}

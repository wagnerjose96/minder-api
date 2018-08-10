package br.minder.alergia;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlergiaRepository extends JpaRepository<Alergia, AlergiaId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from alergia where id_usuario = :usuarioId", value = "select c.*, m.* from alergia_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join alergia c on a.id_alergia = c.id "
			+ "group by c.id, m.id_medicamento having c.id_usuario = :usuarioId " + "order by c.id")
	Page<Alergia> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from alergia where id_usuario = :usuarioId and tipo_alergia ilike :searchTerm", value = "select c.*, m.* from alergia_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join alergia c on a.id_alergia = c.id "
			+ "group by c.id, m.id_medicamento having c.id_usuario = :usuarioId "
			+ "and c.tipo_alergia ilike :searchTerm order by c.id")
	Page<Alergia> findAll(Pageable pageable, @Param("searchTerm") String searchTerm,
			@Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from alergia where id_usuario = :usuarioId", value = "select c.*, m.* from alergia_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join alergia c on a.id_alergia = c.id "
			+ "group by c.id, m.id_medicamento having c.id_usuario = :usuarioId " + "order by c.id")
	List<Alergia> findAll(@Param("usuarioId") String usuarioId);

}

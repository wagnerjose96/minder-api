package br.minder.alarme;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmeRepository extends JpaRepository<Alarme, AlarmeId> {

	@Query(nativeQuery = true, 
			countQuery = "select count(id) from alarme where id_usuario = :usuarioId", 
			value = "select a.*, m.* from alarme a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento "
			+ "group by a.id, m.id_medicamento having a.id_usuario = :usuarioId " + "order by a.id")
	Page<Alarme> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, 
			countQuery = "select count(id) from alarme where id_usuario = :usuarioId and descricao ilike :searchTerm", 
			value = "select a.*, m.* from alarme a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento "
			+ "group by a.id, m.id_medicamento having a.id_usuario = :usuarioId "
			+ "and a.descricao ilike :searchTerm order by id")
	Page<Alarme> findAll(Pageable pageable, @Param("searchTerm") String searchTerm, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, 
			countQuery = "select count(id) from alarme where id_usuario = :usuarioId", 
			value = "select a.*, m.* from alarme a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento "
			+ "group by a.id, m.id_medicamento having a.id_usuario = :usuarioId "
			+ "order by id")
	        List<Alarme> findAll(@Param("usuarioId") String usuarioId);

}

package br.minder.doenca;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoencaRepository extends JpaRepository<Doenca, DoencaId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from doenca where id_usuario = :usuarioId", value = "select c.*, m.* from doenca_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join doenca c on a.id_doenca = c.id_doenca "
			+ "group by c.id_doenca, m.id_medicamento having c.id_usuario = :usuarioId "
			+ "order by c.id_doenca")
	Page<Doenca> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from doenca where id_usuario = :usuarioId and nome_doenca ilike :searchTerm", value = "select c.*, m.* from doenca_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join doenca c on a.id_doenca = c.id_doenca "
			+ "group by c.id_doenca, m.id_medicamento having c.id_usuario = :usuarioId "
			+ "and c.nome_doenca ilike :searchTerm order by c.id_doenca")
	Page<Doenca> findAll(Pageable pageable, @Param("searchTerm") String searchTerm,
			@Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from doenca where id_usuario = :usuarioId", value = "select c.*, m.* from doenca_medicamento a inner join medicamento m "
			+ "on a.id_medicamento = m.id_medicamento " + "inner join doenca c on a.id_doenca = c.id_doenca "
			+ "group by c.id_doenca, m.id_medicamento having c.id_usuario = :usuarioId "
			+ "order by c.id_doenca")
	List<Doenca> findAll(@Param("usuarioId") String usuarioId);

}
package br.minder.contato;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContatoRepository extends JpaRepository<Contato, ContatoId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from contato", value = "select e.id_emergencia, b.id_contato, c.*, t.ddd, t.* "
			+ "from emergencia e " + "inner join contato_emergencia b on e.id_emergencia = b.id_emergencia "
			+ "inner join contato c on c.id = b.id_contato " + "inner join telefone t on c.id_telefone = t.id "
			+ "group by e.id_emergencia, c.id, b.id_contato, t.id having e.id_usuario = :usuarioId " + "order by c.id")
	Page<Contato> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from contato where nome ilike :searchTerm", value = "select e.id_emergencia, b.id_contato, c.*, t.ddd, t.* "
			+ "from emergencia e " + "inner join contato_emergencia b on e.id_emergencia = b.id_emergencia "
			+ "inner join contato c on c.id = b.id_contato " + "inner join telefone t on c.id_telefone = t.id "
			+ "group by e.id_emergencia, c.id, b.id_contato, t.id having e.id_usuario = :usuarioId and c.nome ilike :searchTerm order by c.id")
	Page<Contato> findAll(Pageable pageable, @Param("searchTerm") String searchTerm,
			@Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id) from contato", value = "select e.id_emergencia, b.id_contato, c.*, t.ddd, t.* "
			+ "from emergencia e " + "inner join contato_emergencia b on e.id_emergencia = b.id_emergencia "
			+ "inner join contato c on c.id = b.id_contato " + "inner join telefone t on c.id_telefone = t.id "
			+ "group by e.id_emergencia, c.id, b.id_contato, t.id having e.id_usuario = :usuarioId " + "order by c.id")
	List<Contato> findAll(@Param("usuarioId") String usuarioId);

}

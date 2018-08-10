package br.minder.contato;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContatoRepository extends JpaRepository<Contato, ContatoId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from contato", value = "select c.*, t.* from contato c inner join telefone t "
			+ "on c.id_telefone = t.id " + "group by c.id, t.id " + "order by c.id")
	Page<Contato> findAll(Pageable pageable);

	@Query(nativeQuery = true, countQuery = "select count(id) from contato where nome ilike :searchTerm", value = "select c.*, t.* from contato c inner join telefone t "
			+ "on c.id_telefone = t.id " + "group by c.id, t.id having c.nome ilike :searchTerm order by c.id")
	Page<Contato> findAll(Pageable pageable, @Param("searchTerm") String searchTerm);

}

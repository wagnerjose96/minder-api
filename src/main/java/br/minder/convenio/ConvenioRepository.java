package br.minder.convenio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConvenioRepository extends JpaRepository<Convenio, ConvenioId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from convenio", value = "select * from convenio order by id")
	Page<Convenio> findAll(Pageable pageable);

	@Query(nativeQuery = true, countQuery = "select count(id) from convenio where nome ilike :searchTerm", value = "select * from convenio where nome ilike :searchTerm order by id")
	Page<Convenio> findAll(Pageable pageable, @Param("searchTerm") String searchTerm);

}

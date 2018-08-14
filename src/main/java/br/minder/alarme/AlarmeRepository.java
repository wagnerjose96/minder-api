package br.minder.alarme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmeRepository extends JpaRepository<Alarme, AlarmeId> {
	@Query(nativeQuery = true, countQuery = "select count(id) from alarme", value = "select * from alarme where id_usuario = :usuarioId")
	Page<Alarme> findAll(Pageable pageable, @Param("usuarioId") String usuarioId);

}

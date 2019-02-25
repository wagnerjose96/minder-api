package br.minder.alarme.alarme_hora;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmeHoraRepository extends JpaRepository<AlarmeHora, AlarmeHoraId> {	
	@Query(nativeQuery = true, value = "select * from alarme_hora where id_usuario = :usuarioId and id_alarme = :alarmeId")
	List<AlarmeHora> findById(@Param("usuarioId") String usuarioId, @Param("alarmeId") String alarmeId);	
	
	@Query(nativeQuery = true, value = "select * from alarme_hora where id_usuario = :usuarioId")
	List<AlarmeHora> findAll(@Param("usuarioId") String usuarioId);	
}

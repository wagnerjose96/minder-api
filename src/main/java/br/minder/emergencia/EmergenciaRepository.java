package br.minder.emergencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmergenciaRepository extends JpaRepository<Emergencia, EmergenciaId> {
	@Query(nativeQuery = true, countQuery = "select count(id_emergencia) from emergencia", value = "select * from emergencia where id_usuario = :usuarioId")
	Emergencia findAll(@Param("usuarioId") String usuarioId);

	@Query(nativeQuery = true, countQuery = "select count(id_emergencia) from emergencia", value = "select * from emergencia where id_usuario = :usuarioId and id_emergencia = :id")
	Emergencia findById(@Param("id") String id, @Param("usuarioId") String usuarioId);

}

package br.minder.emergencia;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergenciaRepository extends JpaRepository<Emergencia, EmergenciaId> {

}

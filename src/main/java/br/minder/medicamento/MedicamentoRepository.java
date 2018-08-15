package br.minder.medicamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicamentoRepository extends JpaRepository<Medicamento, MedicamentoId> {
	@Query(nativeQuery = true, countQuery = "select count(id_medicamento) from medicamento", value = "select * from medicamento where ativo = 1")
	Page<Medicamento> findAll(Pageable pageable);

	@Query(nativeQuery = true, countQuery = "select count(id_medicamento) from medicamento", value = "select * from medicamento where ativo = 1 and id_medicamento = :medicamentoId")
	Medicamento findById(@Param("medicamentoId") String medicamentoId);

}

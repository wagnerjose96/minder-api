package br.hela.medicamento;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentoRepository extends JpaRepository<Medicamento, MedicamentoId> {

}

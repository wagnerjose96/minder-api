package br.minder.cirurgia;

import org.springframework.data.jpa.repository.JpaRepository;
import br.minder.cirurgia.Cirurgia;

public interface CirurgiaRepository extends JpaRepository<Cirurgia, CirurgiaId> {
	
}

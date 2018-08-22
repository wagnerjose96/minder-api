package br.minder.alergia.alergia_medicamento;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

@Service
@Transactional
public class AlergiaMedicamentoService {
	@Autowired
	private AlergiaMedicamentoRepository repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "DELETE FROM alergia_medicamento where id_alergia = ?";

	public void salvar(AlergiaMedicamento novoAlergiaMedicamento) {
		repo.save(novoAlergiaMedicamento);
	}

	public void alterar(String id) {
		Object[] param = { id };
		jdbcTemplate.update(sql, param);
	}

	public List<AlergiaMedicamento> encontrar() {
		return repo.findAll();
	}

	public Optional<AlergiaMedicamento> encontrar(AlergiaMedicamentoId id) {
		return repo.findById(id);
	}

}

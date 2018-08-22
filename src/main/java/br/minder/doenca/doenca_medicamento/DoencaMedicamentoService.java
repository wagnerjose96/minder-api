package br.minder.doenca.doenca_medicamento;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DoencaMedicamentoService {
	@Autowired
	private DoencaMedicamentoRepository repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "DELETE FROM doenca_medicamento where id_doenca = ?";

	public void salvar(DoencaMedicamento novoDoencaMedicamento) {
		repo.save(novoDoencaMedicamento);
	}

	public List<DoencaMedicamento> encontrar() {
		return repo.findAll();
	}

	public Optional<DoencaMedicamento> encontrar(DoencaMedicamentoId id) {
		return repo.findById(id);
	}

	public void alterar(String id) {
		Object[] param = { id };
		jdbcTemplate.update(sql, param);
	}
}
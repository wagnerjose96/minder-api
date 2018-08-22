package br.minder.cirurgia.cirurgia_medicamento;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CirurgiaMedicamentoService {
	@Autowired
	private CirurgiaMedicamentoRepository repo;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String sql = "DELETE FROM cirurgia_medicamento where id_cirurgia = ?";

	public void salvar(CirurgiaMedicamento novoCirurgiaMedicamento) {
		repo.save(novoCirurgiaMedicamento);
	}

	public List<CirurgiaMedicamento> encontrar() {
		return repo.findAll();
	}

	public Optional<CirurgiaMedicamento> encontrar(CirurgiaMedicamentoId id) {
		return repo.findById(id);
	}

	public void alterar(String id) {
		Object[] param = { id };
		jdbcTemplate.update(sql, param);
	}
}

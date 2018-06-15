package br.hela.emergencia.emergencia_usuario;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class Emergencia_Usuario_Service {
	@Autowired
	private Emergencia_Usuario_Repository repo;

	public void salvar(Emergencia_Usuario novoEmergenciaUsuario) {
		repo.save(novoEmergenciaUsuario);
	}

	public List<Emergencia_Usuario> encontrar() {
		return repo.findAll();
	}
}
package br.hela.usuarioAdm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.hela.usuarioAdm.UsuarioAdm;
import br.hela.usuarioAdm.UsuarioAdmId;
import br.hela.usuarioAdm.UsuarioAdmRepository;
import br.hela.usuarioAdm.comandos.BuscarUsuarioAdm;
import br.hela.usuarioAdm.comandos.CriarUsuarioAdm;
import br.hela.usuarioAdm.comandos.EditarUsuarioAdm;

@Service
@Transactional
public class UsuarioAdmService {
	@Autowired
	private UsuarioAdmRepository repo;

	public Optional<UsuarioAdmId> salvar(CriarUsuarioAdm comando) {
		UsuarioAdm novo = repo.save(new UsuarioAdm(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarUsuarioAdm> encontrar(UsuarioAdmId id) {
		UsuarioAdm adm = repo.findById(id).get();
		BuscarUsuarioAdm resultado = new BuscarUsuarioAdm(adm);
		return Optional.of(resultado);
	}

	public Optional<List<BuscarUsuarioAdm>> encontrar() {
		List<UsuarioAdm> adms = repo.findAll();
		List<BuscarUsuarioAdm> resultados = new ArrayList<>();
		for (UsuarioAdm adm : adms) {
			BuscarUsuarioAdm nova = new BuscarUsuarioAdm(adm);
			resultados.add(nova);
		}
		return Optional.of(resultados);
	}

	public Optional<String> deletar(UsuarioAdmId id) {
		UsuarioAdm usuario = repo.findById(id).get();
		repo.delete(usuario);
		return Optional.of("Usuário -> " + id + ": deletado com sucesso");
	}

	public Optional<UsuarioAdmId> alterar(EditarUsuarioAdm comando) {
		Optional<UsuarioAdm> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			UsuarioAdm user = optional.get();
			user.apply(comando);
			repo.save(user);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}

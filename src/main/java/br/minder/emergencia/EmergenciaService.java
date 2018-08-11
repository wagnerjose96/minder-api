package br.minder.emergencia;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.minder.alergia.Alergia;
import br.minder.alergia.AlergiaRepository;
import br.minder.cirurgia.Cirurgia;
import br.minder.cirurgia.CirurgiaRepository;
import br.minder.doenca.Doenca;
import br.minder.doenca.DoencaRepository;
import br.minder.emergencia.comandos.BuscarEmergencia;
import br.minder.emergencia.comandos.BuscarEmergenciaPdf;
import br.minder.emergencia.comandos.CriarEmergencia;
import br.minder.emergencia.comandos.EditarEmergencia;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.sangue.comandos.BuscarSangue;
import br.minder.usuario.UsuarioId;
import br.minder.usuario.UsuarioService;
import br.minder.usuario.comandos.BuscarUsuario;

@Service
@Transactional
public class EmergenciaService {

	@Autowired
	private AlergiaRepository alergiaRepo;

	@Autowired
	private CirurgiaRepository cirurgiaRepo;

	@Autowired
	private DoencaRepository doencaRepo;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private EmergenciaRepository repo;

	public Optional<EmergenciaId> salvar(CriarEmergencia comando, UsuarioId id) {
		Emergencia novo = repo.save(new Emergencia(comando, id));
		return Optional.of(novo.getIdEmergencia());
	}

	private BuscarEmergenciaPdf construirEmergencia(Optional<BuscarUsuario> user, BuscarEmergenciaPdf resultado) {
		if (user.isPresent()) {
			String nome = user.get().getNome();
			BuscarEndereco endereco = user.get().getEndereco();
			BuscarSangue sangue = user.get().getSangue();
			List<Alergia> alergias = alergiaRepo.findAll(user.get().getId().toString());
			List<Cirurgia> cirurgias = cirurgiaRepo.findAll(user.get().getId().toString());
			List<Doenca> doencas = doencaRepo.findAll(user.get().getId().toString());
			if (!alergias.isEmpty() && !cirurgias.isEmpty() && !doencas.isEmpty()) {
				resultado.setAlergias(alergias);
				resultado.setCirurgias(cirurgias);
				resultado.setDoencas(doencas);
			}
			resultado.setNomeDoUsuario(nome);
			resultado.setTipoSanguineo(sangue);
			resultado.setEndereco(endereco);

		}
		return resultado;
	}

	public Optional<BuscarEmergenciaPdf> encontrarPdf(UsuarioId id) {
		List<Emergencia> emergencias = repo.findAll();
		for (Emergencia emergencia : emergencias) {
			if (id.toString().equals(emergencia.getIdUsuario().toString())) {
				BuscarEmergenciaPdf resultados = construirEmergencia(usuarioService.encontrar(id),
						new BuscarEmergenciaPdf(emergencia));
				return Optional.of(resultados);
			}
		}
		return Optional.empty();
	}

	public Optional<BuscarEmergencia> encontrar(UsuarioId id) {
		List<Emergencia> emergencias = repo.findAll();
		for (Emergencia emergencia : emergencias) {
			if (id.toString().equals(emergencia.getIdUsuario().toString())) {
				return Optional.of(new BuscarEmergencia(emergencia));
			}
		}
		return Optional.empty();
	}

	public Optional<EmergenciaId> alterar(EditarEmergencia comando) {
		Optional<Emergencia> optional = repo.findById(comando.getId());
		if (optional.isPresent()) {
			Emergencia emer = optional.get();
			emer.apply(comando);
			repo.save(emer);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}
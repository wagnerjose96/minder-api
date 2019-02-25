package br.minder.emergencia;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.minder.alergia.AlergiaService;
import br.minder.alergia.comandos.BuscarAlergia;
import br.minder.cirurgia.CirurgiaService;
import br.minder.cirurgia.comandos.BuscarCirurgia;
import br.minder.doenca.DoencaService;
import br.minder.doenca.comandos.BuscarDoenca;
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
	private AlergiaService alergia;

	@Autowired
	private CirurgiaService cirurgia;

	@Autowired
	private DoencaService doenca;

	@Autowired
	private UsuarioService usuario;

	@Autowired
	private EmergenciaRepository repo;

	public Optional<EmergenciaId> salvar(CriarEmergencia comando, UsuarioId id) {
		Emergencia novo = repo.save(new Emergencia(comando, id));
		return Optional.of(novo.getIdEmergencia());
	}

	private BuscarEmergenciaPdf construirEmergencia(Optional<BuscarUsuario> optional, BuscarEmergenciaPdf resultado) {
		if (optional.isPresent()) {
			String nome = optional.get().getNome();
			BuscarEndereco endereco = optional.get().getEndereco();
			BuscarSangue sangue = optional.get().getSangue();
			List<BuscarAlergia> alergias = alergia.encontrar(optional.get().getId());
			List<BuscarCirurgia> cirurgias = cirurgia.encontrar(optional.get().getId());
			List<BuscarDoenca> doencas = doenca.encontrar(optional.get().getId());
			if (!alergias.isEmpty()) {
				resultado.setAlergias(alergias);
			}
			if (!cirurgias.isEmpty()) {
				resultado.setCirurgias(cirurgias);

			}
			if (!doencas.isEmpty()) {
				resultado.setDoencas(doencas);
			}
			resultado.setNomeDoUsuario(nome);
			resultado.setTipoSanguineo(sangue);
			resultado.setEndereco(endereco);
		}
		return resultado;
	}

	public Optional<BuscarEmergenciaPdf> encontrarPdf(UsuarioId id) {
		Emergencia emergencia = repo.findAll(id.toString());
		if (emergencia != null) {
			return Optional.of(construirEmergencia(usuario.encontrar(id), new BuscarEmergenciaPdf(emergencia)));
		}
		return Optional.empty();
	}

	public Optional<BuscarEmergencia> encontrar(UsuarioId id) {
		Emergencia emergencia = repo.findAll(id.toString());
		if (emergencia != null) {
			return Optional.of(new BuscarEmergencia(emergencia));
		}
		return Optional.empty();
	}

	public Optional<EmergenciaId> alterar(EditarEmergencia comando, UsuarioId usuarioId) {
		Emergencia optional = repo.findById(comando.getId().toString(), usuarioId.toString());
		if (optional != null) {
			Emergencia emer = optional;
			emer.apply(comando);
			repo.save(emer);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}
}
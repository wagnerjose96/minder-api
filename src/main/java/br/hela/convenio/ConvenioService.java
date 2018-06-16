package br.hela.convenio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.convenio.Convenio;
import br.hela.convenio.ConvenioId;
import br.hela.convenio.ConvenioRepository;
import br.hela.convenio.comandos.BuscarConvenio;
import br.hela.convenio.comandos.CriarConvenio;
import br.hela.convenio.comandos.EditarConvenio;

@Service
@Transactional
public class ConvenioService {
	@Autowired
	private ConvenioRepository convenioRepo;

	public Optional<ConvenioId> salvar(CriarConvenio comando) {
		Convenio novo = convenioRepo.save(new Convenio(comando));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarConvenio> encontrar(ConvenioId id) {
		Convenio convenio = convenioRepo.findById(id).get();
		BuscarConvenio resultado = new BuscarConvenio();
		if (convenio.getAtivo() == 1) {
			resultado = new BuscarConvenio(convenio);
		}
		return Optional.of(resultado);
	}

	public Optional<List<BuscarConvenio>> encontrar() {
		List<BuscarConvenio> resultados = new ArrayList<>();
		List<Convenio> convenios = convenioRepo.findAll();
		for (Convenio convenio : convenios) {
			if (convenio.getAtivo() == 1) {
				BuscarConvenio nova = new BuscarConvenio(convenio);
				resultados.add(nova);
			}
		}
		return Optional.of(resultados);
	}

	public Optional<String> deletar(ConvenioId id) {
		Convenio convenio = convenioRepo.findById(id).get();
		convenio.setAtivo(0);
		convenioRepo.save(convenio);
		return Optional.of("Convênio -> " + id + ": deletado com sucesso");
	}

	public Optional<ConvenioId> alterar(EditarConvenio comando) {
		Optional<Convenio> optional = convenioRepo.findById(comando.getId());
		if (optional.isPresent()) {
			Convenio conv = optional.get();
			conv.apply(comando);
			convenioRepo.save(conv);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}

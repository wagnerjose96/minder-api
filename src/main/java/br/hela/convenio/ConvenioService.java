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
import br.hela.convenio.comandos.CriarConvenio;
import br.hela.convenio.comandos.EditarConvenio;

@Service
@Transactional
public class ConvenioService {
	@Autowired
	private ConvenioRepository convenioRepo;

	public Optional<ConvenioId> salvar(CriarConvenio comando) {
		comando.setAtivo(1);
		Convenio novo = convenioRepo.save(new Convenio(comando));
		return Optional.of(novo.getId());
	}

	public Optional<Convenio> encontrar(ConvenioId id) {
		Convenio convenio = convenioRepo.findById(id).get();
		if (convenio.getAtivo() == 1) {
			return Optional.of(convenio);
		}
		return Optional.empty();
	}

	public Optional<List<Convenio>> encontrar() {
		List<Convenio> resultados = new ArrayList<>();
		List<Convenio> convenios = convenioRepo.findAll();
		for (Convenio convenio : convenios) {
			if (convenio.getAtivo() == 1) {
				resultados.add(convenio);
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
			comando.setAtivo(1);
			conv.apply(comando);
			convenioRepo.save(conv);
			return Optional.of(comando.getId());
		}
		return Optional.empty();
	}

}

package br.hela.tipoSanguineo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.tipoSanguineo.comandos.CriarTipoSanguineo;

@Service
@Transactional
public class TipoSanguineoService {
	@Autowired
	TipoSanguineoRepository repoSangue;
	
	public Optional<List<TipoSanguineo>> encontrarTudo(){
		return Optional.of(repoSangue.findAll());
	}
	
	public Optional<TipoSanguineo> encontrarPorId(TipoSanguineoId id){
		return repoSangue.findById(id);
	}
	
	public Optional<TipoSanguineoId> salvar(CriarTipoSanguineo comando){
		TipoSanguineo novo = new TipoSanguineo(comando);
		repoSangue.save(novo);
		return Optional.of(novo.getTipoSanguineoId());
	}

}

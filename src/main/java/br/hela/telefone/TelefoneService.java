package br.hela.telefone;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.hela.telefone.comandos.CriarTelefone;
import br.hela.telefone.comandos.EditarTelefone;

@Service
@Transactional
public class TelefoneService {
	@Autowired
	TelefoneRepository repo;
	
	public Optional<List<Telefone>> encontrar(){
		return Optional.of(repo.findAll());
	}
	
	public Optional<Telefone> encontrar (TelefoneId id){
		return repo.findById(id);
	}
	
	public Optional<TelefoneId> salvar(CriarTelefone comandos){
		Telefone novo = new Telefone(comandos);
		repo.save(novo);
		return Optional.of(novo.getTelefoneId());
	}
	
	public Optional<String> deletar(TelefoneId id){
		repo.deleteById(id);
		return Optional.of("Telefone -> " + id + " deletado com sucesso");
	}
	
	public Optional<TelefoneId> alterar (EditarTelefone comandos){
		Optional<Telefone> optional = repo.findById(comandos.getTelefoneId());
		if(optional.isPresent()) {
			Telefone telefone = optional.get();
			telefone.aplly(comandos);
			repo.save(telefone);
			return Optional.of(telefone.getTelefoneId());
		}
		return Optional.empty();
	}
	
}

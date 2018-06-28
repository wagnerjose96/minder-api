package br.hela.pergunta_notificacao.pergunta_resposta_usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.hela.pergunta_notificacao.PerguntaService;
import br.hela.pergunta_notificacao.comandos.BuscarPergunta;
import br.hela.pergunta_notificacao.pergunta_resposta_usuario.comandos.BuscarPerguntaRespostaUsuario;
import br.hela.pergunta_notificacao.pergunta_resposta_usuario.comandos.CriarPerguntaRespostaUsuario;
import br.hela.pergunta_notificacao.resposta.RespostaService;
import br.hela.pergunta_notificacao.resposta.comandos.BuscarResposta;
import br.hela.usuario.UsuarioId;

@Service
@Transactional
public class PerguntaRespostaUsuarioService {
	@Autowired
	private PerguntaRespostaUsuarioRepository repo;

	@Autowired
	private PerguntaService perguntaService;

	@Autowired
	private RespostaService respostaService;

	public Optional<PerguntaRespostaUsuarioId> salvar(CriarPerguntaRespostaUsuario comando, UsuarioId id) {
		PerguntaRespostaUsuario novo = repo.save(new PerguntaRespostaUsuario(comando, id));
		return Optional.of(novo.getId());
	}

	public Optional<BuscarPerguntaRespostaUsuario> encontrar(PerguntaRespostaUsuarioId id, UsuarioId idUser) {
		Optional<PerguntaRespostaUsuario> perguntaResposta = repo.findById(id);
		if (perguntaResposta.isPresent()) {
			return Optional.of(construir(idUser, perguntaResposta.get()));
		}
		return Optional.empty();
	}

	public Optional<List<BuscarPerguntaRespostaUsuario>> encontrar(UsuarioId id) {
		List<BuscarPerguntaRespostaUsuario> resultados = new ArrayList<>();
		List<PerguntaRespostaUsuario> perguntaRespostas = repo.findAll();
		for (PerguntaRespostaUsuario perguntaResposta : perguntaRespostas) {
			if (id.toString().equals(perguntaResposta.getIdUsuario().toString())) {
				resultados.add(construir(id, perguntaResposta));
			}
		}
		return Optional.of(resultados);
	}

	private BuscarPerguntaRespostaUsuario construir(UsuarioId id, PerguntaRespostaUsuario resultado) {
		BuscarPerguntaRespostaUsuario result = new BuscarPerguntaRespostaUsuario(resultado);
		Optional<BuscarPergunta> pergunta = perguntaService.encontrar(resultado.getIdPergunta());
		Optional<BuscarResposta> resposta = respostaService.encontrar(resultado.getIdResposta());
		if (pergunta.isPresent() && resposta.isPresent()) {
			result.setPergunta(pergunta.get());
			result.setResposta(resposta.get());
		}
		return result;
	}
}

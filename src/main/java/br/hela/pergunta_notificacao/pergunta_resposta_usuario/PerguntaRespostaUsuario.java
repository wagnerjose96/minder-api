package br.hela.pergunta_notificacao.pergunta_resposta_usuario;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import br.hela.pergunta_notificacao.PerguntaId;
import br.hela.pergunta_notificacao.pergunta_resposta_usuario.comandos.CriarPerguntaRespostaUsuario;
import br.hela.pergunta_notificacao.resposta.RespostaId;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Entity
@Data
public class PerguntaRespostaUsuario {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id"))
	private PerguntaRespostaUsuarioId id;
	@AttributeOverride(name = "value", column = @Column(name = "id_pergunta"))
	private PerguntaId idPergunta;
	@AttributeOverride(name = "value", column = @Column(name = "id_resposta"))
	private RespostaId idResposta;
	@AttributeOverride(name = "value", column = @Column(name = "id_usuario"))
	private UsuarioId idUsuario;
	
	public PerguntaRespostaUsuario() {
	}

	public PerguntaRespostaUsuario(CriarPerguntaRespostaUsuario comando, UsuarioId id) {
		this.id = new PerguntaRespostaUsuarioId();
		this.idPergunta = comando.getIdPergunta();
		this.idResposta = comando.getIdResposta();
		this.idUsuario = id;
	}
}

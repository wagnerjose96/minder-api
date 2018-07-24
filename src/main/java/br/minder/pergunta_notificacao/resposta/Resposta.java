package br.minder.pergunta_notificacao.resposta;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.pergunta_notificacao.PerguntaId;
import br.minder.pergunta_notificacao.resposta.comandos.CriarResposta;
import br.minder.pergunta_notificacao.resposta.comandos.EditarResposta;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
public class Resposta {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_resposta"))
	@Setter(AccessLevel.NONE)
	private RespostaId idResposta;
	@AttributeOverride(name = "value", column = @Column(name = "id_pergunta"))
	private PerguntaId idPergunta;
	private String descricao;

	public Resposta() {
	}

	public Resposta(CriarResposta comando) {
		this.idResposta = new RespostaId();
		this.descricao = comando.getDescricao();
		this.idPergunta = comando.getIdPergunta();
	}
	
	public void apply(EditarResposta comando) {
		this.idResposta = comando.getIdResposta();
		this.descricao = comando.getDescricao();
	}
}

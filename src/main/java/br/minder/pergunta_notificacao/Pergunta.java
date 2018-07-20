package br.minder.pergunta_notificacao;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.hibernate.envers.Audited;

import br.minder.pergunta_notificacao.comandos.CriarPergunta;
import br.minder.pergunta_notificacao.comandos.EditarPergunta;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Audited
@Getter
@Setter
public class Pergunta {
	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "id_pergunta"))
	@Setter(AccessLevel.NONE)
	private PerguntaId idPergunta;
	private String descricao;

	public Pergunta() {
	}

	public Pergunta(CriarPergunta comando) {
		this.idPergunta = new PerguntaId();
		this.descricao = comando.getDescricao();
	}

	public void apply(EditarPergunta comando) {
		this.idPergunta = comando.getIdPergunta();
		this.descricao = comando.getDescricao();
	}
	
}

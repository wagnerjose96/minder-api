package br.minder.plano_de_saude.comandos;

import java.math.BigInteger;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;

import br.minder.convenio.ConvenioId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarPlanoDeSaude {
	@AttributeOverride(name = "value", column = @Column(name = "id_convenio"))
	private ConvenioId idConvenio;
	private BigInteger numeroCartao;
	private String habitacao;
	private String territorio;
	private String dataVencimento;
}

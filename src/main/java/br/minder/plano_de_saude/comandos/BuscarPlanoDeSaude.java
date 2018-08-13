package br.minder.plano_de_saude.comandos;

import java.math.BigInteger;
import br.minder.convenio.Convenio;
import br.minder.conversor.ConverterData;
import br.minder.plano_de_saude.PlanoDeSaude;
import br.minder.plano_de_saude.PlanoDeSaudeId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarPlanoDeSaude {
	private PlanoDeSaudeId id;
	private Convenio convenio;
	private BigInteger numeroCartao;
	private String habitacao;
	private String territorio;
	private String dataVencimento;

	public BuscarPlanoDeSaude(PlanoDeSaude comandos) {
		this.id = comandos.getId();
		this.numeroCartao = comandos.getNumeroCartao();
		this.habitacao = comandos.getHabitacao();
		this.territorio = comandos.getTerritorio();
		Long dataLong = comandos.getDataVencimento().getTime();
		this.dataVencimento = ConverterData.converterDataVencimento(dataLong);
	}
}

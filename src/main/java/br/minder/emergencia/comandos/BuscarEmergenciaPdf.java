package br.minder.emergencia.comandos;

import java.util.ArrayList;
import java.util.List;
import br.minder.alergia.Alergia;
import br.minder.cirurgia.Cirurgia;
import br.minder.doenca.Doenca;
import br.minder.emergencia.Emergencia;
import br.minder.emergencia.EmergenciaId;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.sangue.comandos.BuscarSangue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarEmergenciaPdf {
	private EmergenciaId id;
	private String nomeDoUsuario;
	private BuscarEndereco endereco;
	private BuscarSangue tipoSanguineo;
	private List<Alergia> alergias = new ArrayList<>();
	private List<Cirurgia> cirurgias = new ArrayList<>();
	private List<Doenca> doencas = new ArrayList<>();
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	private int doadorDeOrgaos;

	public BuscarEmergenciaPdf(Emergencia comandos) {
		this.id = comandos.getIdEmergencia();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
	}

}

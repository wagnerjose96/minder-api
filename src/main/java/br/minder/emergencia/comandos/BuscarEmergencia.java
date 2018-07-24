package br.minder.emergencia.comandos;

import java.util.ArrayList;
import java.util.List;

import br.minder.alergia.comandos.BuscarAlergia;
import br.minder.cirurgia.comandos.BuscarCirurgia;
import br.minder.contato.comandos.BuscarContato;
import br.minder.doenca.comandos.BuscarDoenca;
import br.minder.emergencia.Emergencia;
import br.minder.emergencia.EmergenciaId;
import br.minder.endereco.comandos.BuscarEndereco;
import br.minder.sangue.comandos.BuscarSangue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuscarEmergencia {
	private EmergenciaId id;
	private String nomeDoUsuario;
	private BuscarEndereco endereco;
	private BuscarSangue tipoSanguineo;
	private List<BuscarAlergia> alergias = new ArrayList<>();
	private List<BuscarCirurgia> cirurgias = new ArrayList<>();
	private List<BuscarDoenca> doencas = new ArrayList<>();
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	private int doadorDeOrgaos;
	private List<BuscarContato> contatos = new ArrayList<>();

	public BuscarEmergencia(Emergencia comandos) {
		this.id = comandos.getIdEmergencia();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
	}

	public BuscarEmergencia() {
	}
}

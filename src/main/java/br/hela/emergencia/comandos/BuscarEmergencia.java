package br.hela.emergencia.comandos;

import java.util.ArrayList;
import java.util.List;
import br.hela.alergia.comandos.BuscarAlergia;
import br.hela.cirurgia.comandos.BuscarCirurgia;
import br.hela.contato.comandos.BuscarContato;
import br.hela.doenca.comandos.BuscarDoenca;
import br.hela.emergencia.Emergencia;
import br.hela.emergencia.EmergenciaId;
import br.hela.endereco.comandos.BuscarEndereco;
import br.hela.sangue.comandos.BuscarSangue;
import lombok.Data;

@Data
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

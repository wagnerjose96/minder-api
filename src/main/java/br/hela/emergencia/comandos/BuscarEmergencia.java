package br.hela.emergencia.comandos;

import java.util.ArrayList;
import java.util.List;

import br.hela.contato.comandos.BuscarContato;
import br.hela.emergencia.Emergencia;
import br.hela.emergencia.EmergenciaId;
import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class BuscarEmergencia {
	private EmergenciaId id;
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	public int doadorDeOrgaos;
	public UsuarioId idUsuario;
	private List<BuscarContato> contatos = new ArrayList<>();

	public BuscarEmergencia(Emergencia comandos) {
		this.id = comandos.getIdEmergencia();
		this.ataqueConvulsivos = comandos.getAtaqueConvulsivos();
		this.problemasCardiacos = comandos.getProblemasCardiacos();
		this.doadorDeOrgaos = comandos.getDoadorDeOrgaos();
		this.idUsuario = comandos.getIdUsuario();
	}

	public BuscarEmergencia() {
	}
}

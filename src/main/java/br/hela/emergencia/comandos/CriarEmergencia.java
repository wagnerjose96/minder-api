package br.hela.emergencia.comandos;

import br.hela.usuario.UsuarioId;
import lombok.Data;

@Data
public class CriarEmergencia {
	private int ataqueConvulsivos;
	private String problemasCardiacos;
	public int doadorDeOrgaos;
	private UsuarioId idUsuario;
}

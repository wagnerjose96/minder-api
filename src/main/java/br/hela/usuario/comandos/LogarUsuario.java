package br.hela.usuario.comandos;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Data;

@Data
public class LogarUsuario {
	private String email;
	private String nome_usuario;
	private String senha;
	private int ativo;

	public String criptografa(String senha) {
		String senhaCriptografada = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(senha.getBytes(), 0, senha.length());
			senhaCriptografada = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException ns) {
			ns.printStackTrace();
		}
		return senhaCriptografada;
	}
}

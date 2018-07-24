package br.minder.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

@Component
public class Criptografia {
	public static String criptografa(String senha) {
		String senhaCriptografada = null;
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(senha.getBytes(), 0, senha.length());
			senhaCriptografada = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.getMessage();
		}
		return senhaCriptografada;
	}

	private Criptografia() {
	}

}

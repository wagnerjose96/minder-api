package br.minder.esqueci_senha;

import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.minder.esqueci_senha.comandos.EsqueciSenha;
import br.minder.esqueci_senha.comandos.GerarSenha;
import br.minder.usuario.Usuario;
import br.minder.usuario.UsuarioId;
import br.minder.usuario.UsuarioRepository;

@Service
@Transactional
public class SenhaService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UsuarioRepository repo;

	private String sqlSenha = "select email, nome_completo, id, ativo from usuario " + "where email = ? and ativo = 1";

	private List<GerarSenha> consultarId(EsqueciSenha comando) {
		String email = comando.getEmail();
		return jdbcTemplate.query(sqlSenha, new Object[] { email }, (rs, rowNum) -> {
			GerarSenha usuario = new GerarSenha();
			String emailUsuario = rs.getString("email");
			if (emailUsuario.equals(email)) {
				usuario.setEmail(rs.getString("email"));
				usuario.setId(new UsuarioId(rs.getString("id")));
				usuario.setAtivo(rs.getInt("ativo"));
				usuario.setNome(rs.getString("nome_completo"));
			}
			return usuario;
		});
	}

	private StringBuilder criarSenha() {
		String[] carct = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };
		StringBuilder senha = new StringBuilder();
		Random r = new Random();
		for (int x = 0; x < 6; x++) {
			int j = (r.nextInt(62));
			senha.append(carct[j]);
		}
		return senha;
	}

	public boolean gerarSenhaAleatoria(EsqueciSenha comando) throws MessagingException, UnsupportedEncodingException {
		List<GerarSenha> usuario = consultarId(comando);
		String senha = null;
		if (!usuario.isEmpty()) {
			Optional<Usuario> optional = repo.findById(usuario.get(0).getId());
			if (optional.isPresent()) {
				Usuario user = optional.get();
				senha = criarSenha().toString();
				usuario.get(0).setSenha(senha);
				user.applySenha(usuario.get(0));
				repo.save(user);
				sendEmail(senha, usuario.get(0).getEmail(), usuario.get(0).getNome());
			}
		}
		return senha != null;
	}

	public static void sendEmail(String senha, String email, String nome) throws MessagingException, UnsupportedEncodingException {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.setFrom(new InternetAddress("minder.application@gmail.com", "Minder"));
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
		generateMailMessage.setSubject("Minder - Recuperação de senha");
		String emailBody = "<br><br>Prezado(a) <b>" + nome.toUpperCase() + "</b>,"
				+ "<br><br> A sua solicitação de geração de nova senha foi concluída com sucesso."
				+ "<br><b> Sua nova senha é: </b>" + senha
				+ "<br> Este é um e-mail automático, não é necessário respondê-lo." + "<br><br> Atenciosamente,"
				+ " <br><b> Equipe Minder </b>";
		generateMailMessage.setContent(emailBody, "text/html; charset=UTF-8");

		Transport transport = getMailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", "minder.application@gmail.com", "15065132");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}

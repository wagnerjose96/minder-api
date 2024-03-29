package br.minder.lista_de_espera;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ListaDeEsperaService {

	private static final String EMAIL = "minder.application@gmail.com";

	private static String formataEmail(String email) {
		email = email.replaceAll("%40", "@");
		email = email.replaceAll("=", "");
		email = email.replaceAll("email", "");
		return email;
	}

	public boolean sendEmail(String email) throws MessagingException, UnsupportedEncodingException {
		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.setFrom(new InternetAddress(EMAIL, "Minder"));
		InternetAddress[] address = { new InternetAddress(EMAIL), new InternetAddress(formataEmail(email)) };
		generateMailMessage.addRecipients(Message.RecipientType.TO, address);
		generateMailMessage.setSubject("Minder - Bem vindo(a) a nossa lista de espera");
		String emailBody = "<div style=\"text-align: center;\"><strong>O que é o Minder ?</strong><br>\n"
				+ "&nbsp;</div>\n" + "\n"
				+ "<div style=\"text-align: justify;\">Uma aplicação móvel onde você guarda todo seu histórico de saúde, como anamneses, exames, vacinas, carteirinhas, consultas, remédios, informações de emergência, dentre outros dados importantes, tudo em um único lugar e totalmente portátil.<br>\n"
				+ "<br>\n"
				+ "Agora você está na nossa lista de espera, fique tranquilo que iremos te avisar quando o aplicativo for lançado nas lojas virtuais Google Play e Apple Store.<br>\n"
				+ "<br>\n"
				+ "Para esclarecimento de dúvidas, por favor entre em contato conosco através deste e-mail!</div>";
		generateMailMessage.setContent(emailBody, "text/html; charset=UTF-8");

		Transport transport = getMailSession.getTransport("smtp");
		transport.connect("smtp.gmail.com", EMAIL, "15065132");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
		return true;
	}
}

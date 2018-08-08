package br.minder.conversor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ConverterData {
	public static String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		return formato.format(data);
	}

	public static String converterDataToTest(Date date) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		return formato.format(date);
	}

	private ConverterData() {

	}

	public static Date converterDataVencimentoSalvar(String string) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM");
		Date data = null;
		try {
			data = formato.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String converterDataVencimento(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("MM-yyyy");
		return formato.format(data);
	}
}

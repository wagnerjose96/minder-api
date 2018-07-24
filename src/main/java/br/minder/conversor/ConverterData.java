package br.minder.conversor;

import java.text.SimpleDateFormat;
import java.sql.Date;

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
}

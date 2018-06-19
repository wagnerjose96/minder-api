package br.hela;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class ConverterData {
	public static String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		String dataFormatada = formato.format(data);
		return dataFormatada;
	}
}

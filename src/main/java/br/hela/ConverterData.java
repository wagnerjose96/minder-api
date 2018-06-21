package br.hela;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class ConverterData {
	public static String converterData(Long data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		return formato.format(data);
	}

	private ConverterData() {

	}
}

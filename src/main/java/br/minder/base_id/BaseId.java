package br.minder.base_id;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseId implements Serializable {
	private static final long serialVersionUID = 8965550305250511524L;
	private String value;

	public BaseId() {
		this.value = UUID.randomUUID().toString();
	}

	public BaseId(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}

package br.hela;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.MappedSuperclass;

import io.swagger.annotations.ApiModelProperty;


@MappedSuperclass
public abstract class BaseId implements Serializable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(required=true)
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

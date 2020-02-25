package com.zenvia.challenge.caixa.enums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public enum TipoCedulaEnum {

	CEDULA_10(10), CEDULA_20(20), CEDULA_50(50), CEDULA_100(100);
	
	private Integer valorFaceCedula;
	
	private BigDecimal valor;
	
	private TipoCedulaEnum(Integer valorFaceCedula) {
		this.valorFaceCedula = valorFaceCedula;
		this.valor = BigDecimal.valueOf(valorFaceCedula);
	}

	public Integer getValorFaceCedula() {
		return valorFaceCedula;
	}

	public BigDecimal getValor() {
		return valor;
	}
	
	/**
	 * Retorna os elementos da enum ordenados de forma decrescente pelo valor
	 * @return
	 */
	public static List<TipoCedulaEnum> getDecrescentValues() {
		List<TipoCedulaEnum> tiposCedula = new ArrayList<>();
		
		tiposCedula.add(CEDULA_100);
		tiposCedula.add(CEDULA_50);
		tiposCedula.add(CEDULA_20);
		tiposCedula.add(CEDULA_10);
		
		return tiposCedula;
	}
}

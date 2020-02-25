package com.zenvia.challenge.caixa.impl.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zenvia.challenge.caixa.arch.exceptions.handlers.BusinessException;
import com.zenvia.challenge.caixa.enums.TipoCedulaEnum;

@Service
public class SaqueService {
	
	public static final String MSG_VALOR_SAQUE_DEVE_SER_MULTIPLO = "Não é possível sacar esse valor com as cédulas disponíveis.";

	public static final String MSG_VALOR_MINIMO_SAQUE = "Valor mínimo de saque é de R$ 10,00.";

	public static final String MSG_VALOR_SAQUE_MAIOR_QUE_ZERO = "Valor do saque pretendido deve ser maior que zero.";

	public static final String MSG_VALOR_SAQUE_OBRIGATORIO = "Valor do saque pretendido é obrigatório.";

	/**
	 * Método para tratamento de um saque
	 * @param valorPretendidoSaque
	 * @return Coleção de mensagens detalhando a entrega de cédulas no saque.
	 */
	public List<String> processarSaque(BigDecimal valorPretendidoSaque) {
		validarValorPretendidoSaque(valorPretendidoSaque);
		
		return calcularCedulas(valorPretendidoSaque);
	}
	
	/**
	 * Calcula as respectivas quantidades e tipos de cédulas para a dispensação em um saque.
	 * @param valorSaque
	 * @return
	 */
	protected List<String> calcularCedulas(BigDecimal valorSaque) {
		Integer quantidadeDeCedulas = 0;
		BigDecimal valorADecompor = new BigDecimal(valorSaque.doubleValue());
		List<String> resumos = new ArrayList<>();
		
		// verifica a quantidade de cédulas necessárias de cada tipo
		for (TipoCedulaEnum tipoCedula : TipoCedulaEnum.getDecrescentValues()) {
			if (valorADecompor.compareTo(tipoCedula.getValor()) >= 0) {
				quantidadeDeCedulas = valorADecompor.divide(tipoCedula.getValor()).abs().intValue();
				valorADecompor = valorADecompor.subtract(tipoCedula.getValor().multiply(BigDecimal.valueOf(quantidadeDeCedulas)));
				resumos.add(gerarResumo(tipoCedula, quantidadeDeCedulas));
			}
		}
		
		return resumos;
	}
	
	/**
	 * Cria o detalhamento sobre um tipo e respectiva quantidade de cédulas a serem dispensadas em um saque.
	 * @param tipoCedula
	 * @param quantidadeDeCedulas
	 * @return
	 */
	protected String gerarResumo(TipoCedulaEnum tipoCedula, Integer quantidadeDeCedulas) {
		StringBuilder resumo = new StringBuilder("Entregar ");
		
		resumo.append(quantidadeDeCedulas);
		resumo.append(" cédula(s) de R$");
		resumo.append(tipoCedula.getValorFaceCedula());
		resumo.append(",00.");

		return resumo.toString();
	}
	
	/**
	 * Efetua validações sobre o valor solicitado de saque.
	 * @param valorPretendidoSaque
	 */
	protected void validarValorPretendidoSaque(BigDecimal valorPretendidoSaque) {
		BigDecimal dez = new BigDecimal(TipoCedulaEnum.CEDULA_10.getValorFaceCedula());
		
		if (valorPretendidoSaque == null) {
			throw new BusinessException(MSG_VALOR_SAQUE_OBRIGATORIO);
		} else if (valorPretendidoSaque.signum() < 1) {
			throw new BusinessException(MSG_VALOR_SAQUE_MAIOR_QUE_ZERO);
		} else if (valorPretendidoSaque.compareTo(dez) == -1) {
			throw new BusinessException(MSG_VALOR_MINIMO_SAQUE);
		} else if (valorPretendidoSaque.remainder(dez).compareTo(BigDecimal.ZERO) != 0) {
			throw new BusinessException(MSG_VALOR_SAQUE_DEVE_SER_MULTIPLO);
		}
	}
	
}

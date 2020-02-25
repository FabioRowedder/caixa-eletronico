package com.zenvia.challenge.caixa.impl.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaixaEletronicoOperationService {
	
	@Autowired
	private SaqueService saqueService;
	
	public CaixaEletronicoOperationService(SaqueService saqueService) {
		this.saqueService = saqueService;
	}

	/**
	 * Método para encaminhamento da requisição de saque.
	 * @param valorPretendidoSaque Valor do saque.
	 * @return Coleção de mensagens detalhando a entrega de cédulas no saque.
	 */
	public List<String> saque(BigDecimal valorPretendidoSaque) {
		return saqueService.processarSaque(valorPretendidoSaque);
	}
}

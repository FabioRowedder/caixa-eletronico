package com.zenvia.challenge.caixa.impl.services;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.zenvia.challenge.caixa.arch.exceptions.handlers.BusinessException;

@RunWith(MockitoJUnitRunner.class)
public class CaixaEletronicoOperationServiceTest {

	@Mock
	private SaqueService saqueService;
	
	@InjectMocks
	private CaixaEletronicoOperationService caixaEletronicoOperationService;
	
	/**
	 * Teste de saque com sucesso
	 */
	@Test
	public void saqueSuccessfulTest() {
		// given
		BigDecimal valorPretendidoSaque = BigDecimal.valueOf(120);
		List<String> retornoArbitrado = new ArrayList<>();
		retornoArbitrado.add("Entragar 1 cédlula(s) de R$ 100,00");
		retornoArbitrado.add("Entragar 1 cédlula(s) de R$ 20,00");
		
		Mockito.when(saqueService.processarSaque(Mockito.refEq(valorPretendidoSaque))).thenReturn(retornoArbitrado);
		
		// when
		List<String> retornoObtido = caixaEletronicoOperationService.saque(valorPretendidoSaque);
		
		// then
		assertEquals(retornoArbitrado, retornoObtido);
		assertThatCode(() -> saqueService.processarSaque(valorPretendidoSaque)).doesNotThrowAnyException();
	}
	
	/**
	 * Teste de lançamento de exceção por validação de regra de negócio infringida
	 */
	@Test
	public void saqueLancandoBusinessExceptionTest() {
		// given
		BigDecimal valorPretendidoSaque = BigDecimal.valueOf(9);
		
		Mockito.doThrow(new BusinessException(SaqueService.MSG_VALOR_MINIMO_SAQUE)).when(saqueService).processarSaque(valorPretendidoSaque);
		
		// when - then
		assertThatThrownBy(() -> saqueService.processarSaque(valorPretendidoSaque))
			.isInstanceOf(BusinessException.class)
			.hasMessage(SaqueService.MSG_VALOR_MINIMO_SAQUE);
	}
}

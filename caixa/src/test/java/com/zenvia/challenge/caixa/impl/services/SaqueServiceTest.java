package com.zenvia.challenge.caixa.impl.services;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zenvia.challenge.caixa.arch.exceptions.handlers.BusinessException;
import com.zenvia.challenge.caixa.enums.TipoCedulaEnum;

@RunWith(MockitoJUnitRunner.class)
public class SaqueServiceTest {
	
	@Spy
	private SaqueService saqueService;

	/**
	 * Teste unitário para confirmar lançamento de uma BusinessException.
	 */
	@Test
	public void processarSaqueComBusinessExceptionTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(100);
		BusinessException exception = new BusinessException(SaqueService.MSG_VALOR_SAQUE_DEVE_SER_MULTIPLO);
		
		Mockito.doThrow(exception).when(saqueService).validarValorPretendidoSaque(valorSaque);
		
		// when - then
		assertThatThrownBy(() -> saqueService.processarSaque(valorSaque))
			.isInstanceOf(BusinessException.class);
	}
	
	/**
	 * Teste unitário para confirmar mensagem de erro quando valor do saque não é múltiplo de 10,00
	 */
	@Test
	public void validarValorPretendidoSaqueComValorNaoMultiploExceptionTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(105);
		
		// when - then
		assertThatThrownBy(() -> saqueService.validarValorPretendidoSaque(valorSaque))
			.isInstanceOf(BusinessException.class)
			.hasMessage(SaqueService.MSG_VALOR_SAQUE_DEVE_SER_MULTIPLO);
	}
	
	/**
	 * Teste unitário para confirmar mensagem de erro quando valor do saque é nulo
	 */
	@Test
	public void validarValorPretendidoSaqueComValorNuloExceptionTest() {
		// given
		BigDecimal valorSaque = null;
		
		// when - then
		assertThatThrownBy(() -> saqueService.validarValorPretendidoSaque(valorSaque))
			.isInstanceOf(BusinessException.class)
			.hasMessage(SaqueService.MSG_VALOR_SAQUE_OBRIGATORIO);
	}
	
	
	/**
	 * Teste unitário para confirmar mensagem de erro quando valor do saque é zero
	 */
	@Test
	public void calcularCedulasComValorZeroExceptionTest() {
		// given
		BigDecimal valorSaque = BigDecimal.ZERO;
		
		// when - then
		assertThatThrownBy(() -> saqueService.validarValorPretendidoSaque(valorSaque))
			.isInstanceOf(BusinessException.class)
			.hasMessage(SaqueService.MSG_VALOR_SAQUE_MAIOR_QUE_ZERO);
	}
	
	/**
	 * Teste unitário para confirmar mensagem de erro quando valor do saque é negativo
	 */
	@Test
	public void calcularCedulasComValorNegativoExceptionTest() {
		// given
		BigDecimal valorSaque = BigDecimal.ONE.negate();
		
		// when - then
		assertThatThrownBy(() -> saqueService.validarValorPretendidoSaque(valorSaque))
			.isInstanceOf(BusinessException.class)
			.hasMessage(SaqueService.MSG_VALOR_SAQUE_MAIOR_QUE_ZERO);
	}
	
	/**
	 * Teste unitário para confirmar mensagem de erro quando valor do saque é menor que 10,00
	 */
	@Test
	public void calcularCedulasComMenorQue10ExceptionTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(9);
		
		// when - then
		assertThatThrownBy(() -> saqueService.validarValorPretendidoSaque(valorSaque))
			.isInstanceOf(BusinessException.class)
			.hasMessage(SaqueService.MSG_VALOR_MINIMO_SAQUE);
	}
	
	/**
	 * Teste unitário de mensagem de dispensação de cédulas com sucesso.
	 */
	@Test
	public void gerarResumoTest() {
		// given
		String resumoEsperado = "Entregar 25 cédula(s) de R$100,00.";
		
		// when
		String resumoObtido = saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 25);
		
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 100,00 sem erros de validação.
	 */
	@Test
	public void processarSaqueSuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(100);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 1));
		
		// when
		List<String> resumoObtido = saqueService.processarSaque(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
		Mockito.verify(saqueService).validarValorPretendidoSaque(valorSaque);
		Mockito.verify(saqueService).calcularCedulas(valorSaque);
		
		assertThatCode(() -> saqueService.validarValorPretendidoSaque(valorSaque)).doesNotThrowAnyException();
	}
	
	/**
	 * Teste unitário para saque de 50,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor50SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(50);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_50, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 10,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor10SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(10);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_10, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 20,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor20SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(20);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 150,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor150SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(150);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_50, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 80,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor80SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(80);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_50, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_10, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 180,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor180SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(180);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_50, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_10, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 90,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor90SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(90);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_50, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 2));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 300,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor300SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(300);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 3));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 110,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor110SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(110);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_10, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 40,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor40SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(40);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 2));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 1890,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor1890SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(1890);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 18));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_50, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 2));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 1880,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor1880SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(1880);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 18));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_50, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_10, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 1030,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor1030SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(1030);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 10));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 1));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_10, 1));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
	
	/**
	 * Teste unitário para saque de 1040,00 sem erros de validação.
	 */
	@Test
	public void calcularCedulasParaValor1040SuccessfulTest() {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(1040);
		List<String> resumoEsperado = new ArrayList<>();
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_100, 10));
		resumoEsperado.add(saqueService.gerarResumo(TipoCedulaEnum.CEDULA_20, 2));
		
		// when
		List<String> resumoObtido = saqueService.calcularCedulas(valorSaque);
		
		// then
		assertEquals(resumoEsperado, resumoObtido);
	}
}

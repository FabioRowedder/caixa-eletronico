package com.zenvia.challenge.caixa.impl.rest.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.zenvia.challenge.caixa.CaixaApplication;
import com.zenvia.challenge.caixa.impl.services.SaqueService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
				classes = CaixaApplication.class,
				properties = "spring.profiles.active=dev,integration_test,h2")
public class CaixaEletronicoRestIT {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mvc;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.alwaysDo(MockMvcResultHandlers.print())
				.build();
	}
	
	/**
	 * Teste integrado de saque com sucesso
	 * @throws Exception
	 */
	@Test
	public void calcularNotasSuccessfulTest() throws Exception {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(150);
		List<String> resumoEsperado = new ArrayList<>();
		String resumo1 = "Entregar 1 cédula(s) de R$100,00.";
		String resumo2 = "Entregar 1 cédula(s) de R$50,00.";
		resumoEsperado.add(resumo1);
		resumoEsperado.add(resumo2);
		
		mvc.perform(
			get(CaixaEletronicoRest.PATH + CaixaEletronicoRest.PATH_CALCULAR_CEDULAS, valorSaque)	
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0]", is(resumo1)))
			.andExpect(jsonPath("$.[1]", is(resumo2)));
	}
	
	/**
	 * Teste integrado de saque com erro de valor mínimo
	 * @throws Exception
	 */
	@Test
	public void calcularNotasComErroDeValorMinimoParaSaqueTest() throws Exception {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(9);
		
		mvc.perform(
			get(CaixaEletronicoRest.PATH + CaixaEletronicoRest.PATH_CALCULAR_CEDULAS, valorSaque)	
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
			.andExpect(content().string(containsString(SaqueService.MSG_VALOR_MINIMO_SAQUE)));
	}
	
	/**
	 * Teste integrado de saque com erro de valor não múltiplo de 10,00
	 * @throws Exception
	 */
	@Test
	public void calcularNotasComErroDeValorSaqueNaoMultiploDe10Test() throws Exception {
		// given
		BigDecimal valorSaque = BigDecimal.valueOf(109);
		
		mvc.perform(
			get(CaixaEletronicoRest.PATH + CaixaEletronicoRest.PATH_CALCULAR_CEDULAS, valorSaque)	
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
			.andExpect(content().string(containsString(SaqueService.MSG_VALOR_SAQUE_DEVE_SER_MULTIPLO)));
	}
	
	/**
	 * Teste integrado de saque com erro de valor zero
	 * @throws Exception
	 */
	@Test
	public void calcularNotasComErroDeValorDeSaqueZeroTest() throws Exception {
		// given
		BigDecimal valorSaque = BigDecimal.ZERO;
		
		mvc.perform(
			get(CaixaEletronicoRest.PATH + CaixaEletronicoRest.PATH_CALCULAR_CEDULAS, valorSaque)	
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()))
			.andExpect(content().string(containsString(SaqueService.MSG_VALOR_SAQUE_MAIOR_QUE_ZERO)));
	}	
}

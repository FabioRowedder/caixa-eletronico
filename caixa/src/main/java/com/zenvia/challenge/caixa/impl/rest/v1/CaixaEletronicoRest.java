package com.zenvia.challenge.caixa.impl.rest.v1;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zenvia.challenge.caixa.impl.services.CaixaEletronicoOperationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = CaixaEletronicoRest.PATH)
@Api(value = "API de atendimento de requisições de caixa eletrônico")
@CrossOrigin(origins = "*")
public class CaixaEletronicoRest {
	
	public static final String PATH = "/caixa-eletronico/api/operacao/v1";
	
	public static final String PATH_CALCULAR_CEDULAS = "/calcula-cedulas/{valor-saque}";
	
	private CaixaEletronicoOperationService operationService;
	
	@Autowired
	public CaixaEletronicoRest(CaixaEletronicoOperationService operationService) {
		this.operationService = operationService;
	}

	@GetMapping(value = CaixaEletronicoRest.PATH_CALCULAR_CEDULAS)
	@ApiOperation(value = "Determina a composição do valor de um saque no caixa eletrônico (quantidade e valor de face das cédulas).")
	public ResponseEntity<List<String>> calcularCedulas(
			@PathVariable(value = "valor-saque") 
			@ApiParam(value = "Valor pretendido do saque.", required = true) 
			BigDecimal valorSaque) {
		
		return ResponseEntity.ok(operationService.saque(valorSaque));
	}
}

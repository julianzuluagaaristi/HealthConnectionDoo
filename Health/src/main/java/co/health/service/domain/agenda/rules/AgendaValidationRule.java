package co.health.service.domain.agenda.rules;

import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.util.UtilObjeto;
import co.health.service.domain.ValidationRule;
import co.health.service.domain.agenda.AgendaDomain;

public class AgendaValidationRule implements ValidationRule<AgendaDomain> {

	private static final ValidationRule<AgendaDomain> instancia = new AgendaValidationRule();
	
	private  AgendaValidationRule() {
		super();
	}
	
	public static final void ejecutarValidacion(final AgendaDomain dato) {
		instancia.validar(dato);
	}

	@Override
	public final void validar(final AgendaDomain dato) {
		if(UtilObjeto.esNulo(dato)) {
			var mensajeUsuario = "No es posible realizar la operacion con una agenda nula";
			throw ServiceHealthException.crear(mensajeUsuario);
		}
		
	}
}

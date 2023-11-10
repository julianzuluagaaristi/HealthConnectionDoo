package co.health.service.domain.agenda.rules;

import java.sql.Date;
import java.time.LocalDateTime;

import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.util.UtilDate;
import co.health.crosscutting.util.UtilObjeto;
import co.health.service.domain.ValidationRule;

public class FechaFinAgendaValidationRule implements ValidationRule<LocalDateTime>{
	private static final ValidationRule<LocalDateTime> instancia = new FechaFinAgendaValidationRule();
	
	private FechaFinAgendaValidationRule() {
		super();
	}
	
	public static final void ejecutarValidacion(final LocalDateTime dato) {
		instancia.validar(dato);
	}


	@Override
	public final void validar(final LocalDateTime dato) {
		validarObligatoriedad(dato);
		validarFormato(dato);
		
	}

	private final void validarObligatoriedad(final LocalDateTime dato) {
		if(UtilObjeto.esNulo(dato)) {
			var mensajeUsuario = "La fecha de inicio de la agenda es un dato Obligatorio";
			throw ServiceHealthException.crear(mensajeUsuario);
		}
	}

	private final void validarFormato(final LocalDateTime dato) {
		if(!UtilDate.validarFecha(dato)) {
			var mensajeUsuario = "La fecha no cumple el formato.La fecha debe tener el formato yyyy-MM-dd";
			throw ServiceHealthException.crear(mensajeUsuario);
		}
	}
	
}

package co.health.service.domain.profesionalsalud.rules;

import java.util.UUID;

import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.util.UtilUuid;
import co.health.service.domain.ValidationRule;

public final class IdTipoServicioValidationRule implements ValidationRule<UUID>{

	
	private static final ValidationRule<UUID> instancia = new IdTipoServicioValidationRule();
	
	private IdTipoServicioValidationRule() {
		super();
	}
	
	public static final void ejecutarValidacion(final UUID dato) {
		instancia.validar(dato);
	}
	
	@Override
	public void validar(UUID dato) {
		if(UtilUuid.valorDefecto(dato)) {
			var mensajeUsuario = "El id del Tipo de servicio es el valor por defecto";
			throw ServiceHealthException.crear(mensajeUsuario);
		}
	}

}

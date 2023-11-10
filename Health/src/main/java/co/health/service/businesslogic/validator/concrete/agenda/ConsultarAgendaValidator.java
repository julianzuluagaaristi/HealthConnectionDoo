package co.health.service.businesslogic.validator.concrete.agenda;

import co.health.service.businesslogic.validator.Validator;
import co.health.service.domain.agenda.AgendaDomain;
import co.health.service.domain.agenda.rules.AgendaValidationRule;
public class ConsultarAgendaValidator implements Validator<AgendaDomain>{

	private static final Validator<AgendaDomain> instancia = new ConsultarAgendaValidator();
	
	private  ConsultarAgendaValidator() {
		super();
	}
	
	public static final void ejecutarValidacion(final AgendaDomain data) {
		instancia.execute(data);
	}
	
	@Override
	public void execute(final AgendaDomain dato) {
		AgendaValidationRule.ejecutarValidacion(dato);
	} 

}

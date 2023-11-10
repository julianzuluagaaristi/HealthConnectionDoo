package co.health.service.businesslogic.concrete.agenda;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.util.UtilObjeto;
import co.health.data.dao.AgendaDAO;
import co.health.data.dao.daofactory.DAOFactory;
import co.health.data.entity.AgendaEntity;
import co.health.service.businesslogic.UseCase;
import co.health.service.businesslogic.validator.concrete.agenda.RegistrarAgendaValidator;
import co.health.service.domain.agenda.AgendaDomain;
import co.health.service.domain.paciente.support.NombreCompletoDomain;
import co.health.service.domain.profesionalsalud.ProfesionalSaludDomain;
import co.health.service.mapper.entity.concrete.AgendaEntityMapper;

public final class RegistrarAgendaUSeCase implements UseCase<AgendaDomain>{

	private DAOFactory factoria;

	public RegistrarAgendaUSeCase(final DAOFactory factoria) {
		setFactoria(factoria);
	}
	
	@Override
	public final void execute( AgendaDomain domain) {
		
		RegistrarAgendaValidator.ejecutarValidacion(domain);
		
		validarNoExistenciaConMismaFechaInicioFechaFinProfesional(domain.getFechaInicio()
				,domain.getFechaFin(),domain.getProfesionalSalud().getNombreCompletoProfesionalSalud().getPrimerNombre());
		
		domain = obtenerIdentificadorAgenda(domain);
		
		registrarNuevaCita(domain);
	}

	private void registrarNuevaCita(final AgendaDomain domain) {
		var entity = AgendaEntityMapper.convertToEntity(domain);
		getAgendaDAO().registrarCita(entity);
	}

	private void validarNoExistenciaConMismaFechaInicioFechaFinProfesional(final LocalDateTime fechaInicio, final LocalDateTime fechaFin,
			final String primerNombre) {
		var entity = crearPacienteFechaInicioFechaFinProfesional(fechaInicio, fechaFin, primerNombre);
		var resultados = getAgendaDAO().consultar(entity);
		if(!resultados.isEmpty()) {
			 String mensajeUsuario = "Ya existe una cita en la misma fecha, y con el mismo profesional de salud";
		        throw ServiceHealthException.crear(mensajeUsuario);
		}
		
		
	}
	
	private AgendaEntity crearPacienteFechaInicioFechaFinProfesional(final LocalDateTime fechaInicio, final LocalDateTime fechaFin,
			final String primerNombre) {
		var domainNombre = NombreCompletoDomain.crear(primerNombre, null, null, null);
		var domainProfesional = ProfesionalSaludDomain.crear(null, null, domainNombre, null, null);
		var domain = AgendaDomain.crear(null, domainProfesional, 0 , fechaInicio, fechaFin);
	 return AgendaEntityMapper.convertToEntity(domain);
	}
	
	private AgendaDomain obtenerIdentificadorAgenda(final AgendaDomain domain) {

		Optional<AgendaEntity> optional;
		UUID uuid;
		do {
			uuid = UUID.randomUUID();
			//optional = getAgendaDAO().consultarPorId(uuid);
		//}while(optional.isPresent());
		}while(uuid==null);
		
		return AgendaDomain.crear(uuid,domain.getProfesionalSalud(), domain.getPrecioServicio(),
				domain.getFechaInicio(), domain.getFechaFin());
	}
	
	private final DAOFactory getFactoria() {
		return factoria;
	}
	
	private final AgendaDAO getAgendaDAO() {
		return getFactoria().obtenerAgendaDAO();
	}

	private final void setFactoria(final DAOFactory factoria) {
		if(UtilObjeto.esNulo(factoria)) {
			var mensajeUsuario = "Se ha presentado un problema tratando de llevar a cabo el "
					+ "registro de la información de la nueva cita de la agenda";//CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000029);
			var mensajeTecnico = "Se ha presentado un problema en el metodo setFactoria de la clase"
					+ " RegistrarAgendaUSeCase debido a que la factoria con la cual se desea crear esta nula.";//CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000030);
			throw ServiceHealthException.crear(mensajeUsuario, mensajeTecnico);
		}
		this.factoria = factoria;
	}

}

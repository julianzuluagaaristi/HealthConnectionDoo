package co.health.service.businesslogic.concrete.paciente;

import java.util.Optional;
import java.util.UUID;

import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.util.UtilObjeto;
import co.health.data.dao.PacienteDAO;
import co.health.data.dao.daofactory.DAOFactory;
import co.health.data.entity.PacienteEntity;
import co.health.service.businesslogic.UseCase;
import co.health.service.businesslogic.validator.concrete.paciente.RegistrarPacienteValidator;
import co.health.service.domain.paciente.PacienteDomain;
import co.health.service.domain.paciente.support.ContactoPacienteDomain;
import co.health.service.domain.paciente.support.CorreoElectronicoPacienteDomain;
import co.health.service.domain.paciente.support.NumeroTelefonoPacienteDomain;
import co.health.service.domain.tipoidentificacion.TipoIdentificacionDomain;
import co.health.service.mapper.entity.concrete.PacienteEntityMapper;

public final class RegistrarPacienteUseCase implements UseCase<PacienteDomain>{

	private DAOFactory factoria;

	public RegistrarPacienteUseCase(final DAOFactory factoria) {
		setFactoria(factoria);
	}
	
	@Override
	public void execute(PacienteDomain domain) {
		//System.out.println()
		
		RegistrarPacienteValidator.ejecutarValidacion(domain);

		validarNoExistenciaPacienteConMismoIdentificacionYTipo(domain.getNumeroIdentificacion(),domain.getTipoIdentificacion().getNombre());
		
		
		validarNoExistenciaPacienteConMismoNumeroTelefono(domain.getContactoPaciente().getNumeroTelefonoPaciente());
		
		
		validarNoExistenciaPacienteConMismoCorreoElectronico(domain.getContactoPaciente().getCorreoElectronicoPaciente());

		
		domain = obtenerIdentificadorCliente(domain);

		registrarNuevoCliente(domain);
		
		
	}
	
	private void registrarNuevoCliente(final PacienteDomain domain ) {
		var entity = PacienteEntityMapper.convertToEntity(domain);
		getPacienteDAO().registar(entity);
		
	}
	
	private final void validarNoExistenciaPacienteConMismoIdentificacionYTipo(final String identificacion,final String codigo) {
		 var entity = crearPacienteEntityIdentificacion(identificacion,codigo);
		    var resultados = getPacienteDAO().consultar(entity);
		    if (!resultados.isEmpty()) {
		        String mensajeUsuario = "Ya existe cliente con el número de identificación:";
		        throw ServiceHealthException.crear(mensajeUsuario);
		    }
	}
	private final PacienteEntity crearPacienteEntityIdentificacion(final String identificacion,final String codigo) {
		var domainTipo = TipoIdentificacionDomain.crear(null, codigo,null);
	    var domain = PacienteDomain.crear(null, identificacion, null, null, null, domainTipo, null);
	    return PacienteEntityMapper.convertToEntity(domain);
	}
	
	private final void validarNoExistenciaPacienteConMismoCorreoElectronico(final CorreoElectronicoPacienteDomain correo) {
		 var entity = crearPacienteEntityCorreoElectronico(correo);
		    var resultados = getPacienteDAO().consultar(entity);
		    
		    if (!resultados.isEmpty()) {
		        String mensajeUsuario = "Ya existe cliente con el correo Electronico o Número de telefono. Por favor revise de"
		        		+ "nuevo los datos ingresados ";
		        throw ServiceHealthException.crear(mensajeUsuario);
		    }
	}
	private PacienteEntity crearPacienteEntityCorreoElectronico(final CorreoElectronicoPacienteDomain correo) {
	    ContactoPacienteDomain domain = ContactoPacienteDomain.crear(correo, null, null);
	    var domainPa = PacienteDomain.crear(null, null, null, domain, null, null, null);
	    return PacienteEntityMapper.convertToEntity(domainPa);
	}
	
	private final void validarNoExistenciaPacienteConMismoNumeroTelefono(final NumeroTelefonoPacienteDomain telefono) {
		var entity = crearPacienteEntityNumeroTelefono(telefono);
		    var resultados = getPacienteDAO().consultar(entity);
		    
		    if (!resultados.isEmpty()) {
		        String mensajeUsuario = "Ya existe cliente con el correo Electronico o Número de telefono. Por favor revise de"
		        		+ "nuevo los datos ingresados ";
		        throw ServiceHealthException.crear(mensajeUsuario);
		    }
	}
	private PacienteEntity crearPacienteEntityNumeroTelefono(final NumeroTelefonoPacienteDomain telefono) {
		ContactoPacienteDomain domain = ContactoPacienteDomain.crear(null, telefono, null);
		var domainPa = PacienteDomain.crear(null, null, null, domain, null, null, null);
	    return PacienteEntityMapper.convertToEntity(domainPa);
	}
	
	private final PacienteDomain obtenerIdentificadorCliente(final PacienteDomain domain) {
		
		Optional<PacienteEntity> optional;
		UUID uuid;
		do {
			uuid = UUID.randomUUID();
			optional = getPacienteDAO().consultarPorId(uuid);
		}while(optional.isPresent());
		
		return PacienteDomain.crear(uuid, domain.getNumeroIdentificacion(), domain.getNombreCompletoPaciente(), domain.getContactoPaciente(),
				domain.getFechaNacimiento(), domain.getTipoIdentificacion(), domain.getInformacionAfiliacionPaciente());
	}
	
	private final DAOFactory getFactoria() {
		return factoria;
	}
	
	private final PacienteDAO getPacienteDAO() {
		return getFactoria().obtenerPacienteDAO();
	}

	private final void setFactoria(final DAOFactory factoria) {
		if(UtilObjeto.esNulo(factoria)) {
			var mensajeUsuario = "Se ha presentado un problema tratando de llevar a cabo el "
					+ "registro de la información del nuevo cliente";//CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000029);
			var mensajeTecnico = "Se ha presentado un problema en el metodo setFactoria de la clase"
					+ " RegistrarPacienteUseCase debido a que la factoria con la cual se desea crear esta nula.";//CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000030);
			throw ServiceHealthException.crear(mensajeUsuario, mensajeTecnico);
		}
		this.factoria = factoria;
	}
}

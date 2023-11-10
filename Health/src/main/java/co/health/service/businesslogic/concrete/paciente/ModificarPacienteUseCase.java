package co.health.service.businesslogic.concrete.paciente;

import java.util.UUID;

import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.util.UtilObjeto;
import co.health.data.dao.PacienteDAO;
import co.health.data.dao.daofactory.DAOFactory;
import co.health.data.entity.PacienteEntity;
import co.health.service.businesslogic.UseCase;
import co.health.service.businesslogic.validator.concrete.paciente.ModificarPacienteValidador;
import co.health.service.domain.paciente.PacienteDomain;
import co.health.service.mapper.entity.concrete.PacienteEntityMapper;

public final class ModificarPacienteUseCase implements UseCase<PacienteDomain>{

	private DAOFactory factoria;
	
	public ModificarPacienteUseCase(final DAOFactory factoria) {
		setFactory(factoria);
	}
	
	@Override
	public final void execute(final PacienteDomain domain) {
		
		ModificarPacienteValidador.ejecutarValidacion(domain);
		
		validarExistenciaClienteAModificar(domain.getId());
		
		modificarPaciente(domain);
		
	}
	
	private void modificarPaciente(final PacienteDomain domain) {
		var entity = PacienteEntityMapper.convertToEntity(domain);
		getPacienteDAO().modificar(entity);
	}

	private final void validarExistenciaClienteAModificar(final UUID id) {
	    var entity = crearPacienteEntityIdAModificar(id);
	    System.out.println("soy tu id:"+ entity.getId());
	    var resultados = getPacienteDAO().consultarPorId(entity.getId());
	    if (resultados.isEmpty()) {
	        String mensajeUsuario = "No existe el paciente que se desea modificar";
	        throw ServiceHealthException.crear(mensajeUsuario);
	    }
	}

	private PacienteEntity crearPacienteEntityIdAModificar(final UUID id) {
	    var domain = PacienteDomain.crear(id, null, null, null, null, null, null);
	    return PacienteEntityMapper.convertToEntity(domain);
	}
	
	private final PacienteDAO getPacienteDAO() {
		return getFactoria().obtenerPacienteDAO();
	}

	public final DAOFactory getFactoria() {
		return factoria;
	}

	public final void setFactory(final DAOFactory factoria) {
		if(UtilObjeto.esNulo(factoria)) {
			var mensajeUsuario = "";//CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000117);
			var mensajeTecnico = "";//CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000118);
			throw ServiceHealthException.crear(mensajeUsuario, mensajeTecnico);
		}
		this.factoria = factoria;
	}
	
	

}

package co.health.service.businesslogic.concrete.agenda;

import java.util.List;

import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.messages.CatalogoMensajes;
import co.health.crosscutting.messages.enumerator.CodigoMensaje;
import co.health.crosscutting.util.UtilObjeto;
import co.health.data.dao.AgendaDAO;
import co.health.data.dao.daofactory.DAOFactory;
import co.health.data.entity.AgendaEntity;
import co.health.service.businesslogic.UseCaseRetorno;
import co.health.service.domain.agenda.AgendaDomain;
import co.health.service.mapper.entity.concrete.AgendaEntityMapper;

public class ConsultarAgendaUseCase implements UseCaseRetorno<AgendaDomain,List<AgendaEntity>>{

	private DAOFactory factoria;

	public ConsultarAgendaUseCase(final DAOFactory factoria) {
		setFactoria(factoria);
	}
	
	
	@Override
	public final List<AgendaEntity> executeRetorno(final AgendaDomain domain) {
		var entity = crearAgendaEntityIdAConsultar(domain);
	    var resultados = getAgendaDAO().consultar(entity);
	    if (resultados.isEmpty()) {
	        throw ServiceHealthException.crear(CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000138));
	    }
	    return resultados;
	}
	private AgendaEntity crearAgendaEntityIdAConsultar(final AgendaDomain domain) {
	    return AgendaEntityMapper.convertToEntity(domain);
	}
	
	private final DAOFactory getFactoria() {
		return factoria;
	}
	
	private final AgendaDAO getAgendaDAO() {
		return getFactoria().obtenerAgendaDAO();
	}

	private final void setFactoria(final DAOFactory factoria) {
		if(UtilObjeto.esNulo(factoria)) {
			throw ServiceHealthException.crear(CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000126),
					CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000127));
		}
		this.factoria = factoria;
	} 

}

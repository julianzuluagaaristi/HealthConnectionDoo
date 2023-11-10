package co.health.service.facade.concrete.agenda;

import java.util.ArrayList;
import java.util.List;

import co.health.crosscutting.exception.HealthException;
import co.health.crosscutting.exception.concrete.ServiceHealthException;
import co.health.crosscutting.messages.CatalogoMensajes;
import co.health.crosscutting.messages.enumerator.CodigoMensaje;
import co.health.data.dao.daofactory.DAOFactory;
import co.health.data.dao.daofactory.TipoDAOFactory;
import co.health.data.entity.AgendaEntity;
import co.health.data.entity.PacienteEntity;
import co.health.service.businesslogic.concrete.agenda.ConsultarAgendaUseCase;
import co.health.service.businesslogic.concrete.paciente.ConsultarPacienteUseCase;
import co.health.service.domain.agenda.AgendaDomain;
import co.health.service.domain.agenda.rules.AgendaValidationRule;
import co.health.service.domain.paciente.PacienteDomain;
import co.health.service.domain.paciente.rules.PacienteValidationRule;
import co.health.service.dto.AgendaDTO;
import co.health.service.dto.PacienteDTO;
import co.health.service.facade.FacadeRetorno;
import co.health.service.mapper.dto.concrete.AgendaDTOMapper;
import co.health.service.mapper.dto.concrete.PacienteDTOMapper;

public class ConsultarAgendaFacade implements FacadeRetorno<AgendaDTO,List<AgendaEntity>> {

    @Override
    public List<AgendaEntity> executeRetorno(final AgendaDTO dto) {
        List<AgendaEntity> resultados = new ArrayList<>();

        final AgendaDomain domain = AgendaDTOMapper.convertToDomain(dto);
        AgendaValidationRule.ejecutarValidacion(domain);

        final DAOFactory daoFactory = DAOFactory.obtenerDAOFactory(TipoDAOFactory.SQLSERVER);

        try {
            daoFactory.iniciarTransaccion();

            var useCase = new ConsultarAgendaUseCase(daoFactory);
            resultados = useCase.executeRetorno(domain);

            daoFactory.confirmarTransaccion();
        } catch (final HealthException exception) {
            daoFactory.cancelarTransacion();
            throw exception;
        } catch (final Exception exception) {
            daoFactory.cancelarTransacion();
            throw ServiceHealthException.crear(CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000138),
            		CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000139), exception);
        } finally {
            daoFactory.cerrarConexion();
        }
        return resultados;
    }

}

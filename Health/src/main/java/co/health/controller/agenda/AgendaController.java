package co.health.controller.agenda;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.health.controller.concrete.response.Respuesta;
import co.health.crosscutting.exception.HealthException;
import co.health.crosscutting.messages.CatalogoMensajes;
import co.health.crosscutting.messages.enumerator.CodigoMensaje;
import co.health.data.entity.AgendaEntity;
import co.health.data.entity.PacienteEntity;
import co.health.service.dto.AgendaDTO;
import co.health.service.dto.PacienteDTO;
import co.health.service.facade.concrete.agenda.ConsultarAgendaFacade;
import co.health.service.facade.concrete.agenda.RegistrarAgendaFacade;
import co.health.service.facade.concrete.paciente.ConsultarPacienteFacade;

@RestController
@RequestMapping("/api/v1/agenda")
public final class AgendaController {
	
	@GetMapping("/dummy")
	public final AgendaDTO obtenerDummy() {
		return AgendaDTO.crear();
	}
	
	@PostMapping()
	public final ResponseEntity<Respuesta<AgendaDTO>> registrar(@RequestBody AgendaDTO dto) {
		
		final Respuesta<AgendaDTO> respuesta = new Respuesta<>();
		
		HttpStatus codigoHttp = HttpStatus.BAD_REQUEST;
		
		try {
			
			RegistrarAgendaFacade facade = new RegistrarAgendaFacade();
			facade.execute(dto);
			codigoHttp = HttpStatus.OK;
			respuesta.getMensajes().add(CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000140));
			
		} catch (final HealthException excepcion) {
			respuesta.getMensajes().add(excepcion.getMensajeUsuario());
			System.err.println(excepcion.getMensajeTecnico());
			System.err.println(excepcion.getLugar());
			excepcion.getRaizExcepcion().printStackTrace();
			
		}catch (final Exception excepcion) {
			respuesta.getMensajes().add(CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000141));
			excepcion.printStackTrace();
		}
		return new ResponseEntity<>(respuesta,codigoHttp);
	}
	
	@PostMapping("/consultar")
	public final ResponseEntity<Respuesta<AgendaEntity>> consultarLo(@RequestBody AgendaDTO dto) {
		
		final Respuesta<AgendaEntity> respuesta = new Respuesta<>();
		
		HttpStatus codigoHttp = HttpStatus.BAD_REQUEST;
		
		
		try {
			ConsultarAgendaFacade facade = new ConsultarAgendaFacade();
			
			respuesta.setDatos(facade.executeRetorno(dto));
			respuesta.getDatos();
			codigoHttp = HttpStatus.OK;
			respuesta.getMensajes().add(CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000143));
			
		} catch (final HealthException excepcion) {
			respuesta.getMensajes().add(excepcion.getMensajeUsuario());
			System.err.println(excepcion.getMensajeTecnico());
			System.err.println(excepcion.getLugar());
			excepcion.getRaizExcepcion().printStackTrace();
			//TODO: hacer logger de la excepcion
			
		}catch (final Exception excepcion) {
			respuesta.getMensajes().add(CatalogoMensajes.obtenerContenidoMensaje(CodigoMensaje.M0000142));
			excepcion.printStackTrace();
			//TODO: hacer logger de la excepcion
		}
		return new ResponseEntity<>(respuesta,codigoHttp);
	}

}

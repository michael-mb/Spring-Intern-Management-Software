package kickstart.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Internetzugriffskontrolleinheit für das Auftreten eines Fehlers
 */
@Controller
public class ErrorControllerImpl implements ErrorController  {

	/**
	 * Neudefinieren der Fehlerbehandlung
	 * @param request Eingehende Anfrage
	 * @return Html-Template des jeweiligen Fehlers
	 */
	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
	   Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	
	   if (status != null) {
	       Integer statusCode = Integer.valueOf(status.toString());
	       
	       if(statusCode == HttpStatus.NOT_FOUND.value()) {
	           return "error404";
	       }else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	           return "error500";
	       }
	   }
	   return "error";
	}

	/**
	 * Neudefinieren der Fehlerweiterleitung
	 * @return Weiterleitung zur eigenen Fehlerseite
	 */
	@Override
	public String getErrorPath() {
		return "/error";
	}
}

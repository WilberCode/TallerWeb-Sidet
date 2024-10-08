package com.ibero.demo.controller;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ibero.demo.entity.Employee;
import com.ibero.demo.entity.UserEntity;
import com.ibero.demo.service.EmailService;
import com.ibero.demo.service.IPeopleService;
import com.ibero.demo.service.IUserService;
import com.ibero.demo.util.EmailValuesDTO;

@Controller
public class ControllerMaster {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IPeopleService peopleService;

	@Autowired
	private IUserService userService;

	@Autowired
	private EmailService emailservice;
	
	//configurar el emisor 
	@Value("${spring.mail.username}")
	private String mailFrom;
	//asunto del email
	private static final String subject = "Credencial Temporal de Acceso";

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping(value = "/login")
	public String showLoginForm(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal) {
		model.addAttribute("titlepage", "©Registrex");
		if (error != null) {model.addAttribute("error", "¡Error al intentar iniciar sesión!");}

		if (logout != null) {model.addAttribute("success", "¡Cerro sesión con exito!");	}
		
		if (principal != null) {return "redirect:/index";}
		return "/login";
	}
	
	@GetMapping(value = "/")
	public String showIndex(Model model) {
		model.addAttribute("titlepage", "©Registrex");
		return "/index";
	}

	@PostMapping("/send-email") //
	public String sendRestartPass(@RequestParam("mailTo") String mailTo, RedirectAttributes flash) {
		logger.info("mailTo :" + mailTo);
		Optional<Employee> usuarioOpt = peopleService.findByEmailPeople(mailTo);
		if (usuarioOpt.isPresent()) {
			Employee empl = usuarioOpt.get();
			UserEntity user = userService.findOneUser(empl.getUserEntity().getId());
			if(user == null) {
				logger.info("Id" + empl.getId() +" No encontrado" + user.getId());
			}else {
			// Genera una nueva contraseña aleatoria
			String newPassword = generateRandomPassword(8);
			//encriptación de la nueva contraseña
			user.setUserPassword(passwordEncoder.encode(newPassword));
			user.setTemporaryPassword(true);
			userService.save(user);
			//Anotar el De Para Asunto del Correo
			EmailValuesDTO dto = new EmailValuesDTO();
			dto.setMailFrom(mailFrom);//De
			dto.setMailTo(empl.getEmailPeople());//Para
			dto.setSubject(subject);//Asunto
			//Se necesita enviar el usuario propietario del correo
			dto.setUserName(user.getUserName());
			//Contraseña temporal 
			dto.setNewuserpass(newPassword);
			//para el nombre del empleado
			dto.setEmployeename(empl.getFullName());
			flash.addFlashAttribute("success", "Credenciales enviado a sus correo electronico");
			// Envía la nueva contraseña al correo electrónico
			logger.info("Credenciales enviado a sus correo electronico");
			emailservice.sendEmail(dto);
			return "redirect:/login";
			}
		} else {
			// Mensaje de error si no se encuentra el email
			logger.error("No existe ninguna coincidencia con el correo proporcionado");
			flash.addFlashAttribute("error", "No existe ninguna coincidencia con el correo proporcionado");
			return "redirect:/login";
		}
		
		return "redirect:/login";
	}

	// generar contraseña aleatoria
	private String generateRandomPassword(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*(),.?\":{}|<>";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

}

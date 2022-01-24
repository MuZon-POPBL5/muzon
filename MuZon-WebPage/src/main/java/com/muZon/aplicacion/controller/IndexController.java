package com.muZon.aplicacion.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.muZon.aplicacion.entity.GrafanaMetrics;
import com.muZon.aplicacion.entity.Role;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.exception.CustomeFieldValidationException;
import com.muZon.aplicacion.repository.RoleRepository;
import com.muZon.aplicacion.service.GrafanaService;
import com.muZon.aplicacion.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {

    @Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

    @Autowired
	GrafanaService grafanaService;

    @GetMapping({ "/", "/login" })
	public String index() {
		return "index";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		Role userRole = roleRepository.findByName("USER");
		List<Role> roles = Arrays.asList(userRole);

		model.addAttribute("signup", true);
		model.addAttribute("userForm", new User());
		model.addAttribute("roles", roles);

		return "user-form/user-signup";
	}

	@GetMapping("/signin")
	public String signin(Model model) {
		return "index";
	}

	@PostMapping("/signup")
	public String signupAction(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
		Role userRole = roleRepository.findByName("USER");
		List<Role> roles = Arrays.asList(userRole);
		model.addAttribute("userForm", user);
		model.addAttribute("roles", roles);
		model.addAttribute("signup", true);
		Date td = new Date();
		user.setCreationDate(td);
		try {
			userService.createUser(user);
		} catch (CustomeFieldValidationException cfve) {
			result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
		} catch (Exception e) {
			model.addAttribute("formErrorMessage", e.getMessage());
		}

		return index();
	}
    
    @GetMapping("/loginCount")
	public String loginCount(Model model) throws Exception {
		GrafanaMetrics metricsGraf = new GrafanaMetrics();
		Date today = new Date();
		metricsGraf.setSqlTimestamp(today);
		Integer numLogs = 1;
		metricsGraf.setNumLogins(numLogs);
		grafanaService.saveGrafanaMetrics(metricsGraf);

		return "redirect:/userForm";
	}
}

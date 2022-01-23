package com.muZon.aplicacion.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.dto.ChangePasswordForm;
import com.muZon.aplicacion.entity.Address;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.repository.AddressRepository;
import com.muZon.aplicacion.repository.CartRepository;
import com.muZon.aplicacion.repository.ProductRepository;
import com.muZon.aplicacion.repository.RoleRepository;
import com.muZon.aplicacion.service.AddressService;
import com.muZon.aplicacion.service.GrafanaService;
import com.muZon.aplicacion.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SettingsController {

    final static int SET_DEFAULT = 1;
	final static int SET_NOT_DEFAULT = 0;
    
	@Autowired
	UserService userService;

	@Autowired
	GrafanaService grafanaService;

	@Autowired
	AddressService addressService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	AddressRepository addressRepository;


    
	@PostMapping("/editUser/changePassword")
	public ResponseEntity<?> postEditUseChangePassword(@Valid @RequestBody ChangePasswordForm form, Errors errors) {
		try {
			if (errors.hasErrors()) {
				String result = errors.getAllErrors()
						.stream().map(x -> x.getDefaultMessage())
						.collect(Collectors.joining(""));

				throw new Exception(result);
			}
			userService.changePassword(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}

	@PostMapping("/editUser/changePassword/{id}")
	public String postEditUseChangePassword(@Valid @ModelAttribute("passwordForm") ChangePasswordForm form) {
		try {
			userService.changePassword(form);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/userForm";
	}

	@PostMapping("/editUser/changeEmail/{id}")
	public String postEditUseChangeEmail(
			@PathVariable(name = "id") Long id, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			userService.changeEmail(userService.getUserById(id), request.getParameter("email"));
		} catch (Exception e) {
			return e.getMessage();
		}
		return "redirect:/userForm";
	}

	@PostMapping("/editUser/addAddress")
	public ResponseEntity<?> postAddAddress(@Valid @RequestBody ChangeAddressForm form, Errors errors) {
		try {
			if (errors.hasErrors()) {
				String result = errors.getAllErrors()
						.stream().map(x -> x.getDefaultMessage())
						.collect(Collectors.joining(""));

				throw new Exception(result);
			}
			addressService.addAddress(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}

	@GetMapping("/setDefault/{id}")
	public String setDefault(Model model, @PathVariable(name = "id") Long id) {
		Optional<Address> address = addressRepository.findById(id);
		Iterable<Address> addressList = addressRepository.findAll();

		for(Address addresses : addressList){
			if(addresses.getId() != address.get().getId()){
				addresses.setDefaultAddress(SET_NOT_DEFAULT);
				addressService.saveChanges(addresses, addresses.getId());
			}
		}

		address.get().setDefaultAddress(SET_DEFAULT);
		addressService.saveChanges(address.get(), id);

		return "redirect:/userForm";
	}

    
	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable(name = "id") Long id) {

		addressService.delete(id);

		return "redirect:/userForm";
	}

    @GetMapping("/accountSettings/{id}")
	public String editAccountSettings(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User userToEdit = userService.getUserById(id);

		model.addAttribute("user", userToEdit);
		model.addAttribute("editMode", "true");
		model.addAttribute("passwordForm", new ChangePasswordForm(id));

		return "user-form/accountSettings";
	}

    @GetMapping("/selectAddress/{id}")
	public String getShowAddressForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User user = userService.getUserById(id);

		List<Address> addresses = addressRepository.findByUser(user);

		model.addAttribute("user", user);
		model.addAttribute("addressList", addresses);
		model.addAttribute("addressForm", new ChangeAddressForm(id));
		model.addAttribute("editMode", "true");

		return "user-form/address-form";
	}

    @GetMapping("/addressForm/cancel")
	public String backFromShowAddress(ModelMap model) {
		return "redirect:/userForm";
	}

    @GetMapping("/addressForm/add/{id}")
	public String addAddressForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User user = userService.getUserById(id);

		model.addAttribute("userAddress", user);
		model.addAttribute("addressActive", "true");
		model.addAttribute("addressForm", new ChangeAddressForm(id));

		model.addAttribute("userForm", user);
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("formTab", "active");
		// model.addAttribute("editMode", "true");
		// model.addAttribute("passwordForm", new ChangePasswordForm(id));

		return "user-form/address-form";
		// return "user-form/user-view";
	}
}

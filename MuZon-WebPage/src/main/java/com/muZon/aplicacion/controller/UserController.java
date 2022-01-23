package com.muZon.aplicacion.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.dto.ChangePasswordForm;
import com.muZon.aplicacion.entity.Address;
import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.GrafanaMetrics;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.repository.CartRepository;
import com.muZon.aplicacion.repository.ProductRepository;
import com.muZon.aplicacion.repository.RoleRepository;
import com.muZon.aplicacion.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@GetMapping("/userForm")
	public String userForm(Model model) throws Exception {
		List<Product> productList = new ArrayList<>();
		List<Cart> cartList = new ArrayList<>();

		model.addAttribute("userForm", new User());
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("listTab", "active");
		model.addAttribute("userTab", "active");
		model.addAttribute("productForm", new Product());

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

		Iterable<Product> pr = productRepository.findAll();
		Iterable<Cart> cr = cartRepository.findAll();

		for (Product product : pr) {
			if (product.getSellerId().getId() != (user.get().getId())) {
				productList.add(product);
			}
		}

		for (Cart cart : cr) {
			if (cart.getBoughtId().getId() == (user.get().getId())) {
				cartList.add(cart);
			}
		}

		model.addAttribute("productList", productList);
		model.addAttribute("cartList", cartList);

		return "user-form/user-view";
	}

	@PostMapping("/userForm")
	public String createUser(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab", "active");
		} else {
			try {
				userService.createUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab", "active");
				model.addAttribute("userTab", "active");

			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
			}
		}

		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		return "user-form/user-view";
	}

	@GetMapping("/editUser/{id}")
	public String getEditUserForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User userToEdit = userService.getUserById(id);

		model.addAttribute("userForm", userToEdit);
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("formTab", "active");
		model.addAttribute("editMode", "true");
		model.addAttribute("passwordForm", new ChangePasswordForm(id));

		return "user-form/user-view";
	}

	@PostMapping("/editUser")
	public String postEditUserForm(@Valid @ModelAttribute("userForm") User user, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("userForm", user);
			model.addAttribute("formTab", "active");
			model.addAttribute("editMode", "true");
			model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
		} else {
			try {
				userService.updateUser(user);
				model.addAttribute("userForm", new User());
				model.addAttribute("listTab", "active");
			} catch (Exception e) {
				model.addAttribute("formErrorMessage", e.getMessage());
				model.addAttribute("userForm", user);
				model.addAttribute("formTab", "active");
				model.addAttribute("userList", userService.getAllUsers());
				model.addAttribute("roles", roleRepository.findAll());
				model.addAttribute("editMode", "true");
				model.addAttribute("passwordForm", new ChangePasswordForm(user.getId()));
			}
		}

		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		return "user-form/user-view";
	}

	@GetMapping("/userForm/cancel")
	public String cancelEditUser(ModelMap model) {
		return "redirect:/userForm";
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(Model model, @PathVariable(name = "id") Long id) throws Exception {
		try {
			userService.deleteUser(id);
		} catch (Exception e) {
			model.addAttribute("listErrorMessage", e.getMessage());
		}
		return userForm(model);
	}

	@PostMapping("/deleteUser/{id}")
	public String deleteUserPost(Model model, @PathVariable(name = "id") Long id) {
		try {
			userService.deleteUser(id);
		} catch (Exception e) {
			model.addAttribute("listErrorMessage", e.getMessage());
		}
		return "redirect:/logout";
	}

	@GetMapping("/displayDeleteForm/{id}")
	public String deleteForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User userToEdit = userService.getUserById(id);
		model.addAttribute("userToDelete", userToEdit);
		return "user-form/deleteForm";
	}
}

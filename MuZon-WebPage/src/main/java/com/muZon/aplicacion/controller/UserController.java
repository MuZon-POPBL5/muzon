package com.muZon.aplicacion.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.muZon.aplicacion.dto.ChangeAddressForm;
import com.muZon.aplicacion.dto.ChangePasswordForm;
import com.muZon.aplicacion.entity.GrafanaMetrics;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.Role;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.exception.CustomeFieldValidationException;
import com.muZon.aplicacion.repository.CartRepository;
import com.muZon.aplicacion.repository.ProductRepository;
import com.muZon.aplicacion.repository.RoleRepository;
import com.muZon.aplicacion.service.GrafanaService;
import com.muZon.aplicacion.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

	static String category;

	@Autowired
	UserService userService;

	@Autowired
	GrafanaService grafanaService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

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

		try {
			userService.createUser(user);
		} catch (CustomeFieldValidationException cfve) {
			result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
		} catch (Exception e) {
			model.addAttribute("formErrorMessage", e.getMessage());
		}

		return index();
	}

	@GetMapping("/userForm")
	public String userForm(Model model) throws Exception {
		GrafanaMetrics metricsGraf= new GrafanaMetrics();
        Date today = new Date();
        metricsGraf.setSqlTimestamp(today);
        Integer numLogs = metricsGraf.getNumLogins();
        metricsGraf.setNumLogins(++numLogs);
        grafanaService.saveGrafanaMetrics(metricsGraf);
		
		model.addAttribute("userForm", new User());
		model.addAttribute("userList", userService.getAllUsers());
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("productList", productRepository.findAll());
		model.addAttribute("cartList", cartRepository.findAll());
		model.addAttribute("listTab", "active");
		model.addAttribute("userTab", "active");

		return "user-form/user-view";
	}

	@GetMapping("/selectAddress/{id}")
	public String getShowAddressForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User user = userService.getUserById(id);

		model.addAttribute("userAddress", user);
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
			userService.changeAddress(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}

	@GetMapping("/displayDeleteForm/{id}")
	public String deleteForm(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User userToEdit = userService.getUserById(id);
		model.addAttribute("userToDelete", userToEdit);
		return "user-form/deleteForm";
	}

	@GetMapping("/accountSettings/{id}")
	public String editAccountSettings(Model model, @PathVariable(name = "id") Long id) throws Exception {
		User userToEdit = userService.getUserById(id);

		model.addAttribute("user", userToEdit);
		model.addAttribute("editMode", "true");
		model.addAttribute("passwordForm", new ChangePasswordForm(id));

		return "user-form/accountSettings";
	}

	@GetMapping("/sell/{id}")
	public String sell(Model model, @PathVariable(name = "id") Long id) throws Exception {
		Role userRole = roleRepository.findByName("USER");
		User userToEdit = userService.getUserById(id);
		List<Role> roles = Arrays.asList(userRole);

		model.addAttribute("userId", userToEdit);
		model.addAttribute("editMode", "true");
		model.addAttribute("productForm", new Product());
		model.addAttribute("roles", roles);

		return "user-form/sellForm";
	}

	@PostMapping("/addProduct/{id}")
	public String addProductToSell(@Valid @ModelAttribute("productForm") Product product, BindingResult result,
			Model model, @PathVariable(name = "id") Long id) throws Exception {

		User seller = userService.getUserById(id);

		try {
			userService.addProduct(seller, product, this.category);
		} catch (CustomeFieldValidationException cfve) {
			result.rejectValue(cfve.getFieldName(), null, cfve.getMessage());
		} catch (Exception e) {
			model.addAttribute("formErrorMessage", e.getMessage());
		}

		return "redirect:/userForm";
	}

	@PostMapping("/addProduct")
	public ResponseEntity<?> passingCategory(@RequestBody String category) throws Exception {

		String split = category.split("[:]")[1];
		String onlyComillas = split.split("[}]")[0];
		this.category = onlyComillas.replaceAll("^\"|\"$", "");

		return ResponseEntity.ok("Success");
	}

	@PostMapping("/upload")
	public ResponseEntity<?> singleFileUpload(@RequestParam() MultipartFile file,
			RedirectAttributes redirectAttributes) {
		try {
			byte[] bytes = file.getBytes();
			/*
			 * Path path = Paths.get(file.getOriginalFilename());
			 * Files.write(path, bytes);
			 */

			userService.save(bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok("Success");
	}

	@PostMapping("/displayProducts/{id}")
	public String displayCarrousel(Model model, @PathVariable(name = "id") Long id, @RequestBody String data)
			throws Exception {
		User user = userService.getUserById(id);

		model.addAttribute("user", user);
		model.addAttribute("editMode", "true");

		if (data.split("[=]")[0].equals("ccate")) {
			String category = data.split("[=]")[1];
			model.addAttribute("categoryList", productRepository.findByCategory(category));
			
			return "user-form/productsPage";
		} else {
			String idP = data.split("[=]")[1];
			Product productToDisplay = productRepository.findById(Long.valueOf(idP)).orElseThrow();
			model.addAttribute("product", productToDisplay);

			return "user-form/buyProduct";
		}
	}

	@PostMapping("/addToCart/{id}")
	public String addToCart(Model model, @PathVariable(name = "id") Long id, @RequestBody String data)
			throws Exception {
		Product productToSave = productRepository.findById(id).orElseThrow();

		String userData = data.split("[,]")[0];
		String userId = userData.split("[=]")[1];
		String quantityData = data.split("[,]")[1];
		String quantity = quantityData.split("[=]")[1];

		User user = userService.getUserById(Long.valueOf(userId));

		// model.addAttribute("user", userToEdit);
		model.addAttribute("editMode", "true");

		userService.addToCart(productToSave, Integer.valueOf(quantity), user);

		return "index";
	}

	@PostMapping("/buyNow/{id}")
	public ResponseEntity<String> buyNow(Model model, @PathVariable(name = "id") Long id, @RequestBody String data)
			throws Exception {
		Product productToSave = productRepository.findById(id).orElseThrow();

		String userData = data.split("[,]")[0];
		String userId = userData.split("[=]")[1];
		String quantityData = data.split("[,]")[1];
		String quantity = quantityData.split("[=]")[1];

		User user = userService.getUserById(Long.valueOf(userId));

		// model.addAttribute("user", userToEdit);
		model.addAttribute("editMode", "true");

		userService.addBuyNow(productToSave, Integer.valueOf(quantity), user);

		return ResponseEntity.ok("Success");
	}
}
package com.muZon.aplicacion.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
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
import com.muZon.aplicacion.entity.Cart;
import com.muZon.aplicacion.entity.GrafanaMetrics;
import com.muZon.aplicacion.entity.Product;
import com.muZon.aplicacion.entity.Role;
import com.muZon.aplicacion.entity.User;
import com.muZon.aplicacion.exception.CustomeFieldValidationException;
import com.muZon.aplicacion.repository.AddressRepository;
import com.muZon.aplicacion.repository.CartRepository;
import com.muZon.aplicacion.repository.ProductRepository;
import com.muZon.aplicacion.repository.RoleRepository;
import com.muZon.aplicacion.service.AddressService;
import com.muZon.aplicacion.service.GrafanaService;
import com.muZon.aplicacion.service.ProductService;
import com.muZon.aplicacion.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	final static int SET_DEFAULT = 1;

	@Autowired
	UserService userService;

	@Autowired
	GrafanaService grafanaService;

	@Autowired
	AddressService addressService;

	@Autowired
	ProductService productService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	AddressRepository addressRepository;

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
			if (cart.getSellerId().getId() == (user.get().getId())) {
				cartList.add(cart);
			}
		}

		model.addAttribute("productList", productList);
		model.addAttribute("cartList", cartList);

		return "user-form/user-view";
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
			addressService.addAddress(form);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok("Success");
	}

	@GetMapping("/setDefault/{id}")
	public String setDefault(Model model, @PathVariable(name = "id") Long id) {
		Optional<Address> address = addressRepository.findById(id);

		address.get().setDefaultAddress(SET_DEFAULT);
		addressService.saveChanges(address.get(), id);

		return "redirect:/userForm";
	}

	@GetMapping("/delete/{id}")
	public String delete(Model model, @PathVariable(name = "id") Long id) {

		addressService.delete(id);

		return "redirect:/userForm";
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
			productService.addProduct(seller, product, this.category);
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
			byte[] fileContent = file.getBytes();

			String encodedString = Base64.getEncoder().encodeToString(fileContent);

			productService.save(encodedString);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok("Success");
	}

	@GetMapping("/displayProducts/{id}")
	public String displayFromCarrousel(Model model, @PathVariable(name = "id") Long id)
			throws Exception {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

		model.addAttribute("user", user);
		model.addAttribute("editMode", "true");

		Product productToDisplay = productRepository.findById(id).orElseThrow();
		model.addAttribute("product", productToDisplay);

		return "user-form/buyProduct";
	}

	@PostMapping("/displayProducts")
	public String displayCarrousel(Model model, @RequestBody String data)
			throws Exception {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

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

		String quantity = data.split("[=]")[1];

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

		productService.addToCart(productToSave, Integer.valueOf(quantity), user);

		return "index";
	}

	@PostMapping("/buyNow/{id}")
	public ResponseEntity<String> buyNow(Model model, @PathVariable(name = "id") Long id, @RequestBody String data)
			throws Exception {
		Product productToSave = productRepository.findById(id).orElseThrow();

		String quantity = data.split("[=]")[1];

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

		productService.addBuyNow(productToSave, Integer.valueOf(quantity), user);

		return ResponseEntity.ok("Success");
	}

	@GetMapping("/buyAll")
	public String buyAll(Model model) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

		List<Cart> cartList = new ArrayList<>();		

		Iterable<Cart> cr = cartRepository.findAll();

		for (Cart cart : cr) {
			if (cart.getSellerId().getId() == (user.get().getId())) {
				cartList.add(cart);
			}
		}

		for(Cart cart : cartList){
			productService.addBuyNow(cart.getProductId(), cart.getQuantity(), user);
		}

		return "redirect:/userForm";
	}
}

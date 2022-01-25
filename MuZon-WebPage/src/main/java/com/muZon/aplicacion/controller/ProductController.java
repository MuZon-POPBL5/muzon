package com.muZon.aplicacion.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.muZon.aplicacion.entity.Cart;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    static String category;
	final static int SET_DEFAULT = 1;

	@Autowired
	UserService userService;

	@Autowired
	ProductService productService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

    
	@GetMapping("/sell/{id}")
	public String sell(Model model, @PathVariable(name = "id") Long id) throws Exception {
		Role userRole = roleRepository.findByName("USER");
		User userToEdit = userService.getUserById(id);
		List<Role> roles = Arrays.asList(userRole);

		model.addAttribute("userId", userToEdit);
		model.addAttribute("editMode", "true");
		model.addAttribute("productForm", new Product());
		model.addAttribute("roles", roles);

		return "products-form/sellForm";
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

		return "products-form/buyProduct";
	}

	@PostMapping("/displayProducts")
	public String displayCarrousel(Model model, @RequestBody String data)
			throws Exception {

		List<Product> categoryList = new ArrayList<>();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

		model.addAttribute("user", user);
		model.addAttribute("editMode", "true");

		if (data.split("[=]")[0].equals("ccate")) {
			String category = data.split("[=]")[1];

			Iterable<Product> list = productRepository.findByCategory(category);

			for(Product product:list){
				if(product.getSellerId().getId() != user.get().getId()){
					categoryList.add(product);
				}
			}

			model.addAttribute("categoryList", categoryList);

			return "products-form/productsPage";
		} else {
			String idP = data.split("[=]")[1];
			Product productToDisplay = productRepository.findById(Long.valueOf(idP)).orElseThrow();
			model.addAttribute("product", productToDisplay);

			return "products-form/buyProduct";
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

		int stock = productToSave.getQuantity() - Integer.valueOf(quantity);

		productService.changeStock(productToSave, stock);

		return ResponseEntity.ok("Success");
	}

	@GetMapping("/buyAll")
	public String buyAll(Model model) throws Exception {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Optional<User> user = userService.getUserByUsername(((UserDetails) principal).getUsername());

		List<Cart> cartList = new ArrayList<>();		

		Iterable<Cart> cr = cartRepository.findAll();

		for (Cart cart : cr) {
			if (cart.getBoughtId().getId() == (user.get().getId())) {
				cartList.add(cart);
			}
		}

		for(Cart cart : cartList){
			productService.addBuyNow(cart.getProductId(), cart.getQuantity(), user);
			productService.changeStock(cart.getProductId(), cart.getProductId().getQuantity() - cart.getQuantity());
		}

		productService.deleteCart(user.get());

		return "redirect:/userForm";
	}
    
}

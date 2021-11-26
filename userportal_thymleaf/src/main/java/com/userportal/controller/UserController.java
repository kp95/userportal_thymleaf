package com.userportal.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lowagie.text.DocumentException;
import com.userportal.domain.User;
import com.userportal.domain.UserPrincipal;
import com.userportal.login.oauth2.LoginOAuth2User;
import com.userportal.service.UserServiceImpl;
import com.userportal.util.AmazonS3Util;
import com.userportal.util.CsvExport;
import com.userportal.util.FileUploadUtil;
import com.userportal.util.PdfExport;

@Controller
//@RequestMapping("/users")
public class UserController {

	private String defaultRedirectURL = "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserServiceImpl service;
	
	
	@GetMapping({"/users","/"})
	public String loadFirstPage() {
		return defaultRedirectURL;
	}
	
	
	@GetMapping("/users/page/{pageNum}")
	public String findAll(@PathVariable(name = "pageNum") int pageNum, Model model,
			@RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir,
			@RequestParam(name = "keyword",required = false) String keyword) {
		
		//fecth logged in user
		Long loggedInUserId = null;
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserPrincipal) {
			UserPrincipal loggedInUser = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			loggedInUserId = loggedInUser.getId();
		}
		else if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof LoginOAuth2User)
		{
			LoginOAuth2User loggedInUser = (LoginOAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User temp = service.findByEmail(loggedInUser.getEmail());
			loggedInUserId = temp.getId();
		}
		
		Page<User> page = service.findAllUser(pageNum, sortField, sortDir, keyword);
		List<User> listUsers = page.getContent();
		
		long startCount = (pageNum - 1) * UserServiceImpl.USERS_PER_PAGE + 1;
		long endCount = startCount + UserServiceImpl.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("loggedInUserId",loggedInUserId);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "users/showUsers";
	}
	
	@GetMapping("/users/addNew")
	public String addNew(Model model) {
		model.addAttribute("pageTitle","Create New User");
		model.addAttribute("user",new User());
		List<User> managers = service.findByAdminManager();
		model.addAttribute("listManagers",managers);
		return "users/addNew";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,@RequestParam("image") MultipartFile multipartFile) {

		if(!multipartFile.isEmpty()) {
			String fileName = multipartFile.getOriginalFilename();
			user.setProfileImage(fileName);
			
			User registerUser = service.register(user);
			
			String uploadDir = "profile-image/" + registerUser.getId();
			 
			try {
				AmazonS3Util.removeFolder(uploadDir);
				AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
				//FileUploadUtil.saveImage(uploadDir, fileName, multipartFile);
			} 
			catch (IOException e) {
				redirectAttributes.addFlashAttribute("message","User saved But error ocurred in Saving image");
			}
		}
		else {
			if(user.getProfileImage().isEmpty()) {
				user.setProfileImage(null);
			}
			
			service.register(user);
		}
		redirectAttributes.addFlashAttribute("message","User saved Succussfully!!");
		return "redirect:/users";
		
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateStatus(@PathVariable(name = "id") Long id,@PathVariable(name = "status") boolean status) {
		service.updateStaus(id, status);
		return defaultRedirectURL;
	}
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Long id,RedirectAttributes attributes) {
		
		try {
			service.delete(id);
			String userPhotosDir = "profile-image/" + id;
			FileUploadUtil.removeDir(userPhotosDir);
			attributes.addFlashAttribute("message","User with ID :" + id +" deleted Succussfully!!");
		}
		catch (Exception e) {
			// TODO: handle exception
			attributes.addFlashAttribute("error_message","User with ID :" + id +" does not Exist!!");
		}
		return defaultRedirectURL;
	}
	
	
	@GetMapping("/logUser/edit/{id}")
	public String editUser(@PathVariable(name = "id") Long id, Model model) {
		
		Object loggedInUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String loggedRole = "";
		if(loggedInUser instanceof UserPrincipal) {
			loggedRole = ((UserPrincipal) loggedInUser).getR();
		}
		List<User> managers = service.findByAdminManager();
		model.addAttribute("listManagers",managers);
		model.addAttribute("pageTitle","Edit User Id :" + id);
		model.addAttribute("user",service.findById(id));
		model.addAttribute("loggedRole",loggedRole);
		return "users/addNew";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) {
		
		UserPrincipal loggedUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<User> users = null;
		if(loggedUser.getR().equals("admin")) {
			users = service.listAll();
		}
		else {
			User parentUser = service.findById(loggedUser.getId());
			users = service.listAll(parentUser);
		}
		
		CsvExport exporter = new CsvExport();
		exporter.export(users, response);
	}
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		UserPrincipal loggedUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<User> users = null;
		if(loggedUser.getR().equals("admin")) {
			users = service.listAll();
		}
		else {
			User parentUser = service.findById(loggedUser.getId());
			users = service.listAll(parentUser);
		}
		PdfExport exporter = new PdfExport();
		exporter.export(users, response);
		LOGGER.debug(response.toString());
	}
}

package org.ashina.mycontact.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.ashina.mycontact.entity.Contact;
import org.ashina.mycontact.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

	@Autowired
	private ContactService contactService;

	@GetMapping("/")
	public String list(Model model, HttpServletRequest request, RedirectAttributes redirect) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		if (hasUserRole) {
			return "admin";
		} else {

			if (model.asMap().get("success") != null)
				redirect.addFlashAttribute("success", model.asMap().get("success").toString());
			return "redirect:/list/page/1";
		}
	}

	// paging number
	@GetMapping("/list/page")
	public String showEmployeePage(HttpServletRequest request,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
			@ModelAttribute("ten") String term, @ModelAttribute("email") String email,
			@ModelAttribute("sdt") String sdt, Model model, ModelMap modal) {

		int pagesize = 5;
		Page<Contact> pageContact = contactService.search(term, email, sdt, pageNumber, pagesize);

		PagedListHolder<?> pages = new PagedListHolder<>(pageContact.getContent());
		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}

		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - pageContact.getTotalPages());
		int end = Math.min(begin + 2, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/list/page/";

		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("employees", pageContact.getContent());
		pages.getPageList();

		return "list";
	}

	@GetMapping("/contact/add")
	public String add(Model model) {
		model.addAttribute("contact", new Contact());
		return "form";
	}

	@GetMapping("/contact/{id}/edit")
	public String edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("contact", contactService.findOne(id));
		return "form";
	}

	@PostMapping("/contact/save")
	public String save(@Valid Contact contact, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "form";
		}
		contactService.save(contact);
		redirect.addFlashAttribute("successMessage", "Saved contact successfully!");
		return "redirect:/";
	}

	@GetMapping("/contact/{id}/delete")
	public String delete(@PathVariable int id, RedirectAttributes redirect) {
		contactService.delete(id);
		redirect.addFlashAttribute("successMessage", "Deleted contact successfully!");
		return "redirect:/";
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	@GetMapping("/403")
	public String accessDenied() {
		return "403";
	}

	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

}

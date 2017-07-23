package fr.sedoo.spring.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.PersonName;
import org.bibsonomy.model.Post;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.sedoo.spring.dao.BibsonomyDAO;
import fr.sedoo.spring.util.ComparatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

@CrossOrigin
@RestController
@RequestMapping("/selectlist")
@Api(value = "/selectlist", description = "Manage select lists")
public class SelectListController {

	private BibsonomyDAO dao = new BibsonomyDAO();
	
	@GetMapping("/journals/{username}")
	@ApiOperation(value = "Find all journals", notes = "Find all journals")
	public List<String> getAllJournals(
			@ApiParam(value = "username", required = true) @PathVariable("username") String username) {

		Set<String> aux = new TreeSet<String>();
		List<String> result = new ArrayList<String>();
		
		List<Post<BibTex>> list = dao.getAllPublications(username);
		for (Post<BibTex> p : list) {
			if (p.getResource().getJournal() != null) {
				aux.add(p.getResource().getJournal());
			}
		}
		
		return ComparatorUtils.getOrdersList(aux);
	}
	
	@GetMapping("/authors/{username}")
	@ApiOperation(value = "Find all authors", notes = "Find all authors")
	
	public List<PersonName> getAllAuthors(
			@ApiParam(value = "username", required = true) @PathVariable("username") String username) {

		List<PersonName> aux = new ArrayList<PersonName>();
		List<PersonName> result = new ArrayList<PersonName>();
		BibsonomyDAO dao = new BibsonomyDAO();
		List<Post<BibTex>> list = dao.getAllPublications(username);
		for (Post<BibTex> p : list) {
			if (p.getResource().getAuthor() != null) {
				for(PersonName pn : p.getResource().getAuthor()) {
					aux.add(pn);
				}
			}
		}
		
		for(PersonName p : aux) {
			result.add(p);
		}
		return result;
		
		// return result.sort((PersonName o1, PersonName o2)->o1.getLastName().compareTo(o2.getLastName()));
	
	}

}

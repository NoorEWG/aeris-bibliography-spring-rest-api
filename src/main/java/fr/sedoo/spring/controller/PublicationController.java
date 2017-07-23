package fr.sedoo.spring.controller;

import java.util.Date;
import java.util.List;

import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.sedoo.spring.dao.BibsonomyDAO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

@Api(value = "/publication", description = "Manage publications")
@CrossOrigin
@RestController
@RequestMapping("/publication")
public class PublicationController {

	// @Autowired
	private BibsonomyDAO dao = new BibsonomyDAO();

	@GetMapping("/isAlive")
	public String isAlive() {
		return "isAlive";
	}

	@ApiOperation(value = "Find publications by username", notes = "Find publications by username")
	@GetMapping("/all/{username}")
	public List getAllPublications(
			@ApiParam(value = "username", required = true) @PathVariable("username") String username) {
		return dao.getAllPublications(username);

	}

	@ApiOperation(value = "Find publications by tag", notes = "Find publications by tag")
	@GetMapping("/tag/{tag}")
	public List getPublicationsByTag(@ApiParam(value = "tag", required = true) @PathVariable("tag") String tag) {
		return dao.getAllPublicationsByTag(tag);

	}

	@ApiOperation(value = "Find publications by searchterm", notes = "Find publications by searchterm")
	@GetMapping("/search/{searchString}")
	public List getPublicationsBySearchString(
			@ApiParam(value = "tag", required = true) @PathVariable("searchString") String searchString) {
		return dao.getAllPublicationsBySearchString(searchString);
	}

	@ApiOperation(value = "Find publications by journalname", notes = "Find publications by journalname")
	@GetMapping("/journal/{journalName}")
	public List getPublicationsByJournalName(
			@ApiParam(value = "tag", required = true) @PathVariable("journalname") String journalName) {
		return dao.getPublicationsByJournalName(journalName);
	}

	@ApiOperation(value = "Find a publication by the resourcehash (=unique identifier)", notes = "Find publications by the resourcehash (=unique identifier)")
	@GetMapping("/hash/{resourceHash}")
	public List getPublicationByResourceHash(
			@ApiParam(value = "resourceHash", required = true) @PathVariable("resourceHash") String resourceHash) {
		return dao.getPublicationByResourceHash(resourceHash);
	}

	@ApiOperation(value = "Add a publication", notes = "Add a publication")
	@PostMapping("/add")
	public String addPublication(@ApiParam(value = "title", required = true) @RequestParam("title") String title,
			@ApiParam(value = "firstname", required = true) @RequestParam("firstname") String firstName,
			@ApiParam(value = "lastname", required = true) @RequestParam("lastname") String lastName,
			@ApiParam(value = "year", required = true) @RequestParam("year") String year,
			@ApiParam(value = "project", required = true) @RequestParam("project") String project) {
		return dao.addPublication(title, firstName, lastName, year, project).get(0);
	}

	@PutMapping("/update/add/tag/{tag}/{hash}")
	@ApiOperation(value = "Add a tag to an existing publication", notes = "Add a tag to an existing publication")
	public String updatePublicationAddTag(@ApiParam(value = "tag", required = true) @PathVariable("tag") String tag,
			@ApiParam(value = "hash", required = true) @PathVariable("hash") String hash) {
		return dao.updatePublicationAddTag(tag, hash).get(0);
	}

	@PutMapping("/update/add/abstract/{abstract}/{hash}")
	@ApiOperation(value = "Add an abstract to an existing publication", notes = "Add an abstract to an existing publication")
	public String updatePublicationAddAbstract(
			@ApiParam(value = "abstract", required = true) @PathVariable("abstract") String description,
			@ApiParam(value = "hash", required = true) @PathVariable("hash") String hash) {

		return dao.updatePublicationAddAbstract(description, hash).get(0);
	}

	@PutMapping("/update/add/url/{url}/{hash}")
	@ApiOperation(value = "Add an url to an existing publication", notes = "Add an url to an existing publication")
	public String updatePublicationAddUrl(@ApiParam(value = "url", required = true) @PathVariable("url") String url,
			@ApiParam(value = "hash", required = true) @PathVariable("hash") String hash) {

		return dao.updatePublicationAddUrl(url, hash).get(0);
	}

	@PutMapping("/update/add/author/{hash}")
	@ApiOperation(value = "Add an author to an existing publication", notes = "Add an author to an existing publication")
	public String updatePublicationAddUrl(@ApiParam(value = "hash", required = true) @PathVariable("hash") String hash,
			@ApiParam(value = "firstname", required = true) @RequestParam("firstname") String firstName,
			@ApiParam(value = "lastname", required = true) @RequestParam("lastname") String lastName) {

		return dao.updatePublicationAddAuthor(firstName, lastName, hash).get(0);
	}
	
	
	@DeleteMapping("/delete/{hash}")
	@ApiOperation(value = "Delete an existing publication by interhash", notes = "Delete an existing publication by interhash")
	public String deletePublicationByHash(@ApiParam(value = "hash", required = true) @PathVariable("hash") String hash) {

		return dao.deletePublication(hash);
	}
	
	
}

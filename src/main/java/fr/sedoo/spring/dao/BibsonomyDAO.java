package fr.sedoo.spring.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bibsonomy.common.enums.Filter;
import org.bibsonomy.common.enums.GroupingEntity;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Person;
import org.bibsonomy.model.PersonName;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.model.util.BibTexUtils;
import org.bibsonomy.model.util.GroupUtils;
import org.bibsonomy.model.util.PersonNameUtils;
import org.bibsonomy.rest.client.RestLogicFactory;
import org.springframework.stereotype.Component;


/*
 * @author Erica de Graaf-Borkent
 * Java connector between Aeris-Bibliography and BibSonomy
 */

@Component
public class BibsonomyDAO {

	final String username = "ericadegraaf";
	final String apikey = "da5675f48806c1381f1ac4c61f90f338";
	final RestLogicFactory rlf = new RestLogicFactory();
	final LogicInterface logic = rlf.getLogicAccess(username, apikey);

	public BibsonomyDAO() {
	}

	/*
	* getPosts possible parameters:
	* (Class<T> resourceType, GroupingEntity grouping, String groupingName,
	* List<String> tags, String hash, String search, SearchType searchType,
	* Set<Filter> filters, Order order, Date startDate, Date endDate, int start,
	* int end);
	*/


	/*
	 * Find all publications by username
	 * @param string username
	 */
	
	public List<Post<BibTex>> getAllPublications(String userName) {
		return logic.getPosts(BibTex.class, GroupingEntity.ALL, userName, null, null, null, null, null, null, null,
				null, 0, 100);
	}

	/*
	 * Find publications by a tag
	 * @param string tag
	 */
	
	public List<Post<BibTex>> getAllPublicationsByTag(String tag) {
		List<String> tags = new ArrayList<String>();
		tags.add(tag);
		return logic.getPosts(BibTex.class, GroupingEntity.ALL, null, tags, null, null, null, null, null, null, null, 0,
				100);
	}
	
	/*
	 * Find publications by a tag
	 * @param string searchString
	 */
	
	public List<Post<BibTex>> getAllPublicationsBySearchString(String searchString) {
		return logic.getPosts(BibTex.class, GroupingEntity.ALL, null, null, null, searchString, null, null, null, null, null, 0,
				100);
	}
	
	/*
	 * Find publications by resourcehash
	 * @param string resourcehash
	 */
	
	
	public List<Post<BibTex>> getPublicationByResourceHash(String resourceHash) {
		return logic.getPosts(BibTex.class, GroupingEntity.ALL, null, null, resourceHash, null, null, null, null, null, null, 0,
				10);

	}
	
	/*
	 * Find publications by journalname
	 * @param string journalName
	 */
	
	
	public List<Post<BibTex>> getPublicationsByJournalName(String journalName) {
		
		List<Post<BibTex>> result = new ArrayList<Post<BibTex>>();
		List<Post<BibTex>> list = logic.getPosts(BibTex.class, GroupingEntity.ALL, username, null, null, null, null, null, null, null, null, 0,100);
		for(Post<BibTex> p : list) {
			if(p.getResource().getJournal().equalsIgnoreCase(journalName)) {
				result.add(p);
			}
		}
		return result;
	}
	

	/*
	 * Add a publication
	 * @param: string title
	 * @param: string firstname
	 * @param: string lastname
	 * @param: string year
	 * @param: string project
	 */
	
	public List<String> addPublication(String title, String firstName, String lastName, String year, String project) {
	
	final Post<BibTex> post = new Post<BibTex>();
	post.setGroups(Collections.singleton(GroupUtils.buildPublicGroup()));
	post.addTag("aeris-data");
	post.addTag(project);
	post.setUser(new User(username));

	final BibTex publication = new BibTex();

	publication.setTitle(title);
	publication.setAuthor(PersonNameUtils.discoverPersonNamesIgnoreExceptions(lastName + ", " + firstName));
	publication.setYear(year);
	publication.setBibtexKey(BibTexUtils.generateBibtexKey(publication));
	publication.setEntrytype("article");
	
	post.setResource(publication);
	
	return logic.createPosts(Collections.<Post<? extends Resource>>singletonList(post));
	}
	
	
	/*
	 * Update publication: add a tag
	 * @param: string resourcehash
	 * @param: string tag
	 */
	
	public List<String> updatePublicationAddTag(String tag, String hash) {
		final Post<? extends Resource> post = getPublicationByResourceHash(hash).get(0);
		post.addTag(tag);
		
		return logic.updatePosts(Collections.<Post<? extends Resource>>singletonList(post), PostUpdateOperation.UPDATE_ALL);
	}
	
	/*
	 * Update publication: add an abstract
	 * @param: string resourcehash
	 * @param: string abstract
	 */
	
	public List<String> updatePublicationAddAbstract(String description, String hash) {
		final Post<BibTex> post = getPublicationByResourceHash(hash).get(0);
		post.setDescription(description);
		
		return logic.updatePosts(Collections.<Post<? extends Resource>>singletonList(post), PostUpdateOperation.UPDATE_ALL);
	}
	
	/*
	 * Update publication: add an url
	 * @param: string resourcehash
	 * @param: string url
	 */
	
	public List<String> updatePublicationAddUrl(String url, String hash) {
		final Post<BibTex> post = getPublicationByResourceHash(hash).get(0);
		post.getResource().setUrl(url);
		
		return logic.updatePosts(Collections.<Post<? extends Resource>>singletonList(post), PostUpdateOperation.UPDATE_ALL);
	}
	
	/*
	 * Update publication: add an author
	 * @param string resourcehash
	 * @param string firstname
	 * @param string lastname
	 */
	
	public List<String> updatePublicationAddAuthor(String firstName, String lastName, String hash) {
		final Post<BibTex> post = getPublicationByResourceHash(hash).get(0);
		PersonName p = new PersonName();
		p.setFirstName(firstName);
		p.setLastName(lastName);
		List<PersonName> persons = post.getResource().getAuthor();
		persons.add(p);
		post.getResource().setAuthor(persons);
		
		return logic.updatePosts(Collections.<Post<? extends Resource>>singletonList(post), PostUpdateOperation.UPDATE_ALL);
	}
	
	public String deletePublication(String hash) {
		
		List<String> resourceHashes = new ArrayList<String>();
		resourceHashes.add(hash);
		logic.deletePosts(username, resourceHashes);
		return null;
	}
	
	
	
}

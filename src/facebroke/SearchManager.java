package facebroke;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.search.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.User;
import facebroke.util.HibernateUtility;


/**
 * Servlet to handle the /search endpoint.
 * 
 * Basically, this will handle search requests and render a results
 * page out to the requesting user
 * 
 * @author matt @ Software Secured
 */
@WebServlet("/search")
public class SearchManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(SearchManager.class);

	
	/**
	 * Call parent servlet
	 */
    public SearchManager() {
        super();
    }


    /**
     * Simple shim to pass GET requests to handleSearch
     */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleSearch(req, res);
	}


	/**
     * Simple shim to pass POST requests to handleSearch
     */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		handleSearch(req, res);
	}

	
	private void handleSearch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		FullTextSession fts = Search.getFullTextSession(HibernateUtility.getSessionFactory().openSession());
		
		fts.beginTransaction();
		
		QueryBuilder qb = fts.getSearchFactory()
							 .buildQueryBuilder()
							 .forEntity(User.class)
							 .get();
		
		Query query = qb.keyword()
						.onField("fname")
						.matching("mary")
						.createQuery();
		
		//Hibernate Query wrapper
		FullTextQuery hibQuery = fts.createFullTextQuery(query, User.class);
		
		@SuppressWarnings("unchecked")
		List<User> result = (List<User>)hibQuery.getResultList();
		
		log.info("Got {} results for \'mary\'",result.size());
		
		if(result.size() > 0) {
			
			/*Collections.sort(result, new Comparator<User>() {
				public int compare(final User a, final User b) {
					return a.getFname().compareTo(b.getFname());
				}
			});*/
			
	
			for(User u : result) {
				log.info("First Name: {}",u.getFname());
				log.info("Last Name: {}",u.getLname());
				log.info("Username: {}",u.getUsername());
			}
		}
	}
}

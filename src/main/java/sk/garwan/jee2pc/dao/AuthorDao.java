package sk.garwan.jee2pc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.garwan.jee2pc.domain.Author;

public class AuthorDao {

	@PersistenceContext(unitName = "authors")
	private EntityManager entityManager;

	public AuthorDao() { }

	public Author saveAuthor(Author author) {
		entityManager.persist(author);
		return author;
	}

	@SuppressWarnings("unchecked")
	public List<Author> loadAllAuthors() {
		return entityManager.createQuery("select a from Author a").getResultList();
	}
}

package sk.garwan.jee2pc.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sk.garwan.jee2pc.domain.Book;

public class BookDao {

	@PersistenceContext(unitName = "books")
	private EntityManager entityManager;

	public BookDao() { }

	public Book saveBook(Book book) {
		entityManager.persist(book);
		return book;
	}

	@SuppressWarnings("unchecked")
	public List<Book> loadAllBooks() {
		return entityManager.createQuery("select b from Book b").getResultList();
	}
}

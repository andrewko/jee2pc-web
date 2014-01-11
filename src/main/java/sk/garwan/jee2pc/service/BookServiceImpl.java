package sk.garwan.jee2pc.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import sk.garwan.jee2pc.dao.BookDao;
import sk.garwan.jee2pc.domain.Book;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class BookServiceImpl implements BookService {

	@Inject
	private BookDao bookDao;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Book createBook(String name) {
		Book book = new Book();
		book.setName(name);
		book = bookDao.saveBook(book);
		return book;
	}

	@Override
	public List<Book> loadAllBooks() {
		return bookDao.loadAllBooks();
	}
}

package sk.garwan.jee2pc.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TransactionService2DBsImpl implements TransactionService2DBs {

	@EJB
	private AuthorService authorService;

	@EJB
	private BookService bookService;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doInTransaction() {
		authorService.createAuthor("author 1");
		bookService.createBook("book 1");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doInTransactionWithException() {
		authorService.createAuthor("author 2");
		bookService.createBook("book with a long name");
	}

	@Override
	public void doWithoutTransactionWithException() {
		authorService.createAuthor("author 2");
		bookService.createBook("book with a long name");
	}
}

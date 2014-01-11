package sk.garwan.jee2pc.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TransactionServiceJmsDbImpl implements TransactionServiceJmsDb {

	@EJB
	private BookService bookService;

	@EJB
	private BookStoreNotifier bookStoreNotifier;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doInTransaction() {
		bookService.createBook("book 1");
		bookStoreNotifier.sendMessage("a new book has been published");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doInTransactionWithException() {
		bookStoreNotifier.sendMessage("a new book will be published");
		bookService.createBook("book with a long name");
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void doInTransactionWithException2() {
		bookService.createBook("book 2");
		bookStoreNotifier.sendMessageWithException("a new book has been published");
	}

	@Override
	public void doWithoutTransactionWithException() {
		bookStoreNotifier.sendMessage("a new book will be published");
		bookService.createBook("book with a long name");
	}

	@Override
	public void doWithoutTransactionWithException2() {
		bookService.createBook("book 2");
		bookStoreNotifier.sendMessageWithException("a new book has been published");
	}
}

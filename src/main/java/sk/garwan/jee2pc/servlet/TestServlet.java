package sk.garwan.jee2pc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sk.garwan.jee2pc.domain.Author;
import sk.garwan.jee2pc.domain.Book;
import sk.garwan.jee2pc.service.AuthorService;
import sk.garwan.jee2pc.service.BookService;
import sk.garwan.jee2pc.service.BookStore;
import sk.garwan.jee2pc.service.BookStoreNotifier;
import sk.garwan.jee2pc.service.TransactionService2DBs;
import sk.garwan.jee2pc.service.TransactionServiceJmsDb;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String NL = System.getProperty("line.separator");
	private static final String LS = "------------------------------------";

	@EJB
	private TransactionService2DBs transactionService2DBs;

	@EJB
	private TransactionServiceJmsDb transactionServiceJmsDb;

	@EJB
	private BookService bookService;

	@EJB
	private AuthorService authorService;

	@EJB
	private BookStore bookStore;

	@EJB
	private BookStoreNotifier bookStoreNotifier;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		StringBuilder sb = new StringBuilder();

		sb.append("Distributed transakctions with a JEE container").append(NL);

		try {

			runTest1(sb);
			runTest2(sb);
			runTest3(sb);
			runTest4(sb);
			runTest5(sb);
			runTest6(sb);
			runTest7(sb);
			runTest8(sb);

		} catch (Exception e) {
			sb.append(NL).append("Test failed: ").append(e.getMessage());
		}

		PrintWriter pw = response.getWriter();
		response.setContentType("text/plain");
		pw.write(sb.toString());
		response.flushBuffer();

	}

	private void runTest1(StringBuilder sb) {
		sb.append(NL).append("Test 1 2DB's: doInTransaction").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		List<Author> authors = authorService.loadAllAuthors();
		int booksBeforeTest = books.size();
		int authorsBeforeTest = authors.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);
		sb.append("Authors in DB before test: ").append(authorsBeforeTest).append(NL);

		transactionService2DBs.doInTransaction();
		books = bookService.loadAllBooks();
		authors = authorService.loadAllAuthors();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Authors in DB after test: ").append(authors.size()).append(NL);

		if ((books.size() == booksBeforeTest + 1) && (authors.size() == authorsBeforeTest + 1)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

	private void runTest2(StringBuilder sb) {
		sb.append(NL).append("Test 2 2DB's: doInTransactionWithException").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		List<Author> authors = authorService.loadAllAuthors();
		int booksBeforeTest = books.size();
		int authorsBeforeTest = authors.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);
		sb.append("Authors in DB before test: ").append(authorsBeforeTest).append(NL);

		try {
			transactionService2DBs.doInTransactionWithException();
		} catch (Exception e) {
		}

		books = bookService.loadAllBooks();
		authors = authorService.loadAllAuthors();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Authors in DB after test: ").append(authors.size()).append(NL);

		if ((books.size() == booksBeforeTest) && (authors.size() == authorsBeforeTest)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

	private void runTest3(StringBuilder sb) {
		sb.append(NL).append("Test 3 2DB's: doWithoutTransactionWithException").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		List<Author> authors = authorService.loadAllAuthors();
		int booksBeforeTest = books.size();
		int authorsBeforeTest = authors.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);
		sb.append("Authors in DB before test: ").append(authorsBeforeTest).append(NL);

		try {
			transactionService2DBs.doWithoutTransactionWithException();
		} catch (Exception e) {
		}

		books = bookService.loadAllBooks();
		authors = authorService.loadAllAuthors();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Authors in DB after test: ").append(authors.size()).append(NL);

		if ((books.size() == booksBeforeTest) && (authors.size() == authorsBeforeTest + 1)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

	private void runTest4(StringBuilder sb) {
		sb.append(NL).append("Test 4 DB+JMS: doInTransaction").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		int booksBeforeTest = books.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);

		transactionServiceJmsDb.doInTransaction();

		books = bookService.loadAllBooks();
		String text = bookStore.receiveMessage();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Message received: ").append(text).append(NL);

		if ((books.size() == booksBeforeTest + 1) && (text != null)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

	private void runTest5(StringBuilder sb) {
		sb.append(NL).append("Test 5 DB+JMS: doInTransactionWithException").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		int booksBeforeTest = books.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);

		try {
			transactionServiceJmsDb.doInTransactionWithException();
		} catch (Exception e) {
		}

		books = bookService.loadAllBooks();
		String text = bookStore.receiveMessage();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Message received: ").append(text).append(NL);

		if ((books.size() == booksBeforeTest) && (text == null)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

	private void runTest6(StringBuilder sb) {
		sb.append(NL).append("Test 6 DB+JMS: doInTransactionWithException2").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		int booksBeforeTest = books.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);

		try {
			transactionServiceJmsDb.doInTransactionWithException2();
		} catch (Exception e) {
		}

		books = bookService.loadAllBooks();
		String text = bookStore.receiveMessage();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Message received: ").append(text).append(NL);

		if ((books.size() == booksBeforeTest) && (text == null)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

	private void runTest7(StringBuilder sb) {
		sb.append(NL).append("Test 7 DB+JMS: doWithoutTransactionWithException").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		int booksBeforeTest = books.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);

		try {
			transactionServiceJmsDb.doWithoutTransactionWithException();
		} catch (Exception e) {
		}

		books = bookService.loadAllBooks();
		String text = bookStore.receiveMessage();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Message received: ").append(text).append(NL);

		if ((books.size() == booksBeforeTest) && (text != null)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

	private void runTest8(StringBuilder sb) {
		sb.append(NL).append("Test 8 DB+JMS: doWithoutTransactionWithException2").append(NL).append(LS).append(NL);

		List<Book> books = bookService.loadAllBooks();
		int booksBeforeTest = books.size();

		sb.append("Books in DB before test: ").append(booksBeforeTest).append(NL);

		try {
			transactionServiceJmsDb.doWithoutTransactionWithException2();
		} catch (Exception e) {
		}

		books = bookService.loadAllBooks();
		String text = bookStore.receiveMessage();

		sb.append(NL);
		sb.append("Books in DB after test: ").append(books.size()).append(NL);
		sb.append("Message received: ").append(text).append(NL);

		if ((books.size() == booksBeforeTest + 1) && (text == null)) {
			sb.append("Test successed !!!").append(NL);
		} else {
			sb.append("Test failed !!!").append(NL);
		}
	}

}

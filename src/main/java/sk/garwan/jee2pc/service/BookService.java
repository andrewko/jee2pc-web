package sk.garwan.jee2pc.service;

import java.util.List;

import sk.garwan.jee2pc.domain.Book;

public interface BookService {
	
	Book createBook(String name);

	List<Book> loadAllBooks();
}

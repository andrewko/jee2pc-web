package sk.garwan.jee2pc.service;

import java.util.List;

import sk.garwan.jee2pc.domain.Author;

public interface AuthorService {

	Author createAuthor(String name);
	
	List<Author> loadAllAuthors();

}

package sk.garwan.jee2pc.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import sk.garwan.jee2pc.dao.AuthorDao;
import sk.garwan.jee2pc.domain.Author;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AuthorServiceImpl implements AuthorService {

	@Inject
	private AuthorDao authorDao;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Author createAuthor(String name) {
		Author author = new Author();
		author.setName(name);
		author = authorDao.saveAuthor(author);
		return author;
	}

	@Override
	public List<Author> loadAllAuthors() {
		return authorDao.loadAllAuthors();
	}
}

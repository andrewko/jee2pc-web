package sk.garwan.jee2pc.service;

import javax.ejb.Local;

@Local
public interface BookStore {

	String receiveMessage();

}

package sk.garwan.jee2pc.service;

import javax.ejb.Local;

@Local
public interface BookStoreNotifier {

	String sendMessage(String text);

	String sendMessageWithException(String text);

}

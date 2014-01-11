package sk.garwan.jee2pc.service;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;

@Stateless
@ResourceAdapter("activemq-ra.rar")
public class BookStoreNotifierImpl implements BookStoreNotifier {

	@Resource(mappedName = "java:/activemq/ConnectionFactory")
	private ConnectionFactory cf;

	@Resource(mappedName = "java:/activemq/queue_out")
	private Destination destination;

	@Override
	public String sendMessage(String text) {
		Connection conn = null;
		String correlationId = System.currentTimeMillis() + "";
		try {
			conn = cf.createConnection();
			Session ses = conn.createSession(true, Session.SESSION_TRANSACTED);
			MessageProducer p = ses.createProducer(destination);
			TextMessage msg = ses.createTextMessage(text);
			msg.setJMSCorrelationID(correlationId);
			p.send(msg);
			ses.close();
			return correlationId;
		} catch (Exception e) {
			throw new RuntimeException("couldn't send a jms message", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
			}
		}
	}

	@Override
	public String sendMessageWithException(String text) {
		Connection conn = null;
		String correlationId = System.currentTimeMillis() + "";
		try {
			conn = cf.createConnection();
			Session ses = conn.createSession(true, Session.SESSION_TRANSACTED);
			MessageProducer p = ses.createProducer(destination);
			TextMessage msg = ses.createTextMessage(text);
			msg.setJMSCorrelationID(correlationId);
			p.send(msg);
			// this will cause an error to occure
			throw new Exception();
		} catch (Exception e) {
			throw new RuntimeException("couldn't send a jms message", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
			}
		}
	}
}

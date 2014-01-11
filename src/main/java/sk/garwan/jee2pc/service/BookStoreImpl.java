package sk.garwan.jee2pc.service;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.jboss.ejb3.annotation.ResourceAdapter;

@Stateless
@ResourceAdapter("activemq-ra.rar")
public class BookStoreImpl implements BookStore {

	@Resource(mappedName = "java:/activemq/ConnectionFactory")
	private ConnectionFactory cf;

	@Resource(mappedName = "java:/activemq/queue_out")
	private Destination destination;

	@Override
	public String receiveMessage() {
		Connection conn = null;
		try {
			conn = cf.createConnection();
			conn.start();
			Session ses = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			javax.jms.MessageConsumer c = ses.createConsumer(destination);
			TextMessage msg = (TextMessage) c.receive(1000);
			ses.close();
			if (msg != null) {
				System.out.println(msg.getJMSCorrelationID());
				return msg.getText();
			}
			else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("couldn't receive jms message", e);
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
			}
		}
	}
}

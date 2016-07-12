package pl.btbw;


import javax.jms.*;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Publisher {

	private TopicConnection topicConnection;
	private TopicSession publisherSession;
	private TopicPublisher publisher;

	public static void main(String[] args) {
		try {

			Publisher publisher = new Publisher("RedTopic", "someTopicName");

			BufferedReader commandLine = new java.io.BufferedReader(new InputStreamReader(System.in));

			while (true) {

				String s = commandLine.readLine();

				if (s.equalsIgnoreCase("exit")) {
					publisher.close();
					System.exit(0);
				} else {
					publisher.writeMessage(s);
				}
			}

		} catch (Exception e) {
			// this is sooo bad!
			e.printStackTrace();
		}
	}

	public Publisher(String topicFactory, String topicName) {

		try {
			InitialContext ctx = new InitialContext(PropertiesUtil.getNoFileProperties());

			TopicConnectionFactory connectionFactory = (TopicConnectionFactory) ctx.lookup(topicFactory);

			this.topicConnection = connectionFactory.createTopicConnection();

			this.publisherSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			Topic chatTopic = (Topic) ctx.lookup(topicName);

			this.publisher = publisherSession.createPublisher(chatTopic);

			topicConnection.start();

		} catch (Exception e) {
			// this is sooo bad!
			e.printStackTrace();
		}
	}

	public void writeMessage(String text) throws JMSException {
		TextMessage message = publisherSession.createTextMessage();
		message.setText(text);
		publisher.publish(message);
	}

	public void close() throws JMSException {
		topicConnection.close();
	}
}

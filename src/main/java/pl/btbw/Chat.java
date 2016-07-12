package pl.btbw;

import javax.jms.*;
import java.io.*;
import javax.naming.*;

public class Chat implements javax.jms.MessageListener {

	private TopicSession publisherSession;
	private TopicPublisher publisher;
	private TopicConnection topicConnection;

	public Chat(String topicFactory, String topicName) throws Exception {

		InitialContext ctx = new InitialContext(PropertiesUtil.getNoFileProperties());

		TopicConnectionFactory connectionFactory = (TopicConnectionFactory) ctx.lookup(topicFactory);

		this.topicConnection = connectionFactory.createTopicConnection();

		this.publisherSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicSession subscriberSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		Topic chatTopic = (Topic) ctx.lookup(topicName);

		this.publisher = publisherSession.createPublisher(chatTopic);
		TopicSubscriber subscriber = subscriberSession.createSubscriber(chatTopic, null, true);

		subscriber.setMessageListener(this);

		topicConnection.start();
	}

	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			System.err.println(textMessage.getText());
		} catch (JMSException e) {
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

	public static void main(String[] args) {
		try {
			if (args.length != 3) {
				System.out.println("something went wrong");
			}

			Chat chat = new Chat(args[0], args[1]);

			BufferedReader commandLine = new java.io.BufferedReader(new InputStreamReader(System.in));

			while (true) {
				String s = commandLine.readLine();

				if (s.equalsIgnoreCase("exit")) {
					chat.close();
					System.exit(0);
				} else {
					chat.writeMessage(args[2] + ": " + s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

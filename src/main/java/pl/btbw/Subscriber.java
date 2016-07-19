package pl.btbw;

import javax.jms.*;
import javax.naming.InitialContext;

public class Subscriber implements MessageListener {

	public static void main(String[] args) {
		new Subscriber("RedTopic", "someTopicName");
	}

	public Subscriber(String topicFactory, String topicName) {
		try {

			InitialContext ctx = new InitialContext(PropertiesUtil.getNoFileProperties());

			TopicConnectionFactory connectionFactory = (TopicConnectionFactory) ctx.lookup(topicFactory);

			Topic chatTopic = (Topic) ctx.lookup(topicName);

			TopicConnection topicConnection = connectionFactory.createTopicConnection();
//			topicConnection.setClientID("2");

			TopicSession subscriberSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			TopicSubscriber subscriber = subscriberSession.createSubscriber(chatTopic, null, true);
//			TopicSubscriber subscriber = subscriberSession.createDurableSubscriber(chatTopic, "Subscriber1");

			subscriber.setMessageListener(this);

			topicConnection.start();

		} catch (Exception e) {
			// this is sooo bad!
			e.printStackTrace();
		}
	}

	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println(textMessage.getText());
		} catch (JMSException e) {
			// this is sooo bad!
			e.printStackTrace();
		}
	}
}

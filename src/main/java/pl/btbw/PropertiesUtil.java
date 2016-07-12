package pl.btbw;

import javax.naming.Context;
import java.util.Properties;

public class PropertiesUtil {

	public static Properties getNoFileProperties() {
		Properties properties = new Properties();
		properties.put(Context.SECURITY_PRINCIPAL, "system");
		properties.put(Context.SECURITY_CREDENTIALS, "manager");
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

		properties.put("connectionFactoryNames", "RedTopic");
		properties.put("topic.someTopicName", "jms.someTopicName");

		return properties;
	}

}

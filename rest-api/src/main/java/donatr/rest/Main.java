package donatr.rest;

import donatr.common.AccountCreatedEvent;
import donatr.common.AccountServletContextListener;
import donatr.common.CreateAccountCommand;
import donatr.rest.command.AccountCommandController;
import donatr.rest.command.CreateAccountRequest;
import donatr.rest.query.AccountQueryController;
import donatr.rest.response.CreateAccountResponse;
import donatr.rest.websocket.AccountWebsocketServer;
import donatr.rest.websocket.AccountWebsocketSessionHandler;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

public class Main {
	public static void main(String[] args) throws Exception {
		Container container = new Container();

		container.start();

		JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "rest-api.war");
		deployment.addClasses(RestApiEventBinder.class);
		deployment.addClasses(CreateAccountCommand.class);
		deployment.addClasses(AccountCreatedEvent.class);
		deployment.addClasses(AccountCreatedEvent.class);
		deployment.addClasses(CreateAccountRequest.class);
		deployment.addClasses(CreateAccountResponse.class);
		deployment.addClasses(AccountServletContextListener.class);
		deployment.addAsWebInfResource(new StringAsset("<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"    xsi:schemaLocation=\"\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" +
				"</beans>"), "beans.xml");
		deployment.addResource(AccountWebsocketSessionHandler.class);
		deployment.addResource(AccountWebsocketServer.class);
		deployment.addResource(AccountQueryController.class);
		deployment.addResource(AccountCommandController.class);
		deployment.addAllDependencies();

		container.deploy(deployment);

	}
}

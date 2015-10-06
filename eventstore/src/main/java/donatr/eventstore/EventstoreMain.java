package donatr.eventstore;

import donatr.common.AccountCreatedEvent;
import donatr.common.AccountServletContextListener;
import donatr.common.CreateAccountCommand;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.Datasource;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.datasources.Driver;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.transactions.TransactionsFraction;

public class EventstoreMain {
	public static void main(String[] args) throws Exception {
		Container container = new Container();

		container.subsystem(new DatasourcesFraction()
						.driver(new Driver("h2")
								.datasourceClassName("org.h2.Driver")
								.xaDatasourceClassName("org.h2.jdbcx.JdbcDataSource")
								.module("com.h2database.h2"))
						.datasource(new Datasource("MyDS")
								.driver("h2")
								.connectionURL("jdbc:h2:mem:eventstore;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
								.authentication("sa", "sa"))
		);

		// Prevent JPA Fraction from installing it's default datasource fraction
		container.fraction(new JPAFraction()
						.inhibitDefaultDatasource()
						.defaultDatasourceName("MyDS")
		);

		container.subsystem(new TransactionsFraction(4714, 4715));

		container.start();

		JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "eventstore.war");
		deployment.addClasses(EventstoreEventBinder.class);
		deployment.addClasses(CreateAccountCommand.class);
		deployment.addClasses(AccountCreatedEvent.class);
		deployment.addClasses(EventRepository.class);
		deployment.addResource(EventstoreConsumer.class);
		deployment.addResource(EventEntry.class);
		deployment.addClasses(AccountServletContextListener.class);
		deployment.addAsWebInfResource(new StringAsset("<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"    xsi:schemaLocation=\"\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" +
				"</beans>"), "beans.xml");
		deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", EventstoreMain.class.getClassLoader()), "classes/META-INF/persistence.xml");
		deployment.addAllDependencies();

		container.deploy(deployment);
	}
}

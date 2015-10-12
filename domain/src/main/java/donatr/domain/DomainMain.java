package donatr.domain;

import donatr.common.AccountServletContextListener;
import donatr.common.DomainConfig;
import donatr.common.axon.DonatrCommandBus;
import donatr.common.axon.DonatrCommandGateway;
import donatr.common.axon.DonatrEventBus;
import donatr.common.axon.DonatrEventStore;
import donatr.domain.account.aggregate.Account;
import donatr.domain.account.aggregate.AccountType;
import donatr.domain.account.aggregate.Transaction;
import donatr.domain.account.command.*;
import donatr.domain.account.event.*;
import donatr.domain.account.handler.AccountEventHandler;
import donatr.domain.account.query.AccountQueryResource;
import donatr.domain.account.repository.AccountRepository;
import donatr.domain.account.repository.DonatrRepository;
import donatr.domain.account.repository.DonatrRepositoryProducer;
import donatr.domain.account.repository.TransactionRepository;
import donatr.domain.account.request.ChangeAccountEmailRequest;
import donatr.domain.account.request.ChangeAccountTypeRequest;
import donatr.domain.account.request.CreateAccountRequest;
import donatr.domain.account.request.CreateTransactionRequest;
import donatr.domain.account.response.GetAccountResponse;
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

public class DomainMain {
	public static void main(String[] args) throws Exception {
		Container container = new Container();

		container.subsystem(new DatasourcesFraction()
						.driver(new Driver("postgres")
								.datasourceClassName("org.postgresql.Driver")
								.xaDatasourceClassName("org.postgresql.xa.PGXADataSource")
								.module("org.postgresql"))
						.datasource(new Datasource("MyDS")
								.driver("postgres")
								.connectionURL("jdbc:postgresql://localhost:5432/donatr")
								.authentication("donatr", "donatr"))
		);

		// Prevent JPA Fraction from installing it's default datasource fraction
		container.fraction(new JPAFraction()
						.inhibitDefaultDatasource()
						.defaultDatasourceName("MyDS")
		);

		container.subsystem(new TransactionsFraction(4712, 4713));

		container.start();

		JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "domain.war");
		deployment.addClasses(DomainConfig.class);
		deployment.addClasses(DonatrCommandGateway.class);
		deployment.addClasses(DonatrEventStore.class);
		deployment.addClasses(DonatrCommandBus.class);
		deployment.addClasses(DonatrEventBus.class);
		deployment.addClasses(DonatrRepository.class);
		deployment.addClasses(DonatrRepositoryProducer.class);
		deployment.addClasses(AccountRepository.class);
		deployment.addClasses(DomainEventBinder.class);
		deployment.addClasses(CreateAccountCommand.class);
		deployment.addClasses(ChangeAccountEmailCommand.class);
		deployment.addClasses(AccountEmailChangedEvent.class);
		deployment.addClasses(AccountEventHandler.class);
		deployment.addClasses(AccountCreatedEvent.class);
		deployment.addClasses(CreateTransactionCommand.class);
		deployment.addClasses(ChangeAccountTypeCommand.class);
		deployment.addClasses(AccountType.class);
		deployment.addClasses(ChangeAccountTypeRequest.class);
		deployment.addClasses(AccountTypeChangedEvent.class);
		deployment.addClasses(TransactionCreatedEvent.class);
		deployment.addClasses(DebitAccountCommand.class);
		deployment.addClasses(AccountDebitedEvent.class);
		deployment.addClasses(CreditAccountCommand.class);
		deployment.addClasses(AccountCreditedEvent.class);
		deployment.addClasses(CreateAccountRequest.class);
		deployment.addClasses(ChangeAccountEmailRequest.class);
		deployment.addClasses(CreateTransactionRequest.class);
		deployment.addClasses(GetAccountResponse.class);
		deployment.addClasses(TransactionRepository.class);
		deployment.addResource(Account.class);
		deployment.addResource(Transaction.class);
		deployment.addResource(AccountCommandResource.class);
		deployment.addResource(AccountQueryResource.class);
		deployment.addClasses(AccountServletContextListener.class);
		deployment.addAsWebInfResource(new StringAsset("<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"    xsi:schemaLocation=\"\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" +
				"</beans>"), "beans.xml");
		deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", DomainMain.class.getClassLoader()), "classes/META-INF/persistence.xml");
		deployment.addAllDependencies();

		container.deploy(deployment);
	}
}

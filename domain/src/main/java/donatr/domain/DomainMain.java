package donatr.domain;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.wildfly.swarm.config.datasources.subsystem.dataSource.DataSource;
import org.wildfly.swarm.config.datasources.subsystem.jdbcDriver.JdbcDriver;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.datasources.DatasourcesFraction;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.jpa.JPAFraction;
import org.wildfly.swarm.transactions.TransactionsFraction;
import org.wildfly.swarm.undertow.WARArchive;

public class DomainMain {
	public static void main(String[] args) throws Exception {
		Container container = new Container();

		container.subsystem(new DatasourcesFraction()
						.jdbcDriver(new JdbcDriver("postgresql")
								.driverName("postgresql")
								.driverDatasourceClassName("org.postgresql.Driver")
								.xaDatasourceClass("org.postgresql.xa.PGXADataSource")
								.driverModuleName("org.postgresql"))
						.dataSource(new DataSource("MyDS").jndiName("java:jboss/naming/context/MyDS")
								.driverName("postgresql")
								.connectionUrl("jdbc:postgresql://localhost:5432/donatr")
								.userName("donatr")
								.password("donatr"))
		);

		// Prevent JPA Fraction from installing it's default datasource fraction
		container.fraction(new JPAFraction()
						.inhibitDefaultDatasource()
						.defaultDatasource("MyDS")
		);

		container.subsystem(new TransactionsFraction());

		container.start();

		/*container
				.deploy(ArtifactManager.artifact("org.postgresql:postgresql:9.4-1201-jdbc41", "postgresql"));*/

		/*JARArchive dsArchive = ShrinkWrap.create(JARArchive.class, "datasource.war");
		dsArchive.as(DatasourceArchive.class).datasource(
				new DataSource("MyDS")
						.driverName("postgresql")
						.datasourceClass("org.postgresql.Driver")
						.connectionUrl("jdbc:postgresql://localhost:5432/donatr")
						.userName("donatr")
						.password("donatr")
		);
		container.deploy(dsArchive);*/


		JAXRSArchive deployment = ShrinkWrap.create(JAXRSArchive.class, "domain.war");
		deployment.setContextRoot("/api");
		deployment.addPackages(true, "donatr.common");
		deployment.addPackages(true, "donatr.domain");
		deployment.addAsWebInfResource(new StringAsset("<beans xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"    xsi:schemaLocation=\"\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee\n" +
				"        http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd\" bean-discovery-mode=\"all\">\n" +
				"</beans>"), "beans.xml");
		deployment.addAsWebInfResource(new ClassLoaderAsset("META-INF/persistence.xml", DomainMain.class.getClassLoader()), "classes/META-INF/persistence.xml");
		deployment.addAllDependencies();
		container.deploy(deployment);

		WARArchive warArchive = ShrinkWrap.create( WARArchive.class, "static.war" );
		warArchive.staticContent("/", "public");
		container.deploy(warArchive);
	}
}

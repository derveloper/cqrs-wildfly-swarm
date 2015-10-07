package donatr.web;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.undertow.WARArchive;

public class WebMain {
	public static void main(String[] args) throws Exception {

		Container container = new Container();

		WARArchive deployment = ShrinkWrap.create(WARArchive.class, "web.war");

		deployment.staticContent("/");

		container.start().deploy(deployment);
	}
}

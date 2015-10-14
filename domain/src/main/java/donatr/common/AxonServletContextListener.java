package donatr.common;

import donatr.domain.DomainEventBinder;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class AxonServletContextListener implements ServletContextListener {
	@Inject
	DomainEventBinder domainConfig;

	public void contextInitialized(ServletContextEvent e) {
		domainConfig.initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}

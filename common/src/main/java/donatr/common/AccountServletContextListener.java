package donatr.common;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class AccountServletContextListener implements ServletContextListener {
	@Inject
	DomainConfig domainConfig;

	public void contextInitialized(ServletContextEvent e) {
		domainConfig.initialize();
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}

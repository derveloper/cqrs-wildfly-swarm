package cc.vileda.cqrs.common;

import com.zanox.rabbiteasy.cdi.EventBinder;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;


@WebListener
public class AccountServletContextListener implements ServletContextListener {
	@Inject
	EventBinder eventBinder;

	public void contextInitialized(ServletContextEvent e) {
		try {
			eventBinder.initialize();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}
}

package interviewui;

import javax.servlet.*;



import javax.servlet.annotation.WebListener;


@WebListener()
public class DynamicAddListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Utilities loadPropertiesFile = new Utilities();
		event.getServletContext().setAttribute("URL", loadPropertiesFile.getPropertyValue("ServiceUrl","config.properties"));

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}

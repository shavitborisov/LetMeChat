package ajax;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MySiteListener implements ServletContextListener {
    public MySiteListener() {
        // TODO Auto-generated constructor stub
    }
    
    public void contextInitialized(ServletContextEvent event) { 
    	ServletContext application = event.getServletContext();
    	application.setAttribute("manager", new ChatRooms());
	}
    
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub	
	}
}
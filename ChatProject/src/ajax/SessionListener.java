package ajax;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import ajax.ChatRooms.Room;

@WebListener
public class SessionListener implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("Session destroyed: " + arg0.getSession().getId());
		if (arg0.getSession().getAttribute("currRoom") != null)
    		((Room) arg0.getSession().getAttribute("currRoom")).leave(arg0.getSession());
	}
}
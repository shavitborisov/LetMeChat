package ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import myDB.UserInfo;
import ajax.ChatRooms.Room;

@WebServlet(asyncSupported = true, urlPatterns = { "/ChatListener" })
public class ChatListener extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public ChatListener() {
    	super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = request.getParameter("code");
		if (msg == null)
			return;
		
		ChatRooms manager = (ChatRooms) this.getServletContext().getAttribute("manager");
		HttpSession session = request.getSession();
		Room room = (Room) session.getAttribute("currRoom");
		if ((room == null) && (!msg.equals("new")))
			return;
		PrintWriter out = response.getWriter();
		
		switch (msg) {
			case "new":
				
				if (room != null)
					room.leave(session);
				
				room = manager.getAvailableNewRoom(session);
				session.setAttribute("currRoom", room);
				out.println("<p class='statusMsg'>Chat room was succesfully allocated, waiting for stranger...</p>");
				break;
				
			case "getPartner":
				
				while (room.status() != Room.READY)
					sleep(10);
				
				out.println("<p class='statusMsg'>Stranger was found, waiting for acknowledgment...</p>");
				break;
				
			case "getPartnerAck":
				
				room.setAcknowledge(session);
				while (room.status() != Room.ACTIVE)
					sleep(10);
				
				HttpSession partnerSession = room.getOtherPartnerSession(session);
				UserInfo partnerInfo = (UserInfo) partnerSession.getAttribute("info");
				
				String partnerName = partnerInfo.getName();
				boolean partnerAgeDisp = partnerInfo.ageDisp();
				int partnerAge = partnerInfo.getAge();
				String partnerBio = partnerInfo.getBio();
				String partnerSex = partnerInfo.getSex();
				boolean partnerMailDisp = partnerInfo.mailDisp();
				String partnerMail = partnerInfo.getMail();
				
				String display = "<p class='statusMsg'>Say hi!</p>";
				display += "<p class='statusMsg'>Name: <span class='statusInfo'>" + partnerName + "</span></p>";
				
				display += "<p class='statusMsg'>Sex: <span class='statusInfo'>";
				if (partnerSex.equals("male"))
					display += "<img class='infoImg' src='http://i57.tinypic.com/2nqqomf.png'>";
				else
					display += "<img class='infoImg' src='http://i58.tinypic.com/66cfb9.png'>";
				display += "</span></p>";
				
				if (partnerAgeDisp)
					display += "<p class='statusMsg'>Age: <span class='statusInfo'>" + partnerAge + " years old.</span></p>";
				
				display += "<p class='statusMsg'>" + partnerName + "'s bio: <span class='statusInfo'>" + partnerBio + "</span></p>";
				
				if (partnerMailDisp)
					display += "<p class='statusMsg'>Contact " + partnerName + ": <span class='statusInfo'>" + partnerMail + "</span></p>";
				
				out.println(display);
				break;
				
			case "getMsg":
		 			
				while ((room.status() == Room.ACTIVE) && (room.noMessages(session)))
	 				sleep(10);
				
	 			if (room.status() == Room.ACTIVE)
				    out.println(room.getNewMessages(session));
	 			else {
	 				response.setStatus(HttpServletResponse.SC_GONE);
	 				out.print("<p class='statusMsg'>Stranger left the conversation...</p>");
	 			}
	 			
	 			break;
		 			
			case "setMsg":
		 		
				room.setNewMessage(session, request.getParameter("data"));
				break;
				
			case "leave":
				
				if (room != null)
					room.leave(session);
				
				break;
		}
	}
	
	private void sleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
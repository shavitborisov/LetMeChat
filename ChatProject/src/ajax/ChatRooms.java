package ajax;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import myDB.UserInfo;

public class ChatRooms {
	
	final static int MAX_ROOMS = 10;
	Room[] rooms;
	LinkedList<Room> waitingList;
	
	public ChatRooms() {
		rooms = new Room[MAX_ROOMS];
		for (int i = 0; i < rooms.length; i++)
			rooms[i] = new Room();
		this.waitingList = new LinkedList<Room>();
	}
	
	public Room getAvailableNewRoom(HttpSession session) throws ServletException {
		Room room = null;
		if (waitingList.isEmpty()) {
			for (int i = 0; i < rooms.length; ++i) // searching for a free room
				if (rooms[i].status == Room.FREE) {
					room = rooms[i];
					break;
				}
			if (room == null)
				throw new ServletException();
		}
		else
			room = waitingList.pollFirst();
		
		room.enter(session);
		return room;
	}
	
	public class Room {
		final static int FREE = 0;
		final static int WAITING = 1;
		final static int READY = 2;
		final static int ACTIVE = 3;
		final static int MIN_PARTNERS = 2;
		
		private int status;
		private int ackCount;
		HashMap <String, HttpSession> chatters;
		
		public Room() {
			status = FREE;
			ackCount = 0;
			chatters = new HashMap<String, HttpSession>();
		}
		
		public void enter(HttpSession session) throws ServletException {
			synchronized(this) {
				switch (chatters.size()) {
					case 0:
						status = WAITING;
						waitingList.addLast(this);
						break;
					case 1:
						status = READY;
						break;
					default:
						throw new ServletException();
				}
				
				chatters.put(session.getId(), session);
				LinkedList<String> messages = new LinkedList<String>();
				session.setAttribute("messages", messages);
			}
		}
		
		public void leave(HttpSession session) {
			synchronized(this) {
				switch (this.status) {
					case ACTIVE:
					case READY:
						this.status = WAITING;
						waitingList.addLast(this);
						break;
					case WAITING:
						this.status = FREE;
						break;
				}
				
				chatters.remove(session.getId());
				@SuppressWarnings("unchecked")
				LinkedList<String> messages = (LinkedList<String>) session.getAttribute("messages");
				if (messages != null)
					messages.clear();
				session.removeAttribute("messages");
				session.removeAttribute("currRoom");
				this.ackCount = 0;
			}
		}
		
		public String getOtherPartnerName(HttpSession mySession) throws ServletException {
			synchronized(this) {
				Iterator<String> it = chatters.keySet().iterator();
				while (it.hasNext()) {
					String sesID = (String) it.next();
					if (mySession.getId() != sesID)
						return (String) ((UserInfo) chatters.get(sesID).getAttribute("info")).getName();
				}
				throw new ServletException();
			}
		}
		
		public HttpSession getOtherPartnerSession(HttpSession mySession) {
			synchronized(this) {
				Iterator<String> it = chatters.keySet().iterator();
				while (it.hasNext()) {
					String sesID = (String) it.next();
					if (mySession.getId() != sesID)
						return ((HttpSession) chatters.get(sesID));
				}
			}
			return null;
		}
		
		public String setAcknowledge(HttpSession mySession) {
			String otherPartner = null;
			Iterator<String> it = chatters.keySet().iterator();
			while (it.hasNext()) {
				String sesID = (String) it.next();
				if (mySession.getId() == sesID)
					ackCount++;
				else
					otherPartner = (String) ((UserInfo) chatters.get(sesID).getAttribute("info")).getName();
			}
			
			if (ackCount == MIN_PARTNERS)
				status = ACTIVE;
			return otherPartner;
		}
		
		public String getNewMessages(HttpSession mySession) {
			String res = "";
			if (mySession == chatters.get(mySession.getId())) {
				@SuppressWarnings("unchecked")
				LinkedList<String> messages = (LinkedList<String>) mySession.getAttribute("messages");
				while (!messages.isEmpty())
					res += messages.pollFirst() ;
			}
			return res;
		}
		
		public boolean noMessages(HttpSession session) {
			if (session == chatters.get(session.getId())) {
				@SuppressWarnings("unchecked")
				LinkedList<String> messages = (LinkedList<String>) session.getAttribute("messages");
				return (messages.isEmpty());
			}
			else return true;
		}
		
		@SuppressWarnings("unchecked")
		public void setNewMessage(HttpSession session, String msg) {
			HttpSession mySession = null;
			HttpSession hisSession = null;
			
			Iterator<String> it = chatters.keySet().iterator();
			while (it.hasNext()) {
				String sesID = (String) it.next();
				if (session.getId() == sesID)
					mySession = session;
				else
					hisSession = chatters.get(sesID);
			}
			
			ZonedDateTime t = ZonedDateTime.now();
			String time = t.format(DateTimeFormatter.ofPattern("k:mm"));
			
			String myMsg = "<pre class='me'>[" + time + "] " + ((UserInfo) mySession.getAttribute("info")).getName() + ": "  + msg + "</pre>";
			((LinkedList<String>) mySession.getAttribute("messages")).add(myMsg);
			
		    String hisMsg = "<pre class='him'>[" + time + "] " + ((UserInfo) mySession.getAttribute("info")).getName() + ": " + msg + "</pre>";
		    ((LinkedList<String>) hisSession.getAttribute("messages")).add(hisMsg);
		}
		
		public int status() {
			return this.status;
		}
	}
}
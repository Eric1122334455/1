
public class Event extends Post {

	// number of events, used to create event id
	protected static int count;
	
	// the location of the event
	protected String venue;
	
	// the date when the event happens
	protected String date;
	
	// the maximum number of attendees of the event
	protected int capacity;
	
	// the current number of participants in the event
	protected int attendeeCount;
	
	/**
	 * Constructor
	 */
	public Event(String name, String description, String creatorId, 
			String venue, String date, int capacity) {
		super(name, description, creatorId);
		count++;
		this.id = "EVE" + String.format("%03d", count);
		this.venue = venue;
		this.date = date;
		this.capacity = capacity;
		this.attendeeCount = 0;
	}
	
	@Override 
	public String getPostDetails() {
		String details = super.getPostDetails();
		
		details += String.format("%-16s", "Venue:") + venue + "\n";
		details += String.format("%-16s", "Date:") + date + "\n";
		details += String.format("%-16s", "Capacity:") + capacity + "\n";
		details += String.format("%-16s", "Attendees:") + attendeeCount + "\n";
		
		return details;
	}
	
	@Override
	public boolean handleReply(Reply reply) {
		// the event is not full
		if (status == Status.CLOSED)
			return false;
		
		// the student id is not yet recorded in this event
		for (Reply record: replies) {
			if (record.getResponderId().equals(reply.getResponderId()))
				return false;
		}
		
		// add this reply object to the replies collection
		replies.add(reply);
		attendeeCount++;
		
		// When the event is full, set event status to CLOSED
		if (attendeeCount == capacity) 
			status = Status.CLOSED;
		
		return true;
	}

	@Override
	public String getReplyDetails() {
		String details = "";
		details += String.format("%-16s", "Attendee list:");
		if (replies.isEmpty())
			details += "Empty\n";
		else {
			for (int i = 0; i < replies.size(); i++) {
				details += replies.get(i).getResponderId();
				if (i != replies.size()-1)
					details += ",";
			}
			details += "\n";
		}
		return details;
	}

	
	// Accessor and mutator methods
	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getAttendeeCount() {
		return attendeeCount;
	}

	public void setAttendeeCount(int attendeeCount) {
		this.attendeeCount = attendeeCount;
	}
	
}


public class Sale extends Post {
	
	// number of sales, used to create sale id
	protected static int count;
	
	// the price at which the post creator wants to sell the item
	protected double askingPrice;
	
	// the highest price offered among all replies from students
	protected double highestOffer;
	
	// the minimum amount by which a new offer price in a reply 
	// must be higher than the highest offer recorded in this sale post
	protected double minimumRaise;
	
	/**
	 * Constructor
	 */
	public Sale(String name, String description, String creatorId, 
			double askingPrice, double minimumRaise) {
		super(name, description, creatorId);
		count++;
		this.id = "SAL" + String.format("%03d", count);
		this.askingPrice = askingPrice;
		this.minimumRaise = minimumRaise;
		this.highestOffer = 0;
	}
	
	@Override
	public String getPostDetails() {
		String details = super.getPostDetails();
		
		details += String.format("%-16s", "Minimum raise:") + "$" + String.format("%.2f", minimumRaise) + "\n";
		details += String.format("%-16s", "Highest offer:");
		if (replies.isEmpty())
			details += "NO OFFER\n";
		else
			details += String.format("%.2f", highestOffer) + "\n";
		
		return details;
	}

	@Override
	public boolean handleReply(Reply reply) {
		if (status == Status.CLOSED)
			return false;
		if (reply.getValue() < highestOffer + minimumRaise)
			return false;
		else {
			replies.add(reply);
			highestOffer = reply.getValue();
		}
		
		// close the post if the price offer is
		// greater than or equal to the asking price
		if (highestOffer >= askingPrice)
			status = Status.CLOSED;
		
		return true;
	}

	@Override
	public String getReplyDetails() {
		String details = "";
//		details += String.format("%-16s", "Asking price:") + "$" + String.format("%.2f", askingPrice) + "\n\n";
		if (replies.isEmpty()) {
			details += String.format("%-16s", "Offer History:") + "Empty\n";
		} else {
			details += "-- Offer History --\n"; 
			for (int i = replies.size()-1; i >=0; i--) {
				Reply reply = replies.get(i);
				details += reply.getResponderId() + ": $" + String.format("%.2f", reply.getValue()) + "\n";
			}
		}
		return details;
	}

	// Accessor and mutator methods
	public double getAskingPrice() {
		return askingPrice;
	}

	public void setAskingPrice(double askingPrice) {
		this.askingPrice = askingPrice;
	}

	public double getHighestOffer() {
		return highestOffer;
	}

	public void setHighestOffer(double highestOffer) {
		this.highestOffer = highestOffer;
	}

	public double getMinimumRaise() {
		return minimumRaise;
	}

	public void setMinimumRaise(double minimumRaise) {
		this.minimumRaise = minimumRaise;
	}	

}

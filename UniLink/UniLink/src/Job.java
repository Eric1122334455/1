
public class Job extends Post {

	// number of jobs, used to create job id
	protected static int count;
		
	// the maximum amount of money the job post creator is willing to paid
	protected double proposedPrice;
		
	// the lowest amount offered among all replies from students
	protected double lowestOffer;
	
	/**
	 * Constructor
	 */
	public Job(String name, String description, String creatorId, 
			double proposedPrice) {
		super(name, description, creatorId);
		count++;
		this.id = "JOB" + String.format("%03d", count);
		this.proposedPrice = proposedPrice;
	}
	
	@Override
	public String getPostDetails() {
		String details = super.getPostDetails();
		
		details += String.format("%-16s", "Proposed price:") + "$" + String.format("%.2f", proposedPrice) + "\n";
		details += String.format("%-16s", "Lowest offer:");
		if (replies.isEmpty())
			details += "NO OFFER\n";
		else
			details += String.format("%.2f", lowestOffer) + "\n";
		
		return details;
	}
	
	@Override
	public boolean handleReply(Reply reply) {
		if (status == Status.CLOSED)
			return false;
		
		if (reply.getValue() > proposedPrice)
			return false;
		
		if (!replies.isEmpty() && reply.getValue() >= lowestOffer)
			return false;
		
		// the new offer price is less than the current lowest offer
		replies.add(reply);
		// set the current lowest offer value to the new offer price
		lowestOffer = reply.getValue();
		
		return true;
	}

	@Override
	public String getReplyDetails() {
		String details = "";
		if (replies.isEmpty()) {
			details += String.format("%-16s", "Offer History:") + "Empty\n";
		} else {
			details += "\n-- Offer History --\n";
			for (int i = replies.size()-1; i >=0; i--) {
				Reply reply = replies.get(i);
				details += reply.getResponderId() + ": $" + String.format("%.2f", reply.getValue()) + "\n";
			}
		}
		return details;
	}

	
	// Accessor and mutator methods
	public double getProposedPrice() {
		return proposedPrice;
	}

	public void setProposedPrice(double proposedPrice) {
		this.proposedPrice = proposedPrice;
	}

	public double getLowestOffer() {
		return lowestOffer;
	}

	public void setLowestOffer(double lowestOffer) {
		this.lowestOffer = lowestOffer;
	}

}

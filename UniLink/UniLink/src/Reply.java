
public class Reply {
	
	// the id of the post which the reply is associated with
	protected String postId;

	// Willing/Prices/Amount of money for event/sale/job poster
	protected double value;
	
	// the id of the student who replies
	protected String responderId;
	
	/**
	 * Constructor
	 */
	public Reply(String postId, double value, String responderId) {
		this.postId = postId;
		this.value = value;
		this.responderId = responderId;
	}

	// accessor and mutator methods
	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getResponderId() {
		return responderId;
	}

	public void setResponderId(String responderId) {
		this.responderId = responderId;
	}
	
}

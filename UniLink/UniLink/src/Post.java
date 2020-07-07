import java.util.ArrayList;

public abstract class Post {

	enum Status {
		OPEN, CLOSED
	}
	
	// a string which uniquely identifies each post
	protected String id;
	// a short string to summarise the post
	protected String title;
	// a longer string which provides more information about the post
	protected String description;
	// a string which is the id of the student who created the post
	protected String creatorId;
	// indicates whether the post is open to receive replies or closed 
	// and no longer accepts replies
	protected Status status;
	// store all replies to a post
	protected ArrayList<Reply> replies;
	
	/**
	 * Constructor, initialize all attributes
	 * It can only be called in constructors of subclasses of the Post
	 */
	protected Post() {
		this.id = "";
		this.title = "";
		this.description = "";
		this.creatorId = "";
		this.status = Status.OPEN;
		this.replies = new ArrayList<Reply>();
	}
	
	protected Post(String title, String description, String creatorId) {
		this.id = "";
		this.title = title;
		this.description = description;
		this.creatorId = creatorId;
		this.status = Status.OPEN;
		this.replies = new ArrayList<Reply>();
	}
	
	/**
	 * This method builds and returns a string which contains 
	 * information about a post
	 * @return a string which contains information about a post
	 */
	public String getPostDetails() {
		String details = "";
		details += String.format("%-16s", "ID:") + id + "\n";
		details += String.format("%-16s", "Title:") + title + "\n";
		details += String.format("%-16s", "Description:") + description + "\n";
		details += String.format("%-16s", "Creator ID:") + creatorId + "\n";
		details += String.format("%-16s", "Status:") + status + "\n";
		
		return details;
	}
	
	/**
	 * Each type of post has a different way to handle a reply
	 */
	public abstract boolean handleReply(Reply reply);
	
	/**
	 * This method should only be called by the post creator on their 
	 * own posts. 
	 * @return a String containing details of all replies to that post.
	 */
	public abstract String getReplyDetails();

	
	
	// Accessor and mutator methods
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ArrayList<Reply> getReplies() {
		return replies;
	}

	public void setReplies(ArrayList<Reply> replies) {
		this.replies = replies;
	}
	
}

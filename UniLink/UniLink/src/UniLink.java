import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UniLink {

	private static List<Post> posts = new ArrayList<Post>();
	private static String curStu = "";  // Current user name
	
	public static void main(String[] args) {
		
		addTestPosts();
		
		boolean quit = false;
		Scanner scanner = new Scanner(System.in);
		
		while (!quit) {
			showUniLinkMenu();
			
			int option = 0;
			while (true) {
				System.out.print("Enter your choice: ");
				option = parseIntegerNumber(scanner);
				if (option != 1 && option != 2) {
					System.out.println("Invalid input");
					System.out.println("You must enter a number in the range [1, 2]");
				}
				else
					break;
			}
			
			if (option == 1) {
				curStu = parseNonEmptyString(scanner, "Enter username: ");
				startStudentMenu(scanner);
				
			} else if (option == 2) {
				quit = true;
			}
		}
	}
	
	private static void startStudentMenu(Scanner scanner) {
		System.out.println("Welcome " + curStu + "!");
		
		boolean quit = false;
		while (!quit) {
			
			showStudentMenu();
			
			// ask user to input a valid option
			int option = -1;
			while (true) {
				System.out.print("Enter your choice: ");
				option = parseIntegerNumber(scanner);
				if (option < 1 || option > 9) {
					System.out.println("Invalid input");
					System.out.println("You must enter a number in the range [1, 9]");
				}
				else
					break;
			}
			
			switch (option) {
			case 1:
				createNewEventPost(scanner);
				break;
			case 2:
				createNewSalePost(scanner);
				break;
			case 3:
				createNewJobPost(scanner);
				break;
			case 4:
				replyToPost(scanner);
				break;
			case 5:
				displayMyPosts();
				break;
			case 6:
				displayAllPosts();
				break;
			case 7:
				closePost(scanner);
				break;
			case 8:
				deletePost(scanner);
				break;
			case 9:
				quit = true;
				break;
			}
		}
	}
	
	private static void showUniLinkMenu() {
		System.out.println("** UniLink System **");
		System.out.println("1. Log in");
		System.out.println("2. Quit");
	}
	
	private static void showStudentMenu() {
		System.out.println("** Student Menu **");
		System.out.println("1. New Event Post");
		System.out.println("2. New Sale Post");
		System.out.println("3. New Job Post");
		System.out.println("4. Reply To Post");
		System.out.println("5. Display My Posts");
		System.out.println("6. Display All Posts");
		System.out.println("7. Close Post");
		System.out.println("8. Delete Post");
		System.out.println("9. Log Out");
	}
	
	private static void createNewEventPost(Scanner scanner) {
		System.out.println();
		System.out.println("Enter details of the event below:");
		
		// post information
		String name = parseNonEmptyString(scanner, "Name: ");
		String description = parseNonEmptyString(scanner, "Description: ");
		String venue = parseNonEmptyString(scanner, "Venue: ");
		String date = parseDate(scanner, "Date: ");
		// capacity
		int capacity = -1;
		while (true) {
			System.out.print("Capacity: ");
			capacity = parseIntegerNumber(scanner);
			if (capacity <= 0) {
				System.out.println("Invalid input");
				System.out.println("You must input a positive integer");
			}
			else
				break;
		}
		Post event = new Event(name, description, curStu, venue, date, capacity);
		posts.add(event);
		System.out.println("Success! Your event has been created with id " + event.getId());
	}
	
	private static void createNewSalePost(Scanner scanner) {
		System.out.println();
		System.out.println("Enter details of the item to sale below:");
		
		// post information
		String name = parseNonEmptyString(scanner, "Name: ");
		String description = parseNonEmptyString(scanner, "Description: ");
		double askingPrice = parseDoubleNumber(scanner, "Asking price: ");
		double minimumRaise = parseDoubleNumber(scanner, "Minimum raise: ");

		Post sale = new Sale(name, description, curStu, askingPrice, minimumRaise);
		posts.add(sale);
		System.out.println("Success! Your new sale has been created with id " + sale.getId());
	}
	
	private static void createNewJobPost(Scanner scanner) {
		System.out.println();
		System.out.println("Enter details of the job below:");
		
		// post information
		String name = parseNonEmptyString(scanner, "Name: ");
		String description = parseNonEmptyString(scanner, "Description: ");
		double proposedPrice = parseDoubleNumber(scanner, "Propose price: ");

		Post job = new Job(name, description, curStu, proposedPrice);
		posts.add(job);
		System.out.println("Success! Your job has been created with id " + job.getId());
	}
	
	private static void replyToPost(Scanner scanner) {
		Post post = choosePostOrQuit(scanner);
		if (post == null)  // quit
			return;

		if (post.getStatus() == Post.Status.CLOSED) {
			System.out.println("This post is already closed. Reply not accepted!");
			return;
		}
		
		if (post.getCreatorId().equals(curStu)) {
			System.out.println("Replying to your own post is invalid!");
			return;
		}
		
		if (post instanceof Event) {
			Event event = (Event)post;
			System.out.println("Name: " + event.getTitle());
			System.out.println("Venue: " + event.getVenue());
			System.out.println("Status: " + event.getStatus());
			while (true) {
				System.out.print("Enter '1' to join event or 'Q' to quit: ");
				String input = scanner.nextLine();
				if (input.equals("1")) {
					boolean ret = post.handleReply(new Reply(post.getId(), 1, curStu));
					if (ret) {
						System.out.println("Event registration accepted!");
					} else {
						System.out.println("Event registration denied! You have already registered.");
					}
					return;
				} else if (input.equals("Q")) {
					return;
				} else {
					System.out.println("Invalid input!");
				}
			}
		}
		
		else if (post instanceof Sale) {
			Sale sale = (Sale)post;
			System.out.println("Name: " + sale.getTitle());
			System.out.println("Highest offer: $" + String.format("%.2f", sale.getHighestOffer()));
			System.out.println("Minimum raise: $" + String.format("%.2f", sale.getMinimumRaise()));
			while (true) {
				System.out.print("Enter your offer or 'Q' to quit: ");
				String input = scanner.nextLine();
				if (input.equals("Q")) {
					return;
				} else {
					double offer = parseDoubleNumber(input);
					if (offer < 0) {
						System.out.println("Invalid input!");
					} else {
						boolean ret = post.handleReply(new Reply(post.getId(), offer, curStu));
						if (ret) {
							if (sale.getAskingPrice() > offer) {
								System.out.println("Your offer has been submitted!");
								System.out.println("However, your offer is below the asking price.");
								System.out.println("The item is still on sale");
							} else {
								System.out.println("Congratulation! The " + sale.getTitle() + " has been sold to you.");
								System.out.println("Please contact the owner " + sale.getCreatorId() + " for more details.");
							}
							return;
						} else {
							System.out.println("Offer not accepted!");
						}
					}					
				} 
			}
		}
		
		else if (post instanceof Job) {
			Job job = (Job)post;
			System.out.println("Name: " + job.getTitle());
			System.out.println("Proposed price: $" + String.format("%.2f", job.getProposedPrice()));
			System.out.println("Lowest offer: $" + String.format("%.2f", job.getLowestOffer()));
			while (true) {
				System.out.print("Enter your offer or 'Q' to quit: ");
				String input = scanner.nextLine();
				if (input.equals("Q")) {
					return;
				} else {
					double offer = parseDoubleNumber(input);
					if (offer < 0) {
						System.out.println("Invalid input!");
					} else {
						boolean ret = post.handleReply(new Reply(post.getId(), offer, curStu));
						if (ret) {
							System.out.println("Offer accepted!");
							return;
						} else {
							System.out.println("Offer not accepted!");
						}
					}					
				} 
			}
		}
	}
	
	private static void displayMyPosts() {
		List<Post> myPosts = new ArrayList<Post>();
		for (Post post : posts) {
			if (post.getCreatorId().equals(curStu))
				myPosts.add(post);
		}
		if (myPosts.isEmpty()) {
			System.out.println("You don't have any post of your own!");
		}
		else {
			System.out.println();
			System.out.println("** MY POSTS **\n");
			for (Post post : myPosts) {
				System.out.println(post.getPostDetails());
				if (post instanceof Sale)
					outputAskingPrice(post);
				System.out.println(post.getReplyDetails());
				
				System.out.println("----------------------------");
			}
			System.out.println();
		}
	}
	
	private static void displayAllPosts() {
		System.out.println();
		System.out.println("** ALL POSTS **\n");
		for (Post post : posts) {
			System.out.println(post.getPostDetails());
			if (post instanceof Sale)
				outputAskingPrice(post);
			System.out.println("----------------------------");
		}
		System.out.println();
	}
	
	private static void closePost(Scanner scanner) {
		Post post = choosePostOrQuit(scanner);
		if (post == null)  // quit
			return;
		else {
			if (!post.getCreatorId().equals(curStu)) {
				System.out.println("Request denied! You are not the owner of this post.");
			} else {
				post.setStatus(Post.Status.CLOSED);
				System.out.println("Succeeded! Post closed");
			}
		}
	}
	
	private static void deletePost(Scanner scanner) {
		Post post = choosePostOrQuit(scanner);
		if (post == null)  // quit
			return;
		else {
			if (!post.getCreatorId().equals(curStu)) {
				System.out.println("Request denied! You are not the owner of this post.");
			} else {
				posts.remove(post);
				System.out.println("Succeeded! Post " + post.getId() + " deleted");
			}
		}
	}
	
	private static void addTestPosts() {
		posts.add(new Event("Movie on Sunday", "Join us to see the movie 1917 this Sunday with"
				+ "a ticket discount applied to group members", "s3", "Village Cinema",
				"26/03/2020", 3));
		posts.add(new Event("Programming Study Group Wednesday night",
				"Let's meet this Wednesday afternoon to finish the programming assignment",
				"s4", "RMIT Library", "15/04/2025", 5));
		posts.add(new Sale("IPad Air 2 64GB", "Excellent working condition, comes with box, "
				+ "cable and charger", "s1", 300.25, 15.00));
		posts.add(new Job("Moving House", "Need a person to help me move my belongings to a "
				+ "new house", "s1", 190.00));
		posts.add(new Job("Fixing TV Antenna", "Looking for someone to fix my TV broken antenna",
				"s2", 80.00));
		posts.add(new Sale("Lightweight hybrid commuter bike", "Recently Service, ready to ride", 
				"s2", 180.00, 10.00));
	}
	
	private static void outputAskingPrice(Post post) {
		if (curStu.equals(post.getCreatorId())) {
			System.out.println(String.format("%-16s", "Asking price:") + "$" + 
					String.format("%.2f", ((Sale)post).getAskingPrice()) + "\n");
		}
	}
	
	private static Post choosePostOrQuit(Scanner scanner) {
		Post retPost = null;
		String input = "";
		while (true) {
			System.out.print("Enter post id or 'Q' to quit: ");
			input = scanner.nextLine();
			if (input.equals("Q")) // quit
				break;
			else {
				for (Post post : posts) {
					if (post.getId().equals(input)) {
						retPost = post;
						break;
					}
				}
				if (retPost != null)
					break;
				else
					System.out.println("Invalid post id! Post not found");
			}
		}
		return retPost;
	}
	
	private static int parseIntegerNumber(Scanner scanner) {
		int num = -1;
		try {
			num = Integer.parseInt(scanner.nextLine());
		} catch (Exception e) {
		} 
		return num;
	}
	
	private static double parseDoubleNumber(String input) {
		double num = -1;
		try {
			num = Double.parseDouble(input);
		} catch (Exception e) {
		} 
		return num;
	}
	
	private static double parseDoubleNumber(Scanner scanner, String prompt) {
		double num = -1;
		while (true) {
			System.out.print(prompt);
			num = Double.parseDouble(scanner.nextLine());
			if (num <= 0) {
				System.out.println("Invalid input! You must input a positive number!");
			}
			else
				break;
		}
		return num;
	}
	
	private static String parseNonEmptyString(Scanner scanner, String prompt) {
		String str = "";
		while (true) {
			System.out.print(prompt);
			str = scanner.nextLine();
			if (str.isEmpty()) {
				System.out.println("Invalid input! You must input a non-empty string!");
			}
			else
				break;
		}
		return str;
	}
	
	private static String parseDate(Scanner scanner, String prompt) {
		String date = "";
		while (true) {
			System.out.print(prompt);
			date = scanner.nextLine();
			if (!date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
				System.out.println("Invalid input! You must input a date in the format dd/mm/yyyy!");
			}
			else
				break;
		}
		return date;
	}
	
}

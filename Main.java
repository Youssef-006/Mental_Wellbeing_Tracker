
public class Main {

    public static void main (String[] args) {

        HealthSystem h = new HealthSystem();
        h.loadUsers(); // load saved users

        while (true) {

            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add New User");
            System.out.println("2. Select User");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            int choice = h.getInt();

            switch (choice) {
                case 1 -> h.addUser();
                case 2 -> h.selectUser();
                case 3 -> {
                    h.saveUsers();
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
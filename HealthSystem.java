import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HealthSystem {

    private ArrayList<Person> users = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private final String USERS_FILE = "users.txt";

    //LOAD USERS
    public void loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2)
                    users.add(new Person(parts[0], parts[1]));
            }
        } catch (IOException e) {
            System.out.println("Error loading users.");
        }
    }

    //SAVE USERS
    public void saveUsers() {
        try (FileWriter fw = new FileWriter(USERS_FILE)) {
            for (Person p : users) {
                fw.write(p.getName() + "," + p.getPassword() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    //ADD USER
    public void addUser() {
        System.out.print("Enter username: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();

        users.add(new Person(name, password));
        saveUsers();

        System.out.println("User added successfully!");
    }

    //SELECT USER
    public void selectUser() {
        if (users.isEmpty()) {
            System.out.println("No users available.");
            return;
        }

        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getName());
        }

        System.out.print("Choose user: ");
        int index = getInt() - 1;

        if (index < 0 || index >= users.size()) {
            System.out.println("Invalid user.");
            return;
        }

        Person selected = users.get(index);

        while (true) {
            System.out.print("Enter password: ");
            String input = sc.nextLine();
            if (selected.checkPassword(input))
                break;
            System.out.println("Wrong password.");
        }

        selected.userDashboard();
    }

    //SAFE INT INPUT 
    public int getInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}

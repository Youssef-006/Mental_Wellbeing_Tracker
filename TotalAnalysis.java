import java.util.Scanner;
public abstract class TotalAnalysis {
    protected double score;
    protected String status;

    public double getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }

    Scanner sc = new Scanner(System.in);

    public void questions () {
    }

    protected double getValidInput(Scanner scanner, int min, int max) {
        while (true) {

            if (scanner.hasNextDouble()) {
                double score = scanner.nextDouble();
                scanner.nextLine();

                if (score >= min && score <= max) {
                    return score;
                } else {
                    System.out.printf("Invalid input. Please enter a number between %d and %d: \n", min, max);
                }
            } else {
                System.out.printf("Invalid input. Please enter a number between %d and %d: \n", min, max);
                scanner.next();
                scanner.nextLine();
            }
        }
    }

    protected double getYesNoInput(Scanner scanner) { //Another way to handle any non - yes/no inputs' error.
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("yes")) {
                return 5.0;
            } else if (input.equalsIgnoreCase("no")) {
                return 1.0;
            } else {
                System.out.println("Invalid input. Please enter 'Yes' or 'No':");
            }
        }
    }

    public void indicator (double score) {

    }

    public void advice (String status) {

    }

}

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Person {

    private String name;
    private String password;
    private String extraNotes;
    private final String dataFile;

    Scanner sc = new Scanner(System.in);

    private final SleepScale sleep = new SleepScale();
    private final StressScale stress = new StressScale();
    private final WaterIntakeScale water = new WaterIntakeScale();
    private final ScreenTimeScale screen = new ScreenTimeScale();
    private final MovementScale movement = new MovementScale();

    private final MoodScale mood = new MoodScale();
    private final FocusScale focus = new FocusScale();


    public Person(String name, String password) {
        this.name = name;
        this.password = password;
        this.dataFile = "data_" + name + ".txt";
    }

    public String getName() { return name; }
    public String getPassword() { return password; }

    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    //USER DASHBOARD
    public void userDashboard() {

        while (true) {
            System.out.println("\n--- User Dashboard (" + name + ") ---");
            System.out.println("1. View previous entries");
            System.out.println("2. Add new daily entry");
            System.out.println("3. Back");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {
                case 1 -> viewEntriesMenu();
                case 2 -> addNewEntry();
                case 3 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    //VIEW ENTRIES
    public void viewEntriesMenu() {
        ArrayList<String> entries = loadAllEntries();

        if (entries.isEmpty()) {
            System.out.println("No entries found.");
            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            System.out.println((i + 1) + ". Day " + (i + 1));
        }

        System.out.print("Choose day: ");
        int day;

        try {
            day = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }

        if (day < 1 || day > entries.size()) {
            System.out.println("Invalid day.");
            return;
        }

        System.out.println("\n" + entries.get(day - 1));
    }

    //ADD NEW DAY
    public void addNewEntry() {
        runAll();
        saveDailyEntry();
    }

    //SAVE DAY
    public void saveDailyEntry() {
        int dayNumber = loadAllEntries().size() + 1;

        try (FileWriter fw = new FileWriter(dataFile, true)) {

            fw.write("=== DAY " + dayNumber + " ===\n");
            fw.write("Sleep: " + sleep.getStatus() + "\n");
            fw.write("Stress: " + stress.getStatus() + "\n");
            fw.write("Water: " + water.getStatus() + "\n");
            fw.write("Screen: " + screen.getStatus() + "\n");
            fw.write("Movement: " + movement.getStatus() + "\n");
            fw.write("Mood: " + mood.getStatus() + "\n");
            fw.write("Focus: " + focus.getStatus() + "\n");
            fw.write("Notes: " + extraNotes + "\n");
            fw.write("=== END ===\n\n");

            System.out.println("Day " + dayNumber + " saved!");

        } catch (IOException e) {
            System.out.println("Error saving entry.");
        }
    }

    //LOAD ALL DAYS
    public ArrayList<String> loadAllEntries() {
        ArrayList<String> entries = new ArrayList<>();
        File f = new File(dataFile);

        if (!f.exists()) return entries;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            StringBuilder sb = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("=== DAY")) {
                    sb = new StringBuilder();
                    sb.append(line).append("\n");
                } else if (line.equals("=== END ===")) {
                    sb.append(line).append("\n");
                    entries.add(sb.toString()); //returns empty string
                } else {
                    sb.append(line).append("\n");
                }
            }
        } catch
        (FileNotFoundException fnd ){
            System.out.println("Could not locate file");
        }
        catch
         (IOException e) {
            System.out.println("Error reading entries.");
        }

        return entries;
    }

    //MAIN ANALYSIS
    public void runAll() {

        System.out.println("\n=== Daily Health Questions ===");

        TotalAnalysis[] baseScales = { sleep, stress, water, screen, movement }; //Runtime Polymorphism

        for (TotalAnalysis t : baseScales) t.questions(); //i //size.totalanalysis

        System.out.print("Extra notes: ");
        extraNotes = sc.nextLine();

        for (TotalAnalysis t : baseScales) t.indicator(t.getScore());

        mood.indicator(sleep, stress);
        focus.indicator(sleep, screen);

        System.out.println("****************************************************************");
        System.out.println("Here Are Your Statistics & Advice For The Day:");
        System.out.println("****************************************************************");

        for (TotalAnalysis t : baseScales) t.advice(t.getStatus());
        mood.advice(mood.getStatus());
        focus.advice(focus.getStatus());
    }

    public void saveDailyEntryFromGUI(double sleepHours, double stressScore,
                                      double waterLiters, double productiveScreen,
                                      double unproductiveScreen, double steps,
                                      String notes) {

        // Create scale objects and set their values
        SleepScale sleep = new SleepScale();
        sleep.score = sleepHours;
        sleep.indicator(sleepHours);

        StressScale stress = new StressScale();
        stress.score = stressScore;
        stress.indicator(stressScore);

        WaterIntakeScale water = new WaterIntakeScale();
        water.score = waterLiters;
        water.indicator(waterLiters);

        ScreenTimeScale screen = new ScreenTimeScale();
        screen.score = unproductiveScreen;
        screen.productiveScreenTime = productiveScreen;
        if (!(productiveScreen == 0 && unproductiveScreen == 0))
            screen.productivePercent = (productiveScreen / (productiveScreen + unproductiveScreen)) * 100;
        else
            screen.productivePercent = 300; // INVALID_PERCENT
        screen.indicator(unproductiveScreen);

        MovementScale movement = new MovementScale();
        movement.score = steps;
        movement.indicator(steps);

        MoodScale mood = new MoodScale();
        mood.indicator(sleep, stress);

        FocusScale focus = new FocusScale();
        focus.indicator(sleep, screen);

        // Save to file
        int dayNumber = loadAllEntries().size() + 1;

        try (FileWriter fw = new FileWriter(dataFile, true)) {
            fw.write("=== DAY " + dayNumber + " ===\n");
            fw.write("Sleep: " + sleep.getStatus() + " (" + sleepHours + " hours)\n");
            fw.write("Stress: " + stress.getStatus() + " (Score: " + String.format("%.2f", stressScore) + "/5)\n");
            fw.write("Water: " + water.getStatus() + " (" + waterLiters + " liters)\n");
            fw.write("Screen: " + screen.getStatus() + " (Productive: " + productiveScreen + "h, Unproductive: " + unproductiveScreen + "h)\n");
            fw.write("Movement: " + movement.getStatus() + " (" + (int)steps + " steps)\n");
            fw.write("Mood: " + mood.getStatus() + "\n");
            fw.write("Focus: " + focus.getStatus() + "\n");
            fw.write("Notes: " + notes + "\n");
            fw.write("=== END ===\n\n");
        } catch (IOException e) {
            System.out.println("Error saving entry: " + e.getMessage());
        }
    }
    

}

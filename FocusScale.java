public class FocusScale extends TotalAnalysis { //composition

    public void indicator(SleepScale sleep, ScreenTimeScale screenTime) {

        // Worst case: Bad sleep + Bad screen habits
        if (sleep.getStatus().equals("LOW_SLEEP") && screenTime.getStatus().equals("BAD_ST")) {
            status = "VERY_LOW_FOCUS";
        }

        // One of them is bad
        else if (sleep.getStatus().equals("LOW_SLEEP") || screenTime.getStatus().equals("BAD_ST")) {
            status = "LOW_FOCUS";
        }

        // Medium: mild screen time OR high sleep (oversleeping)
        else if (screenTime.getStatus().equals("NORMAL_ST") || sleep.getStatus().equals("HIGH_SLEEP")) {
            status = "AVERAGE_FOCUS";
        }

        // Perfect: good sleep + perfect screen time
        else {  // PERFECT_SLEEP + Perfect (screen)
            status = "HIGH_FOCUS";
        }
    }

    @Override
    public void advice(String status) {
        System.out.println("Focus Statistics:");
        System.out.println("By showing your screen time and sleep we calculated that your focus is: " + status);
        switch (status) {

            case "VERY_LOW_FOCUS":
                System.out.println("Reduce distractions and rest a bit.");
                break;

            case "LOW_FOCUS":
                System.out.println("Try a Pomodoro session and remove phone distractions.");
                break;

            case "AVERAGE_FOCUS":
                System.out.println("A clear plan for the day will improve it.");
                break;

            case "HIGH_FOCUS":
                System.out.println("Keep up the healthy habits!");
                break;

            default:
                System.out.println("Unable to determine focus level.");
        }
        System.out.println("*********************************");
    }
}


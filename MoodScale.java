public class MoodScale extends TotalAnalysis { //COMPOSITION


    //overloaded method NOT OVERRIDE!!
    public void indicator(SleepScale sleep, StressScale stress) {

        // HIGH Stress = Usually leads to BAD mood
        if (stress.getStatus().equals("HIGH_STRESS")) {
            if (sleep.getStatus().equals("LOW_SLEEP"))
                status = "VERY_IRRITABLE";   // worst combination
            else
                status = "IRRITABLE";
        }

        // MILD Stress = Neutral to slightly down mood
        else if (stress.getStatus().equals("MILD_STRESS")) {
            if (sleep.getStatus().equals("PERFECT_SLEEP"))
                status = "CALM";
            else
                status = "NEUTRAL";
        }

        // LOW Stress = Good mood
        else { // LOW_STRESS
            if (sleep.getStatus().equals("PERFECT_SLEEP"))
                status = "HAPPY";
            else
                status = "CONTENT";
        }

    }

    @Override
    public void advice(String status) {
        System.out.println("Mood Statistics:");
        System.out.println("By showing your sleep and stress we calculated that your mood is: " + status);
        switch (status) {
            case "VERY_IRRITABLE":
                System.out.println("Try resting early and reducing stressful tasks.");
                break;

            case "IRRITABLE":
                System.out.println("A short walk or breathing exercise can help.");
                break;

            case "NEUTRAL":
                System.out.println("Try some light relaxation or music.");
                break;

            case "CALM":
                System.out.println("You are calm today. Great job managing stress!");
                break;

            case "CONTENT":
                System.out.println("Keep your healthy routine going!");
                break;

            case "HAPPY":
                System.out.println("Keep up the good habits!");
                break;

            default:
                System.out.println("Mood unknown. Try again.");
        }
        System.out.println("*********************************");
    }
}


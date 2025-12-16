public class WaterIntakeScale extends TotalAnalysis implements OverallAnalysis {

    private final double MIN_WATER_INTAKE = 1.5;
    private final double MAX_WATER_INTAKE = 4.5;

    @Override
    public void questions() {
            System.out.println("How much of the water did you drink today? ");
            score = getValidInput(sc, 0, 7);
    }

    @Override
    public void indicator (double score) {
            if (score < MIN_WATER_INTAKE)
                status = "LOW_INTAKE";

            else if (score > MAX_WATER_INTAKE)
                status = "HIGH_INTAKE";

            else
                status = "PERFECT";
    }

    @Override
    public void advice (String status) {
        System.out.println("Water Intake Statistics:");
            if (status.equals("LOW_INTAKE"))
                System.out.println("You drank too little water, try increasing your water intake a little bit more.");

            else if (status.equals("HIGH_INTAKE"))
                System.out.println("You have drank too much water. Slow down and spread your drinking throughout the day.");

            else System.out.println("Good water intake. Keep drinking steadily through the day!");

            System.out.println("*********************************");
        }


    }


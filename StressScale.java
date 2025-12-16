public class StressScale extends TotalAnalysis implements OverallAnalysis {

    private double sum = 0;
    private final double [] choice = new double [5];

    @Override
    public void questions () {
        System.out.println("Did you procrastinate today? (Yes/No):");
        choice[0] = getYesNoInput(sc);

        System.out.println("Did you have an important (ex.Exam/Meeting/Interview) event today? (Yes/No):");
        choice[1] = getYesNoInput(sc);

        System.out.println("How often did feel pressured  (1 - 5): ");
        choice[2] = getValidInput(sc, 1, 5);

        System.out.println("How often did you feel emotionally exhausted today(1 - 5): ");
        choice[3] = getValidInput(sc, 1, 5);

        System.out.println("Do you feel tired even after resting? (Yes/No): ");
        choice[4] = getYesNoInput(sc);

        for (int i = 0; i < 5; i++) {
            this.sum += choice[i];
        }
        score = (this.sum) / 5;
        sum = 0;
    }

    @Override
    public void indicator (double score) {
        if (score < 2) {
            status = "LOW_STRESS";
        }

        else if (score > 3) {
            status = "HIGH_STRESS";
        }

        else {
            status = "MILD_STRESS";
        }

    }

    @Override
    public void advice(String status) {
        System.out.println("Stress Statistics:");
        System.out.println("Your Stress Level is " + score + " out of 5.");

        if (status.equals("HIGH_STRESS"))
            System.out.println("It is very high, try drinking a cup of chamomile and take a break to clear your thoughts.");

        else if (status.equals("MILD_STRESS"))
            System.out.println("It's mild, try some deep breathing exercises or a short walk outside to relax and recharge.");

        else
            System.out.println("Perfect! You're doing great—keep up healthy habits like regular exercise and good sleep to stay balanced.");
        System.out.println("*********************************");
    }

}

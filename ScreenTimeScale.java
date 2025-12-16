public class ScreenTimeScale extends TotalAnalysis {

    public double productiveScreenTime;
    public double productivePercent;
    private final int INVALID_PERCENT = -1;
    protected String screenTimeStatus;

    public double getProductivePercent() {
        return productivePercent;
    }

    @Override
    public void questions() {
        System.out.println("How much of the screen time was productive? ");
        productiveScreenTime = getValidInput(sc, 0, 24);

        System.out.println("How much of the screen time was Unproductive? ");
        score = getValidInput(sc, 0, 24);
        try {
            if (!(productiveScreenTime == 0 && score == 0))
                productivePercent = (productiveScreenTime / (productiveScreenTime + score)) * 100;
        } catch (ArithmeticException a) {
            productivePercent = INVALID_PERCENT;
        }
    }

    @Override
    public void indicator (double score) {
        if (score <= 2)
            status = "GOOD_ST";

        else if (score >= 5)
            status = "BAD_ST";

        else
            status = "NORMAL_ST";

        screenTimeStatus = status;
    }

    public String getScreenTimeStatus() {
        return screenTimeStatus;
    }

    public void advice (String status, double productivePercent) {
        System.out.println("Screen Time Statistics:");
        if (productivePercent == INVALID_PERCENT)
            System.out.println("The Productivity and screen time calculation can't be measured");
        else {
            System.out.printf("The percentage of productive screen time throughout your day are: %.2f%%\n", productivePercent);
            if (status.equals("BAD_ST"))
                System.out.println("Be careful! Too much unproductive screen time can waste your day, Set timers for apps, " +
                        "Turn off notifications, Use a To-Do list to stay focused");
            else if (status.equals("NORMAL_ST"))
                System.out.println("Good, but try to reduce unproductive time a little to increase productivity");

            else System.out.println("Excellent! You have great self-control and healthy digital habits");
            System.out.println("*********************************");
        }
    }

}

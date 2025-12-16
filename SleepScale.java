public class SleepScale extends TotalAnalysis implements OverallAnalysis {

    @Override
    public void questions() {
        System.out.println("Enter the hours you slept:");
        score = getValidInput(sc, 0, 23);
    }

    @Override
    public void indicator (double score) {
        if (score < 6) {
             status = "LOW_SLEEP";
        }
        else if (score > 10) {
             status = "HIGH_SLEEP";
        }
        else {
             status = "PERFECT_SLEEP";
        }

    }

    @Override
    public void advice(String status) {
        System.out.println("Sleep Statistics:");
        if (status.equals("LOW_SLEEP"))
            System.out.println("Your sleep has been too low lately. Try reducing caffeine, especially in the afternoon and evening, to help you fall asleep more easily.");

        else if (status.equals("HIGH_SLEEP"))
            System.out.println("You've been sleeping a bit too much. Consider setting a consistent alarm clock to maintain a healthier sleep schedule.");

        else
            System.out.println("Perfect! Your sleep is right on track—keep up the great habits!");
        System.out.println("*********************************");
    }

}

public class MovementScale extends TotalAnalysis implements OverallAnalysis {

    @Override
    public void questions() {
        System.out.println("How much estimated steps did you move today: ");
        score = getValidInput(sc, 0, 100000);
    }

    @Override
    public void indicator (double score) {
        if (score < 2000) {
            status = "LOW_STEPS";
        }
        else if (score > 40000) {
            status = "HIGH_STEPS";
        }
        else {
            status = "PERFECT_STEPS";
        }
    }

    @Override
    public void advice (String status) {
        System.out.println("Movement Statistics:");
        if (status.equals("LOW_STEPS"))
            System.out.println("Your steps have been too low, you try to aim for 4000 to 5000 steps.");

        else if (status.equals("HIGH_STEPS"))
            System.out.println("Your steps have been too high, its good, but try to rest a little.");

        else
            System.out.println("Perfect steps, continue!");
        System.out.println("*********************************");
    }


  }

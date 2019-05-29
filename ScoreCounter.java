import greenfoot.World;

/**
 * This score counter class keeps the scores. It contains ScoreDisplayer
 * that displays the score on the screen.
 * 
 * @author Team APCSA 2019
 * @author Andy Ge
 * @author Yijie Gui
 * @version 2019-05-22
 */
@SuppressWarnings("WeakerAccess")
public class ScoreCounter
{
    /**
     * This array stores the count of all the hit scores:
     * scores[0]: Max
     * scores[1]: Great
     * scores[2]: Cool
     * scores[3]: Good
     * scores[4]: Bad
     * scores[5]: Poor
     * Timings for each score see JudgementController.calculateTimings().
     */
    private final int[] scores = new int[6];

    /**
     * This array stores scores in the hit order.
     * Eg. [0, 0, 1, 0, 0, 0, 2, 1, 0, 0, 5, 3, 0, ...]
     */
    private final int[] scoresHitOrder;

    /**
     * Half note ratio.
     * See ScoreCalculator.calculateHalfNoteRatio() for more details.
     */
    private final double halfNoteRatio;

    /** The index of the current note (Live updating) */
    private int noteIndex = 0;

    /** Total score (Live updating) */
    private double totalScore = 0;

    /** Bonus (Live updating) */
    private double bonus = 100;

    /** Accuracy displayer */
    private final ScoreDisplayerAccuracy accuracyDisplayer;

    /**
     * Create a new counter, initialised to 0.
     *
     * @param beatmap The beatmap
     */
    public ScoreCounter(Beatmap beatmap)
    {
        this.scoresHitOrder = new int[beatmap.countTotalObjects()];
        this.halfNoteRatio = ScoreCalculator.calculateHalfNoteRatio(beatmap.countTotalObjects());

        // Create displayers
        accuracyDisplayer = new ScoreDisplayerAccuracy(this);
    }

    /**
     * Initialize score displayers.
     */
    public void initDisplayers(World world)
    {
        world.addObject(accuracyDisplayer, 0, 0);
        accuracyDisplayer.init();
    }

    /**
     * Update image for all the displayers.
     */
    private void updateImage()
    {
        accuracyDisplayer.update();
    }

    /**
     * Add a new hit score, and update the image.
     *
     * @param hit Hit value from 0 to 5 (From Great to Poor)
     */
    public void hit(int hit)
    {
        // Store scores.
        scores[hit]++;
        scoresHitOrder[noteIndex] = hit;
        noteIndex ++;

        // Update bonus and total score.
        bonus = ScoreCalculator.calculateNewBonus(bonus, hit);
        totalScore += ScoreCalculator.calculateHitScore(halfNoteRatio, bonus, hit);

        updateImage();
    }

    // ###################
    // Getters and Setters
    // ###################

    public int[] getScores()
    {
        return scores;
    }

    public int[] getScoresHitOrder()
    {
        return scoresHitOrder;
    }

    public double getBonus()
    {
        return bonus;
    }

    public void setBonus(double bonus)
    {
        this.bonus = bonus;
    }

    public double getTotalScore()
    {
        return totalScore;
    }

    public double getHalfNoteRatio()
    {
        return halfNoteRatio;
    }

    public ScoreDisplayerAccuracy getAccuracyDisplayer()
    {
        return accuracyDisplayer;
    }
}

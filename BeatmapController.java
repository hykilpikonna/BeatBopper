import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.util.ArrayList;

/**
 * A BeatmapController is an actor that controls the beatmap, spawns the
 * notes in the correct timing, etc.
 *
 * @author Team APCSA 2019
 * @author Yijie Gui
 * @since 2019-05-25 06:37
 */
@SuppressWarnings("WeakerAccess")
public class BeatmapController extends Actor
{
    /** Timing controller. */
    private final TimingController timer;

    /** Beatmap. */
    private final Beatmap beatmap;

    /** Judgement calculator */
    private final JudgementCalculator judgementCalculator;

    /** Score counter. */
    private final ScoreCounter scoreCounter;

    /**
     * Create a beatmap controller.
     */
    public BeatmapController(Beatmap beatmap, ScoreCounter scoreCounter)
    {
        this.timer = new TimingController();
        this.beatmap = beatmap;
        this.judgementCalculator = new JudgementCalculator(beatmap);
        this.scoreCounter = scoreCounter;

        setImage((GreenfootImage) null);
    }

    /**
     * Start the timer. ( = Start the game)
     */
    public void startTimer()
    {
        timer.start();
    }

    /**
     * Act: Detect the notes that are about to fall down, and spawn them.
     */
    public void act()
    {
        // Get time
        int gameTime = timer.getTotalDuration();

        // Spawn notes.
        for (int i = 0; i < Constants.KEYS.length; i++)
        {
            // TODO: Optimize this (used new ArrayList because java.util.ConcurrentModificationException)
            for (NoteInformation noteInfo : new ArrayList<NoteInformation>(beatmap.getFuture(i)))
            {
                // Within the speed range.
                if (noteInfo.getTime() - gameTime > Constants.GAME_SPEED_MS)
                {
                    break;
                }

                // Spawn the note to the top.
                spawnNote(noteInfo);
            }
        }

        // Register non-hit notes as missed.
        for (ArrayList<Note> col : beatmap.getPresent())
        {
            // TODO: Optimize this (used new ArrayList because java.util.ConcurrentModificationException)
            for (Note note : new ArrayList<Note>(col))
            {
                if (judgementCalculator.isMissed(note.getHitTime(), gameTime))
                {
                    // Register a missed note.
                    // TODO: Test this
                    registerHitAndRemoveNote(note, 5);
                }
            }
        }
    }

    /**
     * Spawn a note.
     *
     * @param noteInfo Information of the note.
     */
    private void spawnNote(NoteInformation noteInfo)
    {
        // Spawn the note.
        Note note = new Note(noteInfo);
        getWorld().addObject(note, 0, 0);
        note.init();

        // Put in present and remove from future.
        beatmap.getPresent(noteInfo.getColumn()).add(note);
        beatmap.getFuture(noteInfo.getColumn()).remove(noteInfo);
    }

    /**
     * Remove a present note.
     *
     * @param note Present note actor.
     * @param hitScore Hit score (From 0 to 5)
     */
    private void registerHitAndRemoveNote(Note note, int hitScore)
    {
        // Hit
        scoreCounter.hit(hitScore);

        // Remove note
        note.getWorld().removeObject(note);

        // Move from present to past
        beatmap.getPresent(note.getColumn()).remove(note);
        beatmap.getPast(note.getColumn()).add(new NoteInformation(note));
    }

    /**
     * Register a hit.
     *
     * @param col Column number
     */
    public void hit(int col)
    {
        // Get time
        int gameTime = timer.getTotalDuration();

        // Check if there are any present notes
        if (beatmap.getPresent(col).size() == 0) return;

        // Get note
        Note note = beatmap.getPresent(col).get(0);

        // Calculate hit score
        int hit = judgementCalculator.calculateHitValue(note.getHitTime(), gameTime);
        
        // Player didn't hit anything in range.
        if (hit == -1) return;

        // Register hit
        registerHitAndRemoveNote(note, hit);
    }

    // ###################
    // Getters and Setters
    // ###################

    public TimingController getTimer()
    {
        return timer;
    }

    public Beatmap getBeatmap()
    {
        return beatmap;
    }

    public JudgementCalculator getJudgementCalculator()
    {
        return judgementCalculator;
    }

    public ScoreCounter getScoreCounter()
    {
        return scoreCounter;
    }
}

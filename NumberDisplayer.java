import greenfoot.Actor;
import greenfoot.GreenfootImage;

import java.util.LinkedList;

/**
 * This class contains the utility methods to render scores. The image in
 * this class aligns from the right (Drawing items from the right to the
 * left.
 *
 * @author Team APCSA 2019
 * @author Yijie Gui
 * @author Andy Ge
 * @since 2019-05-29 17:06
 */
@SuppressWarnings("WeakerAccess")
public abstract class NumberDisplayer extends Actor
{
    /** Image of the Numbers (0-9) */
    private final GreenfootImage[] numberImages;

    /** Image of the Dot (Eg. the "." in "12.4") */
    private final GreenfootImage dotImage;

    /** Max height in the numbers */
    private final int maxHeight;

    /** Current rendered width from the right to the left. */
    private int currentRenderedWidth;

    /**
     * Construct a score displayer.
     *
     * @param numberImages Numbers
     * @param dotImage Image of the dot.
     */
    public NumberDisplayer(GreenfootImage[] numberImages, GreenfootImage dotImage)
    {
        this.numberImages = numberImages;
        this.dotImage = dotImage;
        currentRenderedWidth = 0;

        // Calculate max height
        int maxHeight = 0;
        for (GreenfootImage image : numberImages)
        {
            maxHeight = Math.max(image.getHeight(), maxHeight);
        }
        this.maxHeight = maxHeight;
    }

    /**
     * Initialize location and image.
     *
     * @param x X location
     * @param y Y location
     */
    protected void init(int x, int y)
    {
        // Create a base image
        GreenfootImage image = new GreenfootImage(Constants.WIDTH, maxHeight);
        setImage(image);

        // Set location
        setLocation(x, y);
    }

    /**
     * Draw a number to the right.
     *
     * @param number Number
     * @param digits How many digits to keep?
     */
    public void drawNumber(double number, int digits)
    {
        // Get the decimal half to digits
        double decimal = (number % 1) * Math.pow(10, digits);

        // Draw the decimal half
        drawNumber((int) decimal);

        // Draw dot
        drawLetter(dotImage);

        // Draw the integer half
        drawNumber((int) number);
    }

    /**
     * Draw a integer to the right.
     *
     * @param number Integer
     */
    public void drawNumber(int number)
    {
        // Get the digits in a stack
        LinkedList<Integer> stack = new LinkedList<>();
        if (number == 0) stack.push(0);
        else while (number > 0)
        {
            stack.push(number % 10);
            number /= 10;
        }

        // Draw them in reverse order
        while (!stack.isEmpty())
        {
            drawLetter(numberImages[stack.pollLast()]);
        }
    }

    /**
     * Draw a letter to the right-most space.
     * Precondition: letter.getHeight() <= maxHeight
     *
     * @param letter Letter image
     */
    public void drawLetter(GreenfootImage letter)
    {
        // Calculate X and Y so that it is right aliened and centered vertically
        int x = Constants.WIDTH - currentRenderedWidth - letter.getWidth();
        int y = (int) ((maxHeight - letter.getHeight()) / 2.0);

        // Draw it!
        getImage().drawImage(letter, x, y);

        // Add its width to the current rendered width
        currentRenderedWidth += letter.getWidth();
    }

    /**
     * Clear the image.
     */
    public void clear()
    {
        getImage().clear();
        currentRenderedWidth = 0;
    }

    /**
     * Obtain an NumberDisplayer object for testing
     *
     * @return NumberDisplayer object.
     */
    public static NumberDisplayer getTestInstance()
    {
        return new NumberDisplayer(Images.SCORE_NUMBERS, Images.SCORE_DOT) {};
    }

    // ###################
    // Getters and Setters
    // ###################

    public GreenfootImage[] getNumberImages()
    {
        return numberImages;
    }

    public int getMaxHeight()
    {
        return maxHeight;
    }
}
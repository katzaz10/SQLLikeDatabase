package edu.yu.data.finalProject;

import edu.yu.data.finalProject.DataPoint;
import edu.yu.data.finalProject.Row;

/**
 * Pair of original row, and what row will look like after update
 * @author Avraham Katz
 */

public class RowUpdatePair
{
    private Row original;
    private Row update;

    protected RowUpdatePair(Row original, Row update)
    {
        this.original = original;
        this.update = update;
    }

    protected Row getOriginal()
    {
        return original;
    }

    /**
     * Swaps values in original row and updated row
     * @return New values in original
     */
    protected Row swapValues()
    {
        for (int i = 0; i < this.original.getSize(); i++)
        {
            DataPoint temp = this.update.getIndex(i);
            this.update.setIndex(this.original.getIndex(i), i);
            this.original.setIndex(temp, i);
        }

        return this.original;
    }
}

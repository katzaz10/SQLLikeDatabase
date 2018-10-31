package edu.yu.data.finalProject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Holds a collection of data in a single line of a Table
 * @author Avraham Katz
 */

public class Row
{
    private ArrayList<DataPoint> values = new ArrayList<DataPoint>();

    protected Row (int rowLength)
    {
        for (int i = 0; i < rowLength; i++)
        {
            this.values.add(null);
        }
    }

    protected DataPoint getIndex(int index)
    {
        return this.values.get(index);
    }


    protected void setIndex(DataPoint data, int index)
    {
        this.values.set(index, data);
    }


    /**
     * Adds a value to the end of a row
     * @param data Value adding
     */
    protected void appendValue(DataPoint data)
    {
        this.values.add(data);
    }


    /**
     * Finds the size of a row
     * @return int Row size
     */
    protected int getSize()
    {
        return this.values.size();
    }


    /**
     * Checks to see if a index of a row is null or not
     * @param index Index of row checking
     * @return boolean Whether it is null or not
     */
    private boolean isNull(int index)
    {
        return this.getIndex(index) == null;
    }


    /**
     * If an index is null, sets the index to the default value
     * @param defaultValue Value to set null indexes to
     * @param index Index of row checking
     */
    protected void setDefaults(DataPoint defaultValue, int index)
    {
        if (this.isNull(index))
        {
            this.setIndex(defaultValue, index);
        }
    }

    /**
     * Finds the whole number length of a decimal number
     * @param index Index of row checking
     * @return Length of whole number portion of decimal
     */
    protected int decimalWholeNumberLength(int index)
    {
        return this.getIndex(index).decimalLengths()[0];
    }


    /**
     * Finds the fractional length of a decimal number
     * @param index Index of row checking
     * @return Length of fractional portion of decimal value
     */
    protected int decimalFractionalLength(int index)
    {
        return this.getIndex(index).decimalLengths()[1];
    }


    protected Row clone()
    {
        Row newRow = new Row(this.getSize());

        for (int i = 0; i < this.getSize(); i++)
        {
            if (this.getIndex(i) != null)
            {
                DataPoint nextValue = this.getIndex(i).clone();
                newRow.setIndex(nextValue, i);
            }
        }

        return newRow;
    }

    @Override
    public int hashCode()
    {
        String values = "";

        for (int i = 0; i < this.getSize(); i++)
        {
            if (this.getIndex(i) == null)
            {
                values = values + 31;
            }

            else
            {
                values = values + this.getIndex(i).toString() + this.getIndex(i).getType().toString();
            }
        }

        return Objects.hash(values);
    }


    /**
     * Determines if the values in 2 Rows are equal
     * @param otherRow Other row checking
     * @return Whether equal or not
     */
    private boolean valuesEqual(Row otherRow)
    {
        if (this.getSize() != otherRow.getSize())
        {
            return false;
        }

        for (int i = 0 ; i < this.getSize(); i++)
        {
            if (!this.getIndex(i).equals(otherRow.getIndex(i)))
            {
                return false;
            }
        }

        return true;
    }


    @Override
    public boolean equals(Object that)
    {
        if (this == that)
        {
            return true;
        }

        if (that == null)
        {
            return false;
        }

        if (this.getClass() != that.getClass())
        {
            return false;
        }

        Row otherRow = (Row) that;

        return this.valuesEqual(otherRow);
    }


    @Override
    public String toString()
    {
        String string = "";

        for (int i = 0; i < this.getSize(); i++)
        {
            string = string + this.getIndex(i);

            if (i != this.getSize() - 1)
            {
                string = string + " ";
            }
        }

        return string;
    }
}
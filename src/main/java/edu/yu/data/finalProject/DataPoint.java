package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

/**
 * DataPoint generic class, holds a value of type BOOLEAN, VARCHAR (String), DECIMAL (Double), or INT (integer)
 * @author Avraham Katz
 */

public class DataPoint<T>
{
    private T object;
    private final ColumnDescription.DataType objectType;

    protected DataPoint(Object object, ColumnDescription.DataType objectType)    //throw runtime exception with message if typing is wrong?????? OR
    {
        this.objectType = objectType;
        String string = object.toString();

        try
        {
            switch(objectType)
            {
                case BOOLEAN:   if (!string.toLowerCase().equals("true") && !string.toLowerCase().equals("false"))
                                {
                                    throw new IllegalArgumentException("data input " + '"' + string + '"' + " does not match data type " + objectType);
                                }

                                this.object = (T) Boolean.valueOf(string);
                                    break;
                case VARCHAR:   this.object = (T) string;
                                    break;
                case DECIMAL:   this.object = (T) Double.valueOf(string);
                                    break;
                case INT:       this.object = (T) Integer.valueOf(string);
                                    break;
            }
        }

        catch (NumberFormatException all)
        {
            throw new IllegalArgumentException("data input " + '"' + string + '"' + " does not match data type " + objectType);
        }
    }


    protected T getObject()
    {
        return this.object;
    }


    protected ColumnDescription.DataType getType()
    {
        return this.objectType;
    }


    /**
     *  Finds the whole number length and decimal length of a decimal DataPoint
     *  @return int[] Slot 0 holds the whole value length. Slot 1 holds the fractional length
     */
    protected int[] decimalLengths()                                                                      //DO WE COUNT NEGATIVE SIGNS?????????????
    {
        Double doubleChecking = (Double) this.getObject();
        String[] splitter = doubleChecking.toString().split("\\.");
        int[] values = new int[2];
        values[0] = splitter[0].length();
        values[1] = splitter[1].length();

        return values;
    }


    /**
     *  Finds the length of a varchar
     * @return Length of varchar
     */
    protected int varcharLength()
    {
        return this.toString().length();
    }


    /**
     * Compares two decimals
     * @param otherDataPoint Other decimal comparing to
     * @return Return -1 if less than decimal comparing to, 1 if greater than decimal comparing to, and 0 if they are equal
     */
    private int decimalCompare(DataPoint otherDataPoint)
    {
        if (otherDataPoint.getType() != ColumnDescription.DataType.DECIMAL)
        {
            throw new IllegalArgumentException("DataPoint's " + this + " & " + otherDataPoint + " not of same type");
        }

        else if ((Double) this.object < (Double) otherDataPoint.object)
        {
            return -1;
        }

        else if ((Double) this.object > (Double) otherDataPoint.object)
        {
            return 1;
        }

        else    //where they are equal
        {
            return 0;
        }
    }


    /**
     * Compares two ints
     * @param otherDataPoint Other int comparing to
     * @return Return -1 if less than int comparing to, 1 if greater than int comparing to, and 0 if they are equal
     */
    private int intCompare(DataPoint otherDataPoint)
    {
        if (otherDataPoint.getType() != ColumnDescription.DataType.INT)
        {
            throw new IllegalArgumentException("DataPoint's " + this + " & " + otherDataPoint + " not of same type");
        }

        else if ((Integer) this.object < (Integer) otherDataPoint.object)
        {
            return -1;
        }

        else if ((Integer) this.object > (Integer) otherDataPoint.object)
        {
            return 1;
        }

        else    //where they are equal
        {
            return 0;
        }
    }

    /**
     * Compares two varchars
     * @param otherDataPoint Other varchar comparing to
     * @return Return -1 if less than varchar comparing to, 1 if greater than varchar comparing to, and 0 if they are equal
     */
    private int varcharCompare(DataPoint otherDataPoint)
    {
        if (this.getType() != ColumnDescription.DataType.VARCHAR || otherDataPoint.getType() != ColumnDescription.DataType.VARCHAR)
        {
            throw new IllegalArgumentException("DataPoint's " + this + " & " + otherDataPoint + " not of same type");
        }


        String thisValue = (String) this.object;
        String otherValue = (String) otherDataPoint.object;

        int comparison = thisValue.compareToIgnoreCase(otherValue);

        if (comparison > 0)
        {
            return 1;
        }

        else if (comparison < 0)
        {
            return -1;
        }

        return 0;
    }

    /**
     * Compares two booleans
     * @param otherDataPoint Other boolean comparing to
     * @return Return -1 if less than boolean comparing to, 1 if greater than boolean comparing to, and 0 if they are equal
     */
    private int booleanCompare(DataPoint otherDataPoint)
    {
        if (this.getType() != ColumnDescription.DataType.BOOLEAN || otherDataPoint.getType() != ColumnDescription.DataType.BOOLEAN)
        {
            throw new IllegalArgumentException("DataPoint's " + this + " & " + otherDataPoint + " not of same type");
        }

        Boolean thisValue = (Boolean) this.object;
        Boolean otherValue = (Boolean) otherDataPoint.object;

        int comparison = thisValue.compareTo(otherValue);

        if (comparison > 0)
        {
            return 1;
        }

        else if (comparison < 0)
        {
            return -1;
        }

        return 0;
    }


    /**
     * Compares two DataPoints
     * @param otherDataPoint Other datapoint comparing to
     * @return Return -1 if less than datapoint comparing to, 1 if greater than datapoint comparing to, and 0 if they are equal
     */
    protected int compare(DataPoint otherDataPoint)
    {
        ColumnDescription.DataType type = this.getType();

        switch(type)
        {
            case INT:               return this.intCompare(otherDataPoint);
            case DECIMAL:           return this.decimalCompare(otherDataPoint);
            case VARCHAR:           return this.varcharCompare(otherDataPoint);
            case BOOLEAN:           return this.booleanCompare(otherDataPoint);
            default:                return 1234;      //will never get here

        }
    }

    /**
     * Clones the dataPoint
     * @return cloned datapoint
     */
    protected DataPoint clone()
    {
        return new DataPoint(this, this.objectType);
    }


    @Override
    public String toString()
    {
        return this.getObject().toString();
    }


    @Override
    public boolean equals(Object that)
    {
        if (this == that)
        {
            return true;
        }

        if (this != null && that == null)
        {
            return false;
        }

        if (this.getClass() != that.getClass())
        {
            return false;
        }

        DataPoint otherDataPoint = (DataPoint) that;

        if (this.objectType != ((DataPoint) that).objectType)
        {
            return false;
        }

        return this.object.toString().toLowerCase().equals(otherDataPoint.object.toString().toLowerCase());
    }


    @Override
    public int hashCode()
    {
        return objectType.hashCode() * 7 + this.object.hashCode();
    }

}
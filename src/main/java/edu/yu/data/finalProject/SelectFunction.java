package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.data.finalProject.ColumnInfo;
import edu.yu.data.finalProject.DataPoint;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Takes an ArrayList of DataPoitns and runs a function from Select on those values
 * @author Avraham Katz
 */

public class SelectFunction
{
    private final ArrayList<DataPoint> values;
    private final ColumnDescription column;
    private final SelectQuery.FunctionName function;
    private final boolean isDistinct;

    protected SelectFunction(ArrayList<DataPoint> values, ColumnDescription column, SelectQuery.FunctionInstance functionInstance)
    {
        this.values = values;
        this.column = column;
        this.function = functionInstance.function;
        this.isDistinct = functionInstance.isDistinct;
    }

    /**
     * Deletes an index from the values
     * @param index Index deleting
     */
    protected void removeIndex(int index)
    {
        this.values.remove(index);
    }


    /**
     * Gets the name of a column for a Table based on column and function
     * @return ColumnName
     */
    private String getColumnFunctionName()
    {
        String distinct = "";

        if (this.isDistinct)
        {
            distinct = "DISTINCT ";
        }

        switch(this.function)
        {
            case AVG:        return "AVG(" + distinct + this.column.getColumnName() + ")";
            case MAX:        return "MAX(" + distinct + this.column.getColumnName() + ")";
            case MIN:        return "MIN(" + distinct + this.column.getColumnName() + ")";
            case SUM:        return "SUM(" + distinct + this.column.getColumnName() + ")";
            case COUNT:      return "COUNT(" + distinct + this.column.getColumnName() + ")";
            default:         return null; //will never reach here
        }
    }


    /**
     * Finds the ColumnInfo for the function
     * @return ColumnInfo
     */
    protected ColumnInfo getFunctionColumnInfo()
    {
        String columnName = this.getColumnFunctionName();

        switch(this.function)
        {
            case AVG:        return new ColumnInfo(columnName, ColumnDescription.DataType.DECIMAL);
            case MAX:        return new ColumnInfo(columnName, column.getColumnType());
            case MIN:        return new ColumnInfo(columnName, column.getColumnType());
            case SUM:        return new ColumnInfo(columnName, column.getColumnType());
            case COUNT:      return new ColumnInfo(columnName, ColumnDescription.DataType.INT);
            default:         return null;
        }
    }

    /**
     * Finds the average of the values
     * @return Average
     */
    private DataPoint averageFunction()
    {
        Double average = 0.0;

        for (int i = 0; i < this.values.size(); i++)
        {
            DataPoint currentPoint = this.values.get(i);

            if (currentPoint != null)
            {
                if (this.column.getColumnType() == ColumnDescription.DataType.DECIMAL)
                {
                    average = average + (Double) currentPoint.getObject();
                }

                else
                {
                    Integer value = (Integer) currentPoint.getObject();
                    average = average + value.doubleValue();
                }
            }
        }

        average = average / this.values.size();

        return new DataPoint(average, ColumnDescription.DataType.DECIMAL);
    }


    /**
     * Counts the number of values
     * @return Count
     */
    private DataPoint countFunction()  //count nulls?
    {
        Integer count = 0;

        for (int i = 0; i < this.values.size(); i++)
        {
            if (this.values.get(i) != null)
            {
                count++;
            }
        }

        return new DataPoint(count, ColumnDescription.DataType.INT);
    }


    /**
     * Finds the max int or decimal in the values
     * @return Max value
     */
    private DataPoint maxFunction()
    {
        DataPoint max = null;

        for (int i = 0; i < this.values.size(); i++)
        {
            DataPoint currentPoint = this.values.get(i);

            if (currentPoint != null && (max == null || currentPoint.compare(max) == 1))
            {
                max = currentPoint;
            }
        }

        if (max == null)
        {
            return null;
        }

        return new DataPoint(max, column.getColumnType());
    }


    /**
     * Finds the min int or decimal in the values
     * @return Min value
     */
    private DataPoint minFunction()
    {
        DataPoint min = null;

        for (int i = 0; i < this.values.size(); i++)
        {
            DataPoint currentPoint = this.values.get(i);

            if (currentPoint != null && (min == null || currentPoint.compare(min) == -1))
            {
                min = currentPoint;
            }
        }

        if (min == null)
        {
            return null;
        }

        return new DataPoint(min, column.getColumnType());
    }


    /**
     * Sums up all the ints in the values
     * @return Sum
     */
    private DataPoint sumInt()
    {
        Integer sum = 0;

        for (int i = 0; i < this.values.size(); i++)
        {
            DataPoint currentPoint = this.values.get(i);

            if (currentPoint != null)
            {
                sum = sum + (Integer) currentPoint.getObject();
            }
        }

        return new DataPoint(sum, ColumnDescription.DataType.INT);
    }


    /**
     * Sums up all the decimals in the values
     * @return Sum
     */
    private DataPoint sumDecimal()
    {
        Double sum = 0.0;

        for (int i = 0; i < this.values.size(); i++)
        {
            DataPoint currentPoint = this.values.get(i);

            if (currentPoint != null)
            {
                sum = sum + (Double) currentPoint.getObject();
            }
        }

        return new DataPoint(sum, ColumnDescription.DataType.DECIMAL);
    }


    /**
     * Sums up all the ints or decimals in the values
     * @return sum
     */
    private DataPoint sumFunction()
    {
        if (column.getColumnType() == ColumnDescription.DataType.DECIMAL)
        {
            return sumDecimal();
        }

        else
        {
            return sumInt();
        }
    }


    /**
     * Ensures that the function can be performed on the given column and values
     */
    private void functionCheck()
    {
        if (this.function == SelectQuery.FunctionName.AVG || this.function == SelectQuery.FunctionName.SUM)
        {
            if (this.column.getColumnType() == ColumnDescription.DataType.VARCHAR || this.column.getColumnType() == ColumnDescription.DataType.BOOLEAN)
            {
                throw new IllegalArgumentException("cannot perform " + this.function + " function on " + this.column.getColumnName() + " column");
            }
        }

        if (this.function == SelectQuery.FunctionName.MIN || this.function == SelectQuery.FunctionName.MAX)
        {
            if (this.column.getColumnType() == ColumnDescription.DataType.BOOLEAN)
            {
                throw new IllegalArgumentException("cannot perform " + this.function + " function on " + this.column.getColumnName() + " column");
            }

        }
    }


    /**
     * Removes all non-Distinct values from the values
     */
    private void distinctValues()
    {
        HashMap<DataPoint, Boolean> distinct = new HashMap<DataPoint, Boolean>();

        int indexChecking = 0;

        while (indexChecking < this.values.size())
        {
            DataPoint pointChecking = this.values.get(indexChecking);

            if (distinct.put(pointChecking, true) != null)
            {
                this.values.remove(indexChecking);
            }

            else
            {
                indexChecking++;
            }
        }
    }


    /**
     * Performs one of various Select functions
     * @return value of function
     */
    protected DataPoint performFunction()
    {
        this.functionCheck();

        if (this.isDistinct)
        {
            this.distinctValues();
        }

        switch(this.function)
        {
            case AVG:       return averageFunction();
            case MAX:       return maxFunction();
            case MIN:       return minFunction();
            case SUM:       return sumFunction();
            case COUNT:     return countFunction();
            default:        throw new IllegalArgumentException();
        }
    }
}
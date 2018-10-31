package edu.yu.data.finalProject;

import java.util.ArrayList;


/**
 * Static methods
 * @author Avraham Katz
 */

public class StaticMethods
{
    /**
     * Compares 2 objects, dealing with null inputs specifically
     * @param item1 First object comparing
     * @param item2 Second object comparing
     * @return int representing relationship between objects
     */
    private static int nullComparison(Object item1, Object item2)
    {
        if (item1 != null && item2 == null)
        {
            return 1;
        }

        else if (item1 == null && item2 != null)
        {
            return -1;
        }

        else if (item1 == null && item2 == null)
        {
            return 0;
        }

        else //both not null
        {
            return 999;
        }
    }

    /**
     * Compares 2 DataPoints, first dealing with cases where on one or both DataPoints are null, and the dealing with case where both are not null
     * @param item1 First DataPoint comparing
     * @param item2 Second DataPoint comparing
     * @return int representing relationship between objects
     */
    protected static int compare(DataPoint item1, DataPoint item2)
    {
        if (StaticMethods.nullComparison(item1, item2) != 999)
        {
            return StaticMethods.nullComparison(item1, item2);
        }

        return item1.compare(item2);
    }

    /**
     * Converts a list of ColumnInfo into an array
     * @param columnInfos ColumnInfos as list
     * @return ColumnInfos as array
     */
    protected static ColumnInfo[] columnInfoListToArray(ArrayList<ColumnInfo> columnInfos)
    {
        ColumnInfo[] columns = new ColumnInfo[columnInfos.size()];

        for (int i = 0; i < columns.length; i++)
        {
            columns[i] = columnInfos.get(i);
        }

        return columns;
    }

}

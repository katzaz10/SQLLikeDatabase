package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;

/**
 * Takes a condition and pulls out the index of the column on the table its referencing and the dataPoint it uses as a comparison
 * @author Avraham Katz
 */

public class DataPointFromCondition
{
    private Table table;
    private Condition condition;

    protected DataPointFromCondition(Table table, Condition condition)
    {
        this.table = table;
        this.condition = condition;
    }


    /**
     * Finds the DataPoint used in a Condition
     * @return DataPoint from condition
     */
    protected DataPoint getDataPoint()
    {
        String dataValue = condition.getRightOperand().toString();

        if (dataValue.toLowerCase().equals("null"))
        {
            return null;
        }

        int index = this.table.getColumnIndex(this.condition.getLeftOperand().toString());
        ColumnDescription.DataType dataType = this.table.getColumnType(index);

        if (dataType == ColumnDescription.DataType.VARCHAR && dataValue.trim().equals(""))
        {
            return null;
        }

        return new DataPoint(dataValue, dataType);
    }
}

package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;

import java.util.ArrayList;


/**
 * Takes a Table and Condition, and filters rows that meet the condition
 * @author Avraham Katz
 */

public class IterationFilter
{
    private final Table table;
    private Condition rootCondition;

    protected IterationFilter(Table table, Condition condition)
    {
        this.table = table;
        this.rootCondition = condition;
    }

    protected IterationFilter(Table table)
    {
        this.table = table;
    }

    /**
     * Determines if a int or decimal in a condition is greater than, less than, or equal to the value in a row specified by the condition
     * @return 0 if values are equal, -1 if condition value greater than row value, 1 if condition value less than row value, or 999 if row value is null
     */
    private int comparativeFromCondition(Row row, Condition condition)
    {
        Integer index = this.table.getColumnIndex(condition.getLeftOperand().toString());

        DataPointFromCondition breakDown = new DataPointFromCondition(this.table, condition);
        DataPoint conditionData = breakDown.getDataPoint();
        DataPoint rowData = row.getIndex(index);

        return StaticMethods.compare(rowData, conditionData);
    }


    /**
     * Tests to see whether a row upholds a Condition. A condition can hold conditions
     * @return Whether row upholds condition or not
     */
    protected boolean rowMeetCondition(Row row, Condition condition)
    {
        Condition.Operator operator = condition.getOperator();

        switch(operator)
        {
            case AND:   return this.rowMeetCondition(row, (Condition) condition.getLeftOperand()) && this.rowMeetCondition(row, (Condition) condition.getRightOperand());
            case OR:    return this.rowMeetCondition(row, (Condition) condition.getLeftOperand()) || this.rowMeetCondition(row, (Condition) condition.getRightOperand());
            default:    break;
        }

        this.table.columnNameInTableCheck(condition.getLeftOperand().toString());
        int comparative = this.comparativeFromCondition(row, condition);

        switch(operator)
        {
            case EQUALS:                        return comparative == 0;
            case NOT_EQUALS:                    return comparative != 0;
            case LESS_THAN:                     return comparative == -1;
            case GREATER_THAN:                  return comparative == 1;
            case LESS_THAN_OR_EQUALS:           return comparative == 0 || comparative == -1;
            case GREATER_THAN_OR_EQUALS:        return comparative == 0 || comparative == 1;
            default:                            return false;
        }
    }


    /**
     * Gets all rows from a this.table that match the condition
     * @return All rows that match the condition
     */
    protected ArrayList<Row> filterRows()
    {
        ArrayList<Row> rowsThatPass = new ArrayList<>();

        for (int i = 0; i < this.table.getColumnHeight(); i++)
        {
            Row rowChecking = this.table.getRow(i);

            if (this.rowMeetCondition(rowChecking, this.rootCondition))
            {
                rowsThatPass.add(rowChecking);
            }
        }

        return rowsThatPass;
    }

}

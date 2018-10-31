package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Filters rows from a condition, if the condition always touches an index on the table
 * @author Avraham Katz
 */

public class IndexFilter
{
    private Table table;
    private Condition rootCondition;

    protected IndexFilter(Table table, Condition condition)
    {
        this.table = table;
        this.rootCondition = condition;
    }


    /**
     * Takes 2 lists of rows and finds rows that are in both lists
     * @param group1 First list
     * @param group2 Second list
     * @return Rows in both lists
     */
    private ArrayList<Row> andCondition(ArrayList<Row> group1, ArrayList<Row> group2)
    {
        HashMap<Row, Boolean> distinct = new HashMap<Row, Boolean>();
        ArrayList<Row> andRows = new ArrayList<>();

        for (int i = 0; i < group1.size(); i++)
        {
            distinct.put(group1.get(i), true);
        }

        for (int j = 0; j < group2.size(); j++)
        {
            if (distinct.put(group2.get(j), true) != null)
            {
                andRows.add(group2.get(j));
            }
        }

        return andRows;
    }

    /**
     * Takes 2 lists of rows and finds rows that are in at least one list
     * @param group1 First list
     * @param group2 Second list
     * @return Rows in lists
     */
    private ArrayList<Row> orCondition(ArrayList<Row> group1, ArrayList<Row> group2)
    {
        HashMap<Row, Boolean> distinct = new HashMap<Row, Boolean>();
        ArrayList<Row> orRows = new ArrayList<>();

        for (int i = 0; i < group1.size(); i++)
        {
            distinct.put(group1.get(i), true);
            orRows.add(group1.get(i));
        }

        for (int j = 0; j < group2.size(); j++)
        {
            if (distinct.put(group2.get(j), true) == null)
            {
                orRows.add(group2.get(j));
            }
        }

        return orRows;
    }


    /**
     * Finds all rows that meet a given condition, if rows indexed
     * @return Whether row upholds condition or not
     */
    private ArrayList<Row> rowsMeetCondition(Condition condition)
    {
        Condition.Operator operator = condition.getOperator();

        switch(operator)
        {
            case AND:       return this.andCondition(this.rowsMeetCondition((Condition) condition.getLeftOperand()), this.rowsMeetCondition((Condition) condition.getRightOperand()));
            case OR:        return this.orCondition(this.rowsMeetCondition((Condition) condition.getLeftOperand()), this.rowsMeetCondition((Condition) condition.getRightOperand()));
            default:        String index = condition.getLeftOperand().toString();
                            DataPointFromCondition breakDown = new DataPointFromCondition(this.table, condition);
                            return this.table.getTableIndex(index).get(breakDown.getDataPoint(), operator);
        }
    }


    /**
     * Filters rows from a Table
     * @return Filtered rows
     */
    protected ArrayList<Row> filterRows()
    {
        return this.rowsMeetCondition(this.rootCondition);
    }
}

package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.DeleteQuery;

import java.util.ArrayList;

/**
 * Deletes rows from a Table
 * @author Avraham Katz
 */

public class Delete
{
    private final Table table;
    private ArrayList<Row> rowsToDelete;
    private final DeleteQuery query;

    protected Delete(Table table, DeleteQuery query)
    {
        this.table = table;
        this.query = query;
    }


    /**
     * Finds all rows that need to be deleted based on an index filter
     */
    private void runIndexBasedFilter()
    {
        IndexFilter rowFilter = new IndexFilter(this.table, this.query.getWhereCondition());

        this.rowsToDelete = rowFilter.filterRows();
    }


    /**
     * Takes a row and iterates through table to find its index and delete it
     * @param row Row deleting
     */
    private void findIndexAndDelete(Row row)
    {
        for (int i = 0; i < this.table.getColumnHeight(); i++)
        {
            if (this.table.getRow(i) == row)
            {
                this.table.deleteRow(i);
                break;
            }
        }
    }

    /**
     *  Deletes all filtered rows
     */
    private void deleteFilteredRows()
    {
        for (int i = 0; i < this.rowsToDelete.size(); i++)
        {
            this.findIndexAndDelete(this.rowsToDelete.get(i));
        }
    }


    /**
     * Deletes all rows in a table that match a Condition
     */
    private void deleteByIterationMethod()
    {
        Condition condition = this.query.getWhereCondition();

        int rowUpTo = 0;

        while (rowUpTo < this.table.getColumnHeight())
        {
            IterationFilter meet = new IterationFilter(this.table);

            if (meet.rowMeetCondition(this.table.getRow(rowUpTo), condition))
            {
                this.table.deleteRow(rowUpTo);
            }

            else
            {
                rowUpTo++;
            }
        }
    }


    /**
     * Delete all rows in a table
     */
    private void deleteAllRows()
    {
        int initialColumnHeight = this.table.getColumnHeight();

        for (int i = 0; i < initialColumnHeight; i++)
        {
            this.table.deleteRow(0);
        }
    }


    /**
     * Deletes row by running through index filter and then deleting by brute force search and destroy
     */
    private void deleteByIndexMethod()
    {
        this.runIndexBasedFilter();
        this.deleteFilteredRows();
    }


    /**
     * Runs the delete
     */
    protected void delete()
    {
        if (this.query.getWhereCondition() == null)
        {
            this.deleteAllRows();
        }

        else if (this.table.conditionAlwaysTouchesIndex(this.query.getWhereCondition()))
        {
            this.deleteByIndexMethod();
        }

        else
        {
            this.deleteByIterationMethod();
        }
    }
}

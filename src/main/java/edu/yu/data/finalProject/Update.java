package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnValuePair;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;
import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.UpdateQuery;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Updates rows on a table, undoing the update if the update does not uphold all column statuses
 * @author Avraham Katz
 */

public class Update
{
    private final Table table;
    private ArrayList<Row> rowsToUpdate;
    private ArrayList<RowUpdatePair> rowUpdatePairs= new ArrayList<>();
    private final UpdateQuery query;

    protected Update(Table tableUpdating, UpdateQuery query)
    {
        this.table = tableUpdating;
        this.query = query;
    }

    /**
     * Finds the indexes on the table the update effects
     * @return Indexes affected
     */
    private ArrayList<Index> indexesUpdateEffects()
    {
        ColumnValuePair[] columnsEffecting = query.getColumnValuePairs();
        ArrayList<Index> indecesEffected = new ArrayList<>();

        for (int i = 0; i < columnsEffecting.length; i++)
        {
            if (this.table.getTableIndex(columnsEffecting[i].getColumnID().getColumnName()) != null)
            {
                indecesEffected.add(this.table.getTableIndex(columnsEffecting[i].getColumnID().getColumnName()));
            }
        }

        return indecesEffected;
    }


    /**
     * Finds rows and their new values, storing them together in a RowUpdatePair
     * @param row Row to be updated
     */
    private void createAndStoreRowUpdatePair(Row row)
    {
        ColumnValuePair[] columnValuePairs = this.query.getColumnValuePairs();
        Row rowChecking = row.clone();

        for (int j = 0; j < columnValuePairs.length; j++)
        {
            ColumnValuePair currentPair = columnValuePairs[j];
            Integer index = this.table.getColumnIndex(currentPair.getColumnID().getColumnName());
            ColumnDescription.DataType type = this.table.getColumnDescription(currentPair.getColumnID().getColumnName()).getColumnType();
            DataPoint updateValue = new DataPoint(currentPair.getValue(), type);
            rowChecking.setIndex(updateValue, index);
        }

        ColumnStatusMaintenance checker = new ColumnStatusMaintenance(this.table, rowChecking); //need to ensure dont double check row for unqiue column issues
        Row checkedRow = checker.maintainExcludingUniqueAndDefault();
        this.rowUpdatePairs.add(new RowUpdatePair(row, checkedRow));
    }


    /**
     * Iterate through filtered rows, storing their RowUpdatePair
     */
    private void createAndStoreAllRowUpdatePairs()
    {
        for (int i = 0; i < this.rowsToUpdate.size(); i++)
        {
            this.createAndStoreRowUpdatePair(this.rowsToUpdate.get(i));
        }
    }

    /**
     * Updates a row and the indexes it is on
     * @param rowUpdate Row updating
     */
    private void updateRow(RowUpdatePair rowUpdate)
    {
        ArrayList<Index> indexesEffected = this.indexesUpdateEffects();

        for (int i = 0; i < indexesEffected.size(); i++)
        {
            indexesEffected.get(i).deleteRow(rowUpdate.getOriginal());
        }

        Row updatedRow = rowUpdate.swapValues();

        for (int k = 0; k < indexesEffected.size(); k++)
        {
            indexesEffected.get(k).addRow(updatedRow);
        }
    }

    /**
     * Checks to ensure column unique status maintained on update
     * @param columnName Column checking
     * @return Whether uphold status or not
     */
    private boolean uniqueColumnCheck(String columnName)
    {
        int columnIndex = this.table.getColumnIndex(columnName);

        ArrayList<DataPoint> values = this.table.getColumnValues(columnIndex);

        HashMap<DataPoint, Boolean> unique = new HashMap<DataPoint, Boolean>();

        for (int i = 0; i < values.size(); i++)
        {
            if (unique.put(values.get(i), true) != null)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Undoes update if the unique status of a column not upheld
     */
    private void undoUpdate()
    {
        for (int i = 0; i < this.rowUpdatePairs.size(); i++)
        {
            this.updateRow(this.rowUpdatePairs.get(i));
        }
    }


    /**
     * Updates all rows that passed filter
     */
    private void updateAllRowsThatNeedUpdating()
    {
        for (int i = 0; i < this.rowUpdatePairs.size(); i++)
        {
            this.updateRow(this.rowUpdatePairs.get(i));
        }
    }

    /**
     * Runs index based filter on condition for update and table
     */
    private void runIndexBasedFilter()
    {
        IndexFilter rowFilter = new IndexFilter(this.table, this.query.getWhereCondition());

        this.rowsToUpdate =  rowFilter.filterRows();
    }

    /**
     * Runs an iteration filter on the table
     */
    private void runIterationFilter()
    {
        Condition condition = this.query.getWhereCondition();
        ArrayList<Row> rowsPassed = new ArrayList<>();

        IterationFilter filter = new IterationFilter(this.table, condition);

        this.rowsToUpdate = filter.filterRows();
    }

    /**
     * Updates rows, starting with index filter
     */
    private void updateByIndexMethod()
    {
        this.runIndexBasedFilter();
        this.createAndStoreAllRowUpdatePairs();
        this.updateAllRowsThatNeedUpdating();
    }

    /**
     * Updates rows, starting with iteration filter
     */
    private void updateByIterationMethod()
    {
        this.runIterationFilter();
        this.createAndStoreAllRowUpdatePairs();
        this.updateAllRowsThatNeedUpdating();
    }


    /**
     * Updates Table
     */
    protected void update()
    {
        if (this.table.conditionAlwaysTouchesIndex(this.query.getWhereCondition()))
        {
            this.updateByIndexMethod();
        }

        else
        {
            this.updateByIterationMethod();
        }

        ColumnValuePair[] updateColumns = this.query.getColumnValuePairs();

        for (int i = 0; i < updateColumns.length; i++)
        {
            if (this.table.getColumnDescription(updateColumns[i].getColumnID().getColumnName()).isUnique())
            {
                if (!this.uniqueColumnCheck(updateColumns[i].getColumnID().getColumnName()))
                {
                    this.undoUpdate();
                    throw new IllegalArgumentException("update does not uphold " + updateColumns[i].getColumnID().getColumnName() + "'s unique status");
                }
            }
        }
    }

}
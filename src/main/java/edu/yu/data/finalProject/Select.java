package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Selects all of the rows in a table that meet a series of conditions, and performs functions on that data
 * @author Avraham Katz
 */

public class Select
{
    private Table tableSelectingFrom;
    private final ArrayList<SelectFunction> functions = new ArrayList<>();
    private final SelectQuery query;


    protected Select(Table table, SelectQuery query)
    {
        this.tableSelectingFrom = table.clone();
        this.query = query;
    }


    /**
     * Creates and runs an index filter on the table
     * @return Rows that meet condition
     */
    private ArrayList<Row> runIndexBasedFilter()
    {
        IndexFilter rowFilter = new IndexFilter(this.tableSelectingFrom, this.query.getWhereCondition());

        return rowFilter.filterRows();
    }

    /**
     * Filters rows by iteration, and checking if they meet a condition
     * @return Rows that meet condition
     */
    private ArrayList<Row> runIterationFilter()
    {
        Condition condition = this.query.getWhereCondition();
        ArrayList<Row> rowsPassed = new ArrayList<>();

        IterationFilter filter = new IterationFilter(this.tableSelectingFrom, condition);

        return filter.filterRows();
    }


    /**
     * Filters out rows that do not meet where condition of SelectQuery
     */
    private void filterRows()
    {
        Condition condition = this.query.getWhereCondition();

        if (condition != null)
        {
            Table filteredRowsTable = this.tableSelectingFrom.emptyClone();
            filteredRowsTable.setTableName(this.tableSelectingFrom.getTableName() + " Select");
            ArrayList<Row> rowsPassed;

            if (this.tableSelectingFrom.conditionAlwaysTouchesIndex(condition))
            {
                rowsPassed = this.runIndexBasedFilter();
            }

            else
            {
                rowsPassed = this.runIterationFilter();
            }

            filteredRowsTable.setActualTable(rowsPassed);
            filteredRowsTable.setColumnHeight(rowsPassed.size());
            this.tableSelectingFrom = filteredRowsTable;
        }
    }


    /**
     * When creating a new table for filtering columns, this sets the columns that are required in then new table
     * @param columnsSelecting Columns required by query
     * @param tableFormatting  New table creating
     */
    private void setFilteredColumns(ColumnID[] columnsSelecting, Table tableFormatting)
    {
        for (int i = 0; i < columnsSelecting.length; i++)
        {
            this.tableSelectingFrom.columnNameInTableCheck(columnsSelecting[i].getColumnName());
        }

        for (int i = 0; i < columnsSelecting.length; i++)
        {
            ColumnDescription columnsDescription = this.tableSelectingFrom.getColumnDescription(columnsSelecting[i].getColumnName());

            tableFormatting.addColumn(columnsDescription, i);
        }
    }


    /**
     * Functions add the columnName they are acting on to the SelectedColumnNames. This removes those from that list, to know which columns Select is really looking for
     * @param columnsSelecting Columns before filtering
     * @return  Columns after filtering
     */
    private ColumnID[] removeFunctionsFromSelectedColumns(ColumnID[] columnsSelecting)
    {
        ColumnID[] filteredColumns = new ColumnID[columnsSelecting.length - this.query.getFunctions().size()];

        for (int i = 0; i < filteredColumns.length; i++) {
            filteredColumns[i] = columnsSelecting[i];
        }

        return filteredColumns;
    }


    /**
     * Creates a Table shell for column filtering
     * @param columnsSelecting Columns need for table
     * @return Table shell
     */
    private Table createTableShellForColumnFilter(ColumnID[] columnsSelecting)
    {
        Table filterTable = new Table(columnsSelecting.length);
        this.setFilteredColumns(columnsSelecting, filterTable);
        filterTable.setTableName(this.tableSelectingFrom.getTableName() + " Select");
        return filterTable;
    }


    /**
     * Takes a shell of a table and fills in the row, only filling in indexes in rows needed
     * @param filterTable Table filling
     * @param columnsSelecting Columns selecting
     */
    private void fillInRowsForTableForColumnFilter(Table filterTable, ColumnID[] columnsSelecting)
    {
        for (int i = 0; i < this.tableSelectingFrom.getColumnHeight(); i++)
        {
            filterTable.addRow(new Row(columnsSelecting.length));
        }

        for (int i = 0; i < columnsSelecting.length; i++)
        {
            int index = this.tableSelectingFrom.getColumnIndex(columnsSelecting[i].getColumnName());

            for (int j = 0; j < filterTable.getColumnHeight(); j++)
            {
                filterTable.setCell(j, i, this.tableSelectingFrom.getCell(j, index));
            }
        }
    }


    /**
     * Filters out columns not required by SelectQuery
     */
    private void filterColumns()
    {
        ColumnID[] columnsSelecting = this.removeFunctionsFromSelectedColumns(this.query.getSelectedColumnNames());

        if (columnsSelecting == null || columnsSelecting.length == 0)
        {
            this.tableSelectingFrom = new Table(0);
        }

        else if (!columnsSelecting[0].getColumnName().equals("*"))
        {
            for (int i = 0; i < columnsSelecting.length; i++)
            {
                this.tableSelectingFrom.columnNameInTableCheck(columnsSelecting[i].getColumnName());
            }

            Table filterTable = this.createTableShellForColumnFilter(columnsSelecting);
            this.fillInRowsForTableForColumnFilter(filterTable, columnsSelecting);

            this.tableSelectingFrom = filterTable;
        }
    }


    /**
     * If a row is non-Distinct, deletes that row within a SelectFunction to maintain balance between SelectFunction values and current Table
     * @param index Index deleting
     */
    private void deleteNonDistinctRowsFromFunctions(int index)
    {
        for (int i = 0; i < this.functions.size(); i++)
        {
            this.functions.get(i).removeIndex(index);
        }
    }


    /**
     * Removes any rows that are not distinct from the table
     */
    private void distinctRows()
    {
        HashMap<Row, Boolean> distinct = new HashMap<Row, Boolean>();

        int indexChecking = 0;

        while (indexChecking < this.tableSelectingFrom.getColumnHeight())
        {
            Row rowChecking = this.tableSelectingFrom.getRow(indexChecking);

            if (distinct.put(rowChecking, true) != null)
            {
                this.tableSelectingFrom.deleteRow(indexChecking);
                this.deleteNonDistinctRowsFromFunctions(indexChecking);
            }

            else
            {
                indexChecking++;
            }
        }
    }


    /**
     * Orders rows from Tables and sets Tables rows to new ordered Rows
     */
    private void orderRows()
    {
        TableRowsOrderer order = new TableRowsOrderer(this.tableSelectingFrom, this.query.getOrderBys());
        ArrayList<Row> orderedRows = order.orderRows();
        this.tableSelectingFrom.setActualTable(orderedRows);
    }


    /**
     * Updates Select functions based on SelectQuery functions
     */
    private void updateFunctions()
    {
        ArrayList<SelectQuery.FunctionInstance> functions = this.query.getFunctions();

        for (int i = 0; i < functions.size(); i++)
        {
            SelectQuery.FunctionInstance currentFunction = functions.get(i);

            String columnName = currentFunction.column.getColumnName();

            this.tableSelectingFrom.columnNameInTableCheck(columnName);

            int columnIndex = this.tableSelectingFrom.getColumnIndex(columnName);
            ArrayList<DataPoint> values = this.tableSelectingFrom.getColumnValues(columnIndex);

            this.functions.add(new SelectFunction(values, this.tableSelectingFrom.getColumnDescription(columnName), currentFunction));
        }
    }


    /**
     * Performs a SelectFunction, and augments value onto every row of Table
     * @param function SelectFunction running
     */
    private void augmentFunctionToTable(SelectFunction function)
    {
        DataPoint newValue = function.performFunction();

        if (this.tableSelectingFrom.getRowLength() == 0 && this.tableSelectingFrom.getColumnHeight() == 0)
        {
            Table newTable = new Table(1);
            Row newRow = new Row(1);
            newRow.setIndex(newValue, 0);
            newTable.addRow(newRow);

            this.tableSelectingFrom = newTable;
        }

        else
        {
            for (int i = 0; i < this.tableSelectingFrom.getColumnHeight(); i++)
            {
                this.tableSelectingFrom.appendValueToRow(newValue, i);
            }
        }
    }


    /**
     * Performs every SelectFunction, and augments values onto every row of Table
     */
    private void augmentFunctionsToTable()
    {
        for (int i = 0; i < this.functions.size(); i++)
        {
            this.augmentFunctionToTable(this.functions.get(i));
        }
    }


    /**
     * Gets ColumnInfo for every SelectFunction in function field
     * @return All ColumnInfos
     */
    private ColumnInfo[] getFunctionColumnInfos()
    {
        ColumnInfo[] columnInfos = new ColumnInfo[this.functions.size()];

        for (int i = 0; i < this.functions.size(); i++)
        {
            columnInfos[i] = this.functions.get(i).getFunctionColumnInfo();
        }

        return columnInfos;
    }


    /**
     * Gets all ColumnInfos for Select, including ColumnInfos from SelectFunctions
     * @return ColumnInfos
     */
    private ColumnInfo[] getColumnInfosWithFunctions()
    {
        ArrayList<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        ColumnInfo[] nonFunctionColumns;
        ColumnInfo[] functionColumns;

        if (this.tableSelectingFrom.getColumnNames() != null)
        {
            nonFunctionColumns = this.tableSelectingFrom.getColumnInfos();

            for (int i = 0; i < nonFunctionColumns.length; i++)
            {
                columnInfos.add(nonFunctionColumns[i]);
            }
        }

        if (this.functions.size() != 0)
        {
            functionColumns = this.getFunctionColumnInfos();

            for (int i = 0; i < functionColumns.length; i++)
            {
                columnInfos.add(functionColumns[i]);
            }
        }

        return StaticMethods.columnInfoListToArray(columnInfos);
    }


    /**
     *Gets all ColumnInfos for Select, excluding ColumnInfos from SelectFunctions
     * @return ColumnInfos
     */
    private ColumnInfo[] getColumnInfosWithoutFunctions()
    {
        return this.tableSelectingFrom.getColumnInfos();
    }


    /**
     * Gets all ColumnInfo of necessary columns
     * @return ColumnInfos
     */
    private ColumnInfo[] getSelectedColumnInfos()
    {
        if (this.functions.size() != 0)
        {
            return this.getColumnInfosWithFunctions();
        }

        else
        {
            return this.getColumnInfosWithoutFunctions();
        }
    }


    /**
     *  Run SelectQuery on Table
     * @return ResultSet
     */
    protected ResultSet runSelect()
    {
        this.filterRows();

        if (this.query.getOrderBys().length > 0)
        {
            this.orderRows();
        }

        if (this.query.getFunctions().size() != 0)
        {
            this.updateFunctions();
        }

        this.filterColumns();

        if (this.query.isDistinct())
        {
            this.distinctRows();
        }

        if (this.functions.size() > 0)
        {
            this.augmentFunctionsToTable();
        }

        return new ResultSet(this.getSelectedColumnInfos(), this.tableSelectingFrom.getActualTable());
    }
}
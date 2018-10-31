package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;

import java.util.*;

/**
 * Holds a series of rows that hold the same type of data. Has a series of columns that discern what the data mean in each index of each row
 * @author Avraham Katz
 */

public class Table {

    private String tableName;
    private int rowLength;
    private int columnHeight = 0;
    private String primaryKeyColumn;

    private ArrayList<Row> actualTable = new ArrayList<Row>();
    private HashMap<String, Integer> columnNameToIndex = new HashMap<String, Integer>();
    private HashMap<Integer, ColumnDescription> indexToColumnDescription = new HashMap<Integer, ColumnDescription>();
    private HashMap<String, ColumnDescription> columnNameToDescription = new HashMap<String, ColumnDescription>();
    private HashMap<String, Index> columnNameToTableIndex = new HashMap<>();


    protected Table (String tableName, int rowLength, String primaryKeyColumn)
    {
        this.tableName = tableName;
        this.rowLength = rowLength;
        this.primaryKeyColumn = primaryKeyColumn;
    }

    protected Table (int rowLength)
    {
        this.rowLength = rowLength;
    }


    protected String getTableName()
    {
        return this.tableName;
    }

    protected void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    protected int getRowLength()
    {
        return this.rowLength;
    }
    
    protected int getColumnHeight()
    {
        return this.columnHeight;
    }

    protected void setColumnHeight(int height)
    {
        this.columnHeight = height;
    }

    protected String getPrimaryKeyColumn()
    {
        return this.primaryKeyColumn;
    }


    protected ArrayList<Row> getActualTable()
    {
        return this.actualTable;
    }


    protected void setActualTable(ArrayList<Row> actualTable)
    {
        this.actualTable = actualTable;
    }


    /**
     * Finds the index of the table a column is in
     * @param columnName Name of column finding index of 
     * @return Integer Index of column
     */
    protected Integer getColumnIndex(String columnName)
    {
        return this.columnNameToIndex.get(columnName);
    }

    
    /**
     * Finds the ColumnDescription by columnName
     * @param columnName Name of column finding
     * @return ColumnDescription Description of column 
     */
    protected ColumnDescription getColumnDescription(String columnName)
    {
        return this.columnNameToDescription.get(columnName);
    }


    /**
     * Finds the Columndescription based on column index
     * @param index Index of column
     * @return ColumnDescription
     */
    protected ColumnDescription getColumnDescription(Integer index)
    {
        return this.indexToColumnDescription.get(index);
    }


    /**
     * Gets all column name in table
     * @return Array of column names
     */
    protected String[] getColumnNames()
    {
        if (this.columnNameToIndex.size() == 0)
        {
            return null;
        }

        else
        {
            String[] columnNames = new String[this.getRowLength()];

            Iterator it = this.columnNameToIndex.entrySet().iterator();

            while (it.hasNext())
            {
                Map.Entry pair = (Map.Entry)it.next();

                columnNames[(Integer) pair.getValue()] = (String) pair.getKey();
            }

            return columnNames;
        }
    }

    /**
     * Gets all ColumnInfos for ResultSet purposes
     * @return ColumnInfos
     */
    protected ColumnInfo[] getColumnInfos()
    {
        String[] columnNames = this.getColumnNames();
        ArrayList<ColumnInfo> returnInfo = new ArrayList<ColumnInfo>();

        for (int i = 0; i < columnNames.length; i++)
        {
            if (columnNames[i] != null)
            {
                String columnName = columnNames[i];
                ColumnInfo nextColumn = new ColumnInfo(columnName, this.getColumnType(columnName));
                returnInfo.add(nextColumn);
            }
        }

        return StaticMethods.columnInfoListToArray(returnInfo);
    }


    /**
     * Gets the row at the given index
     * @param rowNumber Index of row
     * @return Row getting
     */
    protected Row getRow(int rowNumber)
    {
        return this.actualTable.get(rowNumber);
    }


    /**
     * Gets all values in a given column of the table
     * @param index Index of column
     * @return Array of all values of column
     */
    protected ArrayList<DataPoint> getColumnValues(int index)
    {
        ArrayList<DataPoint> column = new ArrayList<DataPoint>();

        for (int i = 0; i < this.getColumnHeight(); i++)
        {
            column.add(this.getCell(i, index));
        }

        return column;
    }


    /**
     * Gets a row and appends a value to the end
     * @param value DataPoint appending
     * @param rowIndex Index of row appending to
     */
    protected void appendValueToRow(DataPoint value, int rowIndex)
    {
        this.getRow(rowIndex).appendValue(value);
    }


    /**
     * Checks that a column name is in a table
     * @param columnName Column name checking
     */
    protected void columnNameInTableCheck(String columnName)
    {
        if (!this.columnNameToDescription.containsKey(columnName))
        {
            throw new IllegalArgumentException("column " + columnName + " not in table " + this.tableName);
        }
    }


    /**
     * Finds DataType of a column based on column index
     * @param index Index of column
     * @return DataType of column
     */
    protected ColumnDescription.DataType getColumnType(Integer index)
    {
        return this.getColumnDescription(index).getColumnType();
    }


    /**
     * Finds DataType of a column based on column name
     * @param columnName Column name
     * @return DataType of column
     */
    private ColumnDescription.DataType getColumnType(String columnName)
    {
        return this.getColumnDescription(columnName).getColumnType();
    }


    /**
     * Gets the given cell in the table
     * @param row Row index
     * @param column Column index
     * @return Value of cell
     */
    protected DataPoint getCell(int row, int column)
    {
        return this.getRow(row).getIndex(column);
    }

    /**
     * Sets the given cell in the table to the given value
     * @param row Row index
     * @param column Column index
     * @param value Value setting cell to
     */
    protected void setCell(int row, int column, DataPoint value)
    {
        this.actualTable.get(row).setIndex(value, column);
    }


    /**
     * Gets a table index on the table
     * @param columnName Name of column indexed
     * @return Index
     */
    protected Index getTableIndex(String columnName)
    {
        return this.columnNameToTableIndex.get(columnName);
    }


    /**
     * Adds a row onto an index
     * @param columnIndexed Name of column indexed
     * @param row Row adding
     */
    private void addRowToTableIndex(String columnIndexed, Row row)
    {
        this.columnNameToTableIndex.get(columnIndexed).addRow(row);
    }


    /**
     * Adds a row to all table indexes
     * @param row Row adding
     */
    private void addRowToAllTablesIndexes(Row row)
    {
        Iterator it = this.columnNameToTableIndex.entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            this.addRowToTableIndex((String) pair.getKey(), row);
        }
    }


    /**
     * Creates an index on the table
     * @param columnName Column indexing
     * @param indexName Name of index
     */
    protected void createTableIndex(String columnName, String indexName)
    {
        Index newIndex = new Index(this.getColumnIndex(columnName), indexName);
        this.columnNameToTableIndex.put(columnName, newIndex);

        for (int i = 0; i < this.columnHeight; i++)
        {
            newIndex.addRow(this.getRow(i));
        }
    }


    /**
     * Determines if a condition touches an index
     * @param condition Condition checking
     * @return Whether always touches or not
     */
    private boolean conditionTouchesIndex(Condition condition)
    {
        ColumnID column = (ColumnID) condition.getLeftOperand();
        String key = column.getColumnName();

        return this.columnNameToTableIndex.containsKey(key);
    }

    /**
     * Determines if a condition always touches an index
     * @param condition Condition checking
     * @return Whether always touches or not
     */
    protected boolean conditionAlwaysTouchesIndex(Condition condition)
    {
        Condition.Operator operator = condition.getOperator();

        if (operator == Condition.Operator.AND || operator == Condition.Operator.OR)
        {
            return this.conditionAlwaysTouchesIndex((Condition) condition.getLeftOperand()) && this.conditionAlwaysTouchesIndex((Condition) condition.getRightOperand());
        }

        return this.conditionTouchesIndex(condition);
    }


    /**
     * Adds a column to the table. This incldues mapping the column name to index of the table, mapping column name to ColumnDescription, and mapping index of column to ColumnDescription
     * @param columnInfo ColumnDescription of column
     * @param index Index of column on table
     */
    protected void addColumn(ColumnDescription columnInfo, Integer index)
    {
        this.columnNameToIndex.put(columnInfo.getColumnName(), index);
        this.columnNameToDescription.put(columnInfo.getColumnName(), columnInfo);
        this.indexToColumnDescription.put(index, columnInfo);
    }


    /**
     * Makes sure that column names adding to Tables uphold all rules of columns
     * @param columnsAdding Columns adding
     */
    private void columnsAddingCheck(ColumnDescription[] columnsAdding)
    {
        HashMap<String, Boolean> distinct = new HashMap<String, Boolean>();

        for (int i = 0; i < columnsAdding.length; i++)
        {
            String columnName = columnsAdding[i].getColumnName();

            if (columnName == null)
            {
                throw new IllegalArgumentException("column name cannot be null");
            }

            columnName = columnName.trim();

            if (columnName.equals(""))
            {
                throw new IllegalArgumentException("column name cannot be empty");
            }

            if (distinct.put(columnName, true) != null)
            {
                throw new IllegalArgumentException("table " + this.tableName + " already has " + columnName + " column");
            }
        }
    }


    /**
     * Maps all columns for a table
     * @param columnsAdding Columns adding to table
     */
    protected void addAllColumns(ColumnDescription[] columnsAdding)
    {
        this.columnsAddingCheck(columnsAdding);

        for (int i = 0; i < columnsAdding.length; i++)
        {
           this.addColumn(columnsAdding[i], i);
        }
    }


    /**
     * Takes a columnDescription and refactors its notNull status and unique status to reflect the qualities of a primary key column
     * @param columnName Name of column
     */
    protected void refactorPrimaryKey(String columnName)
    {
        if (this.columnNameToDescription.get(columnName).getHasDefault())
        {
            throw new IllegalArgumentException("primary key column " + columnName + " cannot have default value");
        }

        this.columnNameToDescription.get(columnName).setNotNull(true);
        this.columnNameToDescription.get(columnName).setUnique(true);
    }


    /**
     * Creates a row of data for table
     * @param pairs Values inserting into which columns in row
     * @return New row
     */
    private Row newUncheckedRow(ColumnValuePair[] pairs)
    {
        Row newRow = new Row(this.rowLength);

        for (int i = 0; i < pairs.length; i++)
        {
            String value = pairs[i].getValue();

            if (this.getColumnType(pairs[i].getColumnID().getColumnName()) != ColumnDescription.DataType.VARCHAR || !value.substring(1, value.length() - 1).trim().equals(""))
            {
                ColumnValuePair currentPair = pairs[i];
                Integer index = this.getColumnIndex(currentPair.getColumnID().getColumnName());
                ColumnDescription.DataType type = this.getColumnType(currentPair.getColumnID().getColumnName());
                DataPoint newDataPoint = new DataPoint(currentPair.getValue(), type);
                newRow.setIndex(newDataPoint, index);
            }
        }

        return newRow;
    }


    /**
     * Adds a row to the table, updating columnHeight field by increasing it by 1
     * @param rowAdding Row adding to table
     */
    protected void addRow(Row rowAdding)
    {
        this.actualTable.add(rowAdding);
        this.addRowToAllTablesIndexes(rowAdding);
        this.columnHeight++;
    }


    /**
     * Inserts a row into the table, doing a variety of checks to ensure the row holds up all statuses of each column
     * @param pairs Values of new row
     */
    protected void insertRow(ColumnValuePair[] pairs)
    {
        if (pairs.length > this.rowLength)
        {
            throw new IllegalArgumentException("row too large for this table");
        }

        Row uncheckedRow = this.newUncheckedRow(pairs);
        ColumnStatusMaintenance checker = new ColumnStatusMaintenance(this, uncheckedRow);
        Row checkedRow = checker.maintain();
        this.addRow(checkedRow);
    }


    /**
     * Deletes a row from a table index
     * @param columnName Name of column index on
     * @param row Row deleting
     */
    private void deleteRowFromTableIndex(String columnName, Row row)
    {
        this.columnNameToTableIndex.get(columnName).deleteRow(row);
    }


    /**
     * Deletes a row from all table indexes
     * @param row Row deleting
     */
    private void deleteRowFromAllTablesIndexes(Row row)
    {
        Iterator it = this.columnNameToTableIndex.entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            this.deleteRowFromTableIndex((String) pair.getKey(), row);
        }
    }


    /**
     * Deletes a row from a table
     * @param index Index of row
     */
    protected void deleteRow(int index)
    {
        this.deleteRowFromAllTablesIndexes(this.getRow(index));
        this.actualTable.remove(index);
        this.columnHeight--;
    }


    /**
     * Returns the longest length of any cell or column name after converting them to string (made for print method in DataBase class)
     * @return Longest length of any value printing
     */
    private int longestLengthOfOutput()
    {
        int longestLength = 0;

        String[] columnNames = this.getColumnNames();

        for (int i = 0; i < columnNames.length; i++)
        {
            if ((columnNames[i] + this.getColumnType(columnNames[i])).length() + 2 > longestLength) //'+1' to take into consideration the ':' symbol to be used for outprint when printing column names
            {
                longestLength = (columnNames[i] + this.getColumnType(columnNames[i])).length() + 2;
            }
        }

        for (int i = 0; i < this.getColumnHeight(); i++)
        {
            for (int j = 0; j < this.getRowLength(); j++)
            {
                if (this.getCell(i,j) != null && this.getCell(i, j).toString().length() > longestLength)
                {
                    longestLength = this.getCell(i, j).toString().length();
                }
            }
        }

        return longestLength;
    }


    /**
     * Print rows from Table
     * @param formatting Formatting of rows
     */
    protected void printTableRows(String formatting)
    {
        for (int i = 0; i < this.getActualTable().size(); i++)
        {
            for (int j = 0; j < this.getRowLength(); j++)
            {
                if (this.getCell(i, j) == null)
                {
                    System.out.printf(formatting, null);
                }

                else
                {
                    System.out.printf(formatting, this.getCell(i , j).toString());
                }
            }

            System.out.println();
        }
    }


    protected void print()
    {
        String[] columnNames = this.getColumnNames();
        String formatting = ("%" + this.longestLengthOfOutput() + "s ");

        System.out.println("[Table: " + tableName + "]");

        for (int i = 0; i < columnNames.length; i++)
        {
            System.out.printf(formatting, columnNames[i] + " " + this.getColumnType(columnNames[i]) + ":");
        }

        System.out.println();

        this.printTableRows(formatting);

        System.out.println();
    }


    protected Table clone()
    {
        Table tableClone = new Table(this.tableName, this.rowLength, this.primaryKeyColumn);

        tableClone.columnNameToIndex = this.columnNameToIndex;
        tableClone.indexToColumnDescription = this.indexToColumnDescription;
        tableClone.columnNameToDescription = this.columnNameToDescription;

        for (int i = 0; i < this.columnHeight; i++)
        {
            Row cloneRow = this.getRow(i).clone();
            tableClone.addRow(cloneRow);
        }

        return tableClone;
    }


    protected Table emptyClone()
    {
        Table tableClone = new Table(this.tableName, this.rowLength, this.primaryKeyColumn);

        tableClone.columnNameToIndex = this.columnNameToIndex;
        tableClone.indexToColumnDescription = this.indexToColumnDescription;
        tableClone.columnNameToDescription = this.columnNameToDescription;

        return tableClone;
    }


    private String addColumnsToString(String tableString)
    {
        tableString = tableString + "COLUMNS: ";

        for (int i = 0; i < this.rowLength; i++)
        {
            tableString = tableString + this.getColumnDescription(i).getColumnName();

            if (i != this.rowLength - 1)
            {
                tableString = tableString + ", ";
            }

            else
            {
                tableString = tableString + " ";
            }
        }

        return tableString;
    }


    private String addRowsToString(String tableString)
    {
        tableString = tableString + "| ROWS: ";

        for (int j = 0; j < this.columnHeight; j++)
        {
            tableString = tableString + "<" + this.getRow(j) +">";

            if (j != this.columnHeight - 1)
            {
                tableString = tableString + " ";
            }
        }

        return tableString;
    }

    @Override
    public String toString()
    {
        String tableString = "";

        tableString = this.addColumnsToString(tableString);

        return this.addRowsToString(tableString);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(this.tableName);
    }
}
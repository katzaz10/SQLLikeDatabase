package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

import java.util.ArrayList;

/**
 * Takes a row, and updates values on default columns and checks that row uphold column status
 * @author Avraham Katz
 */

public class ColumnStatusMaintenance
{
    private Table table;
    private Row rowChecking;


    protected ColumnStatusMaintenance(Table table, Row row)
    {
        this.table = table;
        this.rowChecking = row;
    }


    /**
     * Ensures a row is not violating the notNull status of a column
     */
    private void maintainNotNullColumn(int columnIndex)
    {
        ColumnDescription column = this.table.getColumnDescription(columnIndex);

        if (this.rowChecking.getIndex(columnIndex) == null)
        {
            if (column.isNotNull())
            {
                throw new IllegalArgumentException("row <" + this.rowChecking + "> does not uphold "
                        + this.table.getColumnDescription(columnIndex).getColumnName() + "'s not-null status");
            }
        }
    }


    /**
     * Ensures that a new row does not violate the unique status of a column
     */
    private void maintainUniqueColumn(int columnIndex)
    {
        ColumnDescription column = this.table.getColumnDescription(columnIndex);

        if (column.isUnique())
        {
            ArrayList<DataPoint> columnValues = this.table.getColumnValues(columnIndex);

            for (int i = 0; i < columnValues.size(); i++)
            {
                if (StaticMethods.compare(this.rowChecking.getIndex(columnIndex), columnValues.get(i)) == 0)
                {
                    throw new IllegalArgumentException("row <" + this.rowChecking + "> does not uphold "
                                                        + this.table.getColumnDescription(columnIndex).getColumnName() + "'s unique status");
                }
            }
        }
    }


    /**
     * When creating a new row, updates the default value of a column
     */
    private void maintainColumnDefaultValue(int columnIndex)
    {
        ColumnDescription column = this.table.getColumnDescription(columnIndex);

        if (column.getHasDefault())
        {
            DataPoint defaultValue = new DataPoint(column.getDefaultValue(), this.table.getColumnType(columnIndex));
            this.rowChecking.setDefaults(defaultValue, columnIndex);
        }
    }


    private void maintainColumnDecimalWholeNumberLength(int columnIndex)
    {
        ColumnDescription column = this.table.getColumnDescription(columnIndex);

        if (this.rowChecking.decimalWholeNumberLength(columnIndex) > column.getWholeNumberLength())
        {
            throw new IllegalArgumentException("row <" + this.rowChecking + "> does not uphold "
                                            + this.table.getColumnDescription(columnIndex).getColumnName() + "'s decimal whole length status");
        }
    }


    private void maintainColumnDecimalFractionalLength(int columnIndex)
    {
        ColumnDescription column = this.table.getColumnDescription(columnIndex);

        if (this.rowChecking.decimalFractionalLength(columnIndex) > column.getFractionLength())
        {
            throw new IllegalArgumentException("row <" + this.rowChecking + "> does not uphold "
                                            + this.table.getColumnDescription(columnIndex).getColumnName() + "'s fractional length status");
        }
    }



    /**
     * When creating a new row, ensures that the values do not violate the decimal length status of a column
     */
    private void maintainColumnDecimalLength(int columnIndex)
    {
        if (this.table.getColumnType(columnIndex) == ColumnDescription.DataType.DECIMAL && this.rowChecking.getIndex(columnIndex) != null)
        {
            this.maintainColumnDecimalWholeNumberLength(columnIndex);
            this.maintainColumnDecimalFractionalLength(columnIndex);
        }
    }


    /**
     * When creating a new row, ensures that the values do not violate the varchar length status of a column
     */
    private void maintainColumnVarcharLength(int columnIndex)
    {
        ColumnDescription column = this.table.getColumnDescription(columnIndex);

        if (this.table.getColumnType(columnIndex) == ColumnDescription.DataType.VARCHAR && this.rowChecking.getIndex(columnIndex) != null)
        {
            if (this.rowChecking.getIndex(columnIndex).varcharLength() > column.getVarCharLength())
            {
                throw new IllegalArgumentException("row <" + this.rowChecking + "> does not uphold "
                                            + this.table.getColumnDescription(columnIndex).getColumnName() + "'s varchar length status");
            }
        }
    }


    /**
     * Does a complete check on a row
     * @return Row after maintenance
     */
    protected Row maintain()
    {
        for (int i = 0; i < this.table.getRowLength(); i++)
        {
            this.maintainNotNullColumn(i);
            this.maintainUniqueColumn(i);
            this.maintainColumnDefaultValue(i);
            this.maintainColumnDecimalLength(i);
            this.maintainColumnVarcharLength(i);
        }

        return this.rowChecking;
    }


    /**
     * Does a complete check on a row, not checking unique or default
     * @return Row after maintenance
     */
    protected Row maintainExcludingUniqueAndDefault()
    {
        for (int i = 0; i < this.table.getRowLength(); i++)
        {
            this.maintainNotNullColumn(i);
            this.maintainColumnDecimalLength(i);
            this.maintainColumnVarcharLength(i);
        }

        return this.rowChecking;
    }
}

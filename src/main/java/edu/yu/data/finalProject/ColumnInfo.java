package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

/**
 * Represents a column's name and dataType for ResultSet purposes
 * @author Avraham Katz
 */

public class ColumnInfo
{
    private final String columnName;
    private final ColumnDescription.DataType dataType;

    protected ColumnInfo(String columnName, ColumnDescription.DataType dataType)
    {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    protected String getColumnName()
    {
        return this.columnName;
    }


    @Override
    public boolean equals(Object that)
    {
        if (this == that)
        {
            return true;
        }

        if (that == null)
        {
            return false;
        }

        if (this.getClass() != that.getClass())
        {
            return false;
        }

        ColumnInfo otherColumnInfo = (ColumnInfo) that;

        if (!this.columnName.equals(otherColumnInfo.columnName))
        {
            return false;
        }

        return this.dataType.equals(otherColumnInfo.dataType);
    }


    @Override
    public String toString()
    {
        return this.columnName + " " + this.dataType;
    }
}

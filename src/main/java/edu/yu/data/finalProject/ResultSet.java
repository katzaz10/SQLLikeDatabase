package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.ColumnDescription;

import java.util.ArrayList;

/**
 * Result of an SQL query
 * @author Avraham Katz
 */

public class ResultSet
{
    private ColumnInfo[] columns;
    private ArrayList<Row> result = new ArrayList<Row>();
    private String message;


    protected ResultSet(ColumnInfo[] columns, ArrayList<Row> result)
    {
        this.columns = columns;
        this.result = result;
    }


    protected ResultSet(ColumnInfo[] columns)
    {
        this.columns = columns;
    }


    protected ResultSet (DataPoint dataPoint)  //for select function purposes where just giving the resultSet a value
    {
        Row oneByOne = new Row(1);
        oneByOne.setIndex(dataPoint, 0);
        this.result.add(oneByOne);
    }

    protected ResultSet (boolean successOrFailure)
    {
        Row oneByOne = new Row(1);
        DataPoint value = new DataPoint(successOrFailure, ColumnDescription.DataType.BOOLEAN);
        oneByOne.setIndex(value, 0);
        this.result.add(oneByOne);
    }

    protected ResultSet (boolean successOrFailure, String message)
    {
        Row oneByOne = new Row(1);
        DataPoint value = new DataPoint(successOrFailure, ColumnDescription.DataType.BOOLEAN);
        oneByOne.setIndex(value, 0);
        this.result.add(oneByOne);
        this.message = message;
    }


    private ColumnInfo[] getColumns()
    {
        return columns;
    }

    private void setMessage(String message)
    {
        this.message = message;
    }

    protected void print()
    {
        if (message != null)
        {
            System.out.printf("    Message:%n      %s%n%n", this.message);
        }

        if (columns != null)
        {
            System.out.printf("    Columns: %n      ");

            for (int i = 0; i < this.columns.length; i++)
            {
                System.out.print(columns[i].getColumnName() + " ");
            }

            System.out.printf("%n%n");
        }

        if (result != null)
        {
            System.out.println("    Result:");

            for (int i = 0; i < result.size(); i++)
            {
                System.out.println("      " + result.get(i));
            }
        }

        System.out.println();
    }


    /**
     * Checks that columns of 2 ResultSets are equal
     * @param otherResultSet Second ResultSet
     * @return Whether equal or not
     */
    private boolean columnsEqual(ResultSet otherResultSet)
    {
        if (this.getColumns().length != otherResultSet.getColumns().length)
        {
            return false;
        }

        for (int i = 0; i < this.getColumns().length; i++)
        {
            for (int k = 0; k < otherResultSet.getColumns().length; k++)
            {
                if (this.columns[i].equals(otherResultSet.columns[k]))
                {
                    break;
                }

                else if (k == otherResultSet.getColumns().length - 1 && !this.columns[i].equals(otherResultSet.columns[k]))
                {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Checks that result of 2 ResultSets are equal
     * @param otherResultSet Second ResultSet
     * @return Whether equal or not
     */
    private boolean resultEqual(ResultSet otherResultSet)
    {
        if (this.result.size() != otherResultSet.result.size())
        {
            return false;
        }

        for (int j = 0; j < this.result.size(); j++)
        {
            if (!this.result.get(j).equals(otherResultSet.result.get(j)))
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Base checklist of things to ensure 2 ResultSets are equal
     * @param that Other object
     * @return Whether equal or not
     */
    private boolean baseCheckEqual(Object that)
    {
        if (that == null)
        {
            return false;
        }

        if (this.getClass() != that.getClass())
        {
            return false;
        }

        ResultSet otherResultSet = (ResultSet) that;

        if ((this.columns == null && otherResultSet.columns != null) || (this.columns != null && otherResultSet.columns == null))
        {
            return false;
        }

        if ((this.result == null && otherResultSet.result != null) || (this.result != null && otherResultSet.result == null))
        {
            return false;
        }

        return true;
    }


    @Override
    public boolean equals(Object that)
    {
        if (this == that)
        {
            return true;
        }

        if (!this.baseCheckEqual(that))
        {
            return false;
        }

        ResultSet otherResultSet = (ResultSet) that;

        if (this.columns != null)
        {
           if (!this.columnsEqual(otherResultSet))
           {
               return false;
           }
        }

        if (this.result != null)
        {
            return this.resultEqual(otherResultSet);
        }

        return true;
    }


    /**
     * Adds columns to String
     * @param resultString Current resultString
     * @return Result String with columns
     */
    private String addColumnsToString(String resultString)
    {
        for (int i = 0; i < this.columns.length; i++)
        {
            resultString = resultString + this.columns[i];

            if (i != this.columns.length - 1)
            {
                resultString = resultString + ", ";
            }

            else
            {
                resultString = resultString + " ";
            }
        }

        return resultString + "| ";
    }


    /**
     * Adds result to String
     * @param resultString Current resultString
     * @return Result String with result
     */
    private String addResultToString(String resultString)
    {
        for (int j = 0; j < result.size(); j++)
        {
            resultString = resultString + " <" + result.get(j) + ">";
        }

        return resultString + " |";
    }


    @Override
    public String toString()
    {
        String resultString = "COLUMNS: ";

        if (this.columns != null)
        {
            resultString = this.addColumnsToString(resultString);
        }

        else
        {
            resultString = resultString + "NULL | ";
        }

        resultString = resultString + "RESULT:";

        if (this.result != null)
        {
            resultString = this.addResultToString(resultString);
        }

        else
        {
            resultString = resultString + "NULL";
        }

        return resultString;
    }
}
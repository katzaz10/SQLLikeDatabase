package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.Condition;

import java.util.ArrayList;

public class Index
{
    private String indexName;
    private BTree index = new BTree();
    private int columnIndex;

    protected Index(int columnIndex, String indexName)
    {
        this.indexName = indexName;
        this.columnIndex = columnIndex;
    }


    /**
     * Adds a row to the index
     * @param row Row adding
     */
    protected void addRow(Row row)
    {
        this.index.put(row.getIndex(columnIndex), row);
    }


    /**
     * Deletes a row from the index
     * @param row Row deleting
     */
    protected void deleteRow(Row row)
    {
        this.index.getRoot().delete(row.getIndex(columnIndex), row);
    }


    /**
     * Gets all rows that match a certain key and operator pair
     * @param key Key comparing to
     * @param operator Type of comparison
     * @return All rows that match the key and operator pair
     */
    protected ArrayList<Row> get(DataPoint key, Condition.Operator operator)
    {
        switch(operator)
        {
            case EQUALS:                        return this.index.getRoot().getEqualTo(key);
            case NOT_EQUALS:                    return this.index.getRoot().getNotEqualTo(key);
            case LESS_THAN:                     return this.index.getRoot().getLessThan(key);
            case GREATER_THAN:                  return this.index.getRoot().getGreaterThan(key);
            case LESS_THAN_OR_EQUALS:           return this.index.getRoot().getLessThanOrEqualTo(key);
            case GREATER_THAN_OR_EQUALS:        return this.index.getRoot().getGreaterThanOrEqualTo(key);
            default:                            return null;
        }
    }

    public String toString()
    {
        return this.indexName + " -> " + this.index.toString();
    }
}

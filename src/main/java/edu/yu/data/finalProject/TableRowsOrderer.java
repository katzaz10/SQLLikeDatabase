package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery;
import edu.yu.data.finalProject.DataPoint;
import edu.yu.data.finalProject.Row;
import edu.yu.data.finalProject.StaticMethods;
import edu.yu.data.finalProject.Table;

import java.util.ArrayList;

import static edu.yu.data.finalProject.TableRowsOrderer.MergeOrder.ASCENDING;
import static edu.yu.data.finalProject.TableRowsOrderer.MergeOrder.DESCENDING;

/**
 * Takes the rows from a Table and orders them ascendingly or descendingly based on columns
 * @author Avraham Katz
 */

public class TableRowsOrderer
{
    private final Table tableOrdering;
    private final SelectQuery.OrderBy[] orders;


    protected TableRowsOrderer(Table tableOrdering, SelectQuery.OrderBy[] orders)
    {
        this.tableOrdering = tableOrdering;
        this.orders = orders;
    }


    /**
     * Moves the first value in a list to another list
     * @param listMovingTo List moving value to
     * @param listMovingFrom List moving value from
     */
    private void moveFirstValueFromListAToListB(ArrayList<Row> listMovingTo, ArrayList<Row> listMovingFrom)           
    {
        listMovingTo.add(listMovingFrom.get(0));
        listMovingFrom.remove(0);
    }

    enum MergeOrder
    {
        ASCENDING, DESCENDING
    }


    /**
     * Merge the values of two arraylists in ascending or descending order
     * @param firstGroup First group of values to merge
     * @param secondGroup Second group of values to merge
     * @param columnIndex Index of Row, where DataPoints that order is dependant on are held
     * @param mergeOrder Whether do an ascending or descending merge
     * @return Merged lists
     */
    private ArrayList<Row> merge(ArrayList<Row> firstGroup, ArrayList<Row> secondGroup, int columnIndex, MergeOrder mergeOrder)
    {
        ArrayList<Row> mergedRows = new ArrayList<Row>();

        while (firstGroup.size() != 0 || secondGroup.size() != 0)
        {
            if (firstGroup.size() == 0) this.moveFirstValueFromListAToListB(mergedRows, secondGroup);

            else if (secondGroup.size() == 0) this.moveFirstValueFromListAToListB(mergedRows, firstGroup);

            else if (mergeOrder== ASCENDING && StaticMethods.compare(firstGroup.get(0).getIndex(columnIndex), secondGroup.get(0).getIndex(columnIndex)) != -1)
            {
                this.moveFirstValueFromListAToListB(mergedRows, secondGroup);
            }

            else if (mergeOrder== DESCENDING && StaticMethods.compare(firstGroup.get(0).getIndex(columnIndex), secondGroup.get(0).getIndex(columnIndex)) != 1)
            {
                this.moveFirstValueFromListAToListB(mergedRows, secondGroup);
            }

            else
            {
                this.moveFirstValueFromListAToListB(mergedRows, firstGroup);
            }
        }

        return mergedRows;
    }


    /**
     * Takes an ArrayList of Rows, and splits the ArrayList in half into 2 ArrayList's of Rows
     * @param rowsSplitting ArrayList of rows splitting in half
     * @return 2 half rows
     */
    private ArrayList<ArrayList<Row>> splitRowList(ArrayList<Row> rowsSplitting)
    {
        ArrayList<Row> firstHalf = new ArrayList<Row>();
        ArrayList<Row> secondHalf = new ArrayList<Row>();
        ArrayList<ArrayList<Row>> rowHalves = new ArrayList<ArrayList<Row>>();

        int initialRowSplittingLength = rowsSplitting.size();

        for (int i = 0; i < initialRowSplittingLength / 2; i++)
        {
            firstHalf.add(rowsSplitting.get(i));
        }

        for (int j = initialRowSplittingLength / 2; j < rowsSplitting.size(); j++)
        {
            secondHalf.add(rowsSplitting.get(j));
        }

        rowHalves.add(0, firstHalf);
        rowHalves.add(1, secondHalf);

        return rowHalves;
    }


    /**
     * Performs an ascending merge sort
     * @param listSorting Rows sorting
     * @param columnIndex Index of rows where values ordering based on are held
     * @return Sorted rows
     */
    private ArrayList<Row> ascendingMergeSort(ArrayList<Row> listSorting, int columnIndex)
    {
        ArrayList<ArrayList<Row>> listHalves = this.splitRowList(listSorting);
        ArrayList<Row> firstHalf = listHalves.get(0);
        ArrayList<Row> secondHalf = listHalves.get(1);

        if (firstHalf.size() == 1 && secondHalf.size() == 1)
        {
            return merge(firstHalf, secondHalf, columnIndex, ASCENDING);
        }

        else if (firstHalf.size() == 1 && secondHalf.size() > 1)
        {
            return merge(firstHalf, ascendingMergeSort(secondHalf, columnIndex), columnIndex, ASCENDING);
        }

        else if (firstHalf.size() > 1 && secondHalf.size() == 1)
        {
            return merge(secondHalf, ascendingMergeSort(firstHalf, columnIndex), columnIndex, ASCENDING);
        }

        else //firstHalf.size() > 1 && secondHalf.size() > 1 (only other option after other if statements)
        {
            return merge(ascendingMergeSort(firstHalf, columnIndex), ascendingMergeSort(secondHalf, columnIndex), columnIndex, ASCENDING);
        }
    }


    /**
     * Performs a descending merge sort
     * @param listSorting Rows sorting
     * @param columnIndex Index of rows where values ordering based on are held
     * @return Sorted rows
     */
    private ArrayList<Row> descendingMergeSort(ArrayList<Row> listSorting, int columnIndex)
    {
        ArrayList<ArrayList<Row>> listHalves = this.splitRowList(listSorting);
        ArrayList<Row> firstHalf = listHalves.get(0), secondHalf = listHalves.get(1);


        if (firstHalf.size() == 1 && secondHalf.size() == 1)
        {
            return merge(firstHalf, secondHalf, columnIndex, DESCENDING);
        }

        else if (firstHalf.size() == 1 && secondHalf.size() > 1)
        {
            return merge(firstHalf, descendingMergeSort(secondHalf, columnIndex), columnIndex, DESCENDING);
        }

        else if (secondHalf.size() == 1 && firstHalf.size() > 1)
        {
            return merge(secondHalf, descendingMergeSort(firstHalf, columnIndex), columnIndex, DESCENDING);
        }

        else if (firstHalf.size() > 1 && secondHalf.size() > 1)
        {
            return merge(descendingMergeSort(firstHalf, columnIndex), descendingMergeSort(secondHalf, columnIndex), columnIndex, DESCENDING);
        }

        return null;
    }


    /**
     * Finds all consecutive rows in a list with same value in column x
     * @param comparisonValue Value grouping based on
     * @param rows Group of rows grouping from
     * @param index Index on row value grouping by found
     * @return All rows that match DataPoint
     */
    private ArrayList<Row> groupAllRowsWithSameValueAtIndexY(DataPoint comparisonValue, ArrayList<Row> rows, int index)
    {
        ArrayList<Row> rowsOfValueX = new ArrayList<Row>();
        this.moveFirstValueFromListAToListB(rowsOfValueX, rows);

        while (rows.size() > 0)
        {
            if (StaticMethods.compare(comparisonValue, rows.get(0).getIndex(index)) == 0)
            {
                this.moveFirstValueFromListAToListB(rowsOfValueX, rows);
            }

            else
            {
               return rowsOfValueX;
            }
        }

        return rowsOfValueX;
    }


    /**
     * Splits the row into subArrayLists where DataPoints in the same column are equals
     * @param rows Row splitting
     * @param columnIndex Index of row splitting based on
     * @return SubArrayLists of rows
     */
    private ArrayList<ArrayList<Row>> splitRowsByColumn(ArrayList<Row> rows, int columnIndex)
    {
        ArrayList<ArrayList<Row>> splitRows = new ArrayList<ArrayList<Row>>();

        while (rows.size() > 0)
        {
            DataPoint comparisonValue = rows.get(0).getIndex(columnIndex);

            splitRows.add(this.groupAllRowsWithSameValueAtIndexY(comparisonValue, rows, columnIndex));
        }

        return splitRows;
    }


    /**
     * Splits every ArrayList of Row in a larger ArrayList into subArrayLists where DataPoints in the same column are equals
     * @param arrayRows List of List of Rows
     * @param columnIndex Index of row splitting based on
     * @return New split rows
     */
    private ArrayList<ArrayList<Row>> splitEverySubarrayOfRowsByColumn(ArrayList<ArrayList<Row>> arrayRows, int columnIndex)
    {
        ArrayList<ArrayList<Row>> splitRows = new ArrayList<ArrayList<Row>>();

        for (int i = 0; i < arrayRows.size(); i++)
        {
            ArrayList<ArrayList<Row>> localSplitRows = this.splitRowsByColumn(arrayRows.get(i), columnIndex);

            for (int j = 0; j < localSplitRows.size(); j++)
            {
                splitRows.add(localSplitRows.get(j));
            }
        }

        return splitRows;
    }


    /**
     * Takes an ArrayList of ArrayList of Rows and orders each subArrayList based on the OrderBy
     * @param arrayRows ArrayList of ArrayList of Rows
     * @param ordering OrderBy, can be ascending or descending order
     * @return Ordered rows
     */
    private ArrayList<ArrayList<Row>> orderEachArrayRows(ArrayList<ArrayList<Row>> arrayRows, SelectQuery.OrderBy ordering)
    {
        int columnIndex = this.tableOrdering.getColumnIndex(ordering.getColumnID().getColumnName());

        for (int i = 0; i < arrayRows.size(); i++)
        {
            ArrayList<Row> currentRow = arrayRows.get(i);

            if (currentRow.size() > 1)
            {
                if (ordering.isAscending())
                {
                    ArrayList<Row> orderedRow = this.ascendingMergeSort(currentRow, columnIndex);
                    arrayRows.set(i, orderedRow);
                }

                else
                {
                    ArrayList<Row> orderedRow = this.descendingMergeSort(currentRow, columnIndex);
                    arrayRows.set(i, orderedRow);
                }
            }
        }

        return arrayRows;
    }


    /**
     * Takes an ArrayList of ArrayList of Rows, and combines all ArrayList of Row into a single ArrayList of Rows
     * @param arrayRows ArrayList of ArrayList of Rows combining
     * @return A singular ArrayList of Rows
     */
    private ArrayList<Row> concantinateRows(ArrayList<ArrayList<Row>> arrayRows)
    {
        ArrayList<Row> finalList = new ArrayList<Row>();

        for (int i = 0; i < arrayRows.size(); i++)
        {
            ArrayList<Row> currentList = arrayRows.get(i);

            for (int j = 0; j < currentList.size(); j++)
            {
                finalList.add(currentList.get(j));
            }
        }

        return finalList;
    }


    /**
     * Checks to ensure that column's needed for OrderBys exist
     */
    private void orderByColumnsInTableCheck()
    {
        for (int i = 0; i < this.orders.length; i++)
        {
            String columnName = this.orders[i].getColumnID().getColumnName();

            this.tableOrdering.columnNameInTableCheck(columnName);
        }
    }


    /**
     * Takes the rows from a table and orders them based on OrderBys
     * @return Ordered Rows
     */
    protected ArrayList<Row> orderRows()
    {
        this.orderByColumnsInTableCheck();

        ArrayList<ArrayList<Row>> rowsToOrder = new ArrayList<ArrayList<Row>>();
        rowsToOrder.add(this.tableOrdering.getActualTable());

        for (int i = 0; i < this.orders.length; i++)
        {
            rowsToOrder = this.orderEachArrayRows(rowsToOrder, this.orders[i]);

            if (i == this.orders.length - 1)
            {
                return this.concantinateRows(rowsToOrder);
            }

            else
            {
                int columnIndex = this.tableOrdering.getColumnIndex(this.orders[i].getColumnID().getColumnName());
                rowsToOrder = splitEverySubarrayOfRowsByColumn(rowsToOrder, columnIndex);
            }
        }

        return null;
    }
}
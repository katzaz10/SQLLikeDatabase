package edu.yu.data.finalProject;

import java.util.ArrayList;

public class BTreeNodeSlot
{
    private DataPoint key;
    private BTreeNode subtree;
    private ArrayList<Row> value = new ArrayList<>();

    protected BTreeNodeSlot(DataPoint key, BTreeNode subtree)
    {
        this.key = key;
        this.subtree = subtree;
    }

    protected BTreeNodeSlot(DataPoint key)
    {
        this.key = key;
    }

    protected BTreeNodeSlot(DataPoint key, Row value)
    {
        this.key = key;
        this.value.add(value);
    }

    protected DataPoint getKey()
    {
        return this.key;
    }

    protected BTreeNode getSubTree()
    {
        return this.subtree;
    }

    protected ArrayList<Row> getValue()
    {
        return this.value;
    }


    /**
     * Adds a row to this.value
     * @param newValue Row adding
     */
    protected void addValue(Row newValue)
    {
        this.value.add(newValue);
    }


    /**
     * Compares the key of this slot to the key of another slot
     * @param otherSlot Other slot comparing
     * @return Comparison
     */
    protected int compare(BTreeNodeSlot otherSlot)
    {
        return StaticMethods.compare(this.key, otherSlot.key);
    }


    /**
     * Compares the key of this slot to another key
     * @param key Key comparing to
     * @return Comparison
     */
    protected int compare(DataPoint key)
    {
        return StaticMethods.compare(this.key, key);
    }


    /**
     * Determines if this slot is in a base node
     * @return Whether base slot or not
     */
    protected boolean isBaseSlot()
    {
        return this.subtree == null;
    }

    public String toString()
    {
        String str = "";

        if (this.key == null)
        {
            str = str + "[null:";
        }

        else
        {
            str = str + "[" + this.key.toString() + ":";
        }

        if (this.isBaseSlot())
        {
            for (int i = 0; i < value.size(); i++)
            {
                str = str + "<" + this.value.get(i) + ">";
            }

            return str + "]";
        }

        return str + this.subtree + "]";
    }
}

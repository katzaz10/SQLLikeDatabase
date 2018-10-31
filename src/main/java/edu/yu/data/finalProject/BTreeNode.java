package edu.yu.data.finalProject;

import java.util.ArrayList;

public class BTreeNode
{
    private final int MAX_SIZE = 4;
    private int size = 0;
    private ArrayList<BTreeNodeSlot> slots = new ArrayList<>();

    protected BTreeNode()
    {

    }

    protected BTreeNodeSlot getSlot(int index)
    {
        return this.slots.get(index);
    }


    protected void addSlot(int index, BTreeNodeSlot slot)
    {
        this.slots.add(index, slot);
        this.size++;
    }


    /**
     * Splits a node in 2 nodes of half size
     * @return Slot referencing the new node
     */
    private BTreeNodeSlot splitNode()
    {
        BTreeNode newNode = new BTreeNode();

        for (int i = 0; i < MAX_SIZE / 2; i++)
        {
            newNode.addSlot(i, this.slots.remove(MAX_SIZE / 2));
            this.size--;
        }

        return new BTreeNodeSlot(newNode.getSlot(0).getKey(), newNode);
    }


    /**
     * Checks the size of the node's values, and if too large splits in half
     * @return new NodeSlot referencing new node
     */
    private BTreeNodeSlot sizeCheckAndSplit()
    {
        if (this.size == MAX_SIZE)
        {
            return this.splitNode();
        }

        return null;
    }


    /**
     * Checks to see if a node is an internal or external node
     * @return If is a base node or not
     */
    private boolean isBaseNode()
    {
        return this.getSlot(0).isBaseSlot();
    }


    /**
     * Adds a new node slot to a base node
     * @param newSlot Slot adding
     * @return New Slot if base node too full
     */
    private BTreeNodeSlot addBaseNodeSlot(BTreeNodeSlot newSlot)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (this.getSlot(i).compare(newSlot) == 0)
            {
                this.getSlot(i).addValue(newSlot.getValue().get(0));
                return this.sizeCheckAndSplit();
            }

            else if (i == this.size - 1 && this.getSlot(i).compare(newSlot) == 1)
            {
                this.addSlot(i, newSlot);
                return this.sizeCheckAndSplit();
            }

            else if ((i == this.size - 1 && this.getSlot(i).compare(newSlot) == -1) || (this.getSlot(i).compare(newSlot) == -1 && this.getSlot(i + 1).compare(newSlot) == 1))
            {
                this.addSlot(i + 1, newSlot);
                return this.sizeCheckAndSplit();
            }
        }

        return null; //will never get here
    }


    /**
     * Adds a slot to an inner node
     * @param newSlot Slot adding
     * @return New Slot if node too full
     */
    private BTreeNodeSlot addKeySlot(BTreeNodeSlot newSlot)
    {
        if (newSlot == null)
        {
            return null;
        }

        for (int i = 0; i < this.size; i++)
        {
            if (i == this.size - 1 && this.getSlot(i).compare(newSlot) == 1)
            {
                this.addSlot(i, newSlot);
                return this.sizeCheckAndSplit();
            }

            else if ((i == this.size - 1 && this.getSlot(i).compare(newSlot) == -1) || (this.getSlot(i).compare(newSlot) == -1 && this.getSlot(i + 1).compare(newSlot) == 1))
            {
                this.addSlot(i + 1, newSlot);
                return this.sizeCheckAndSplit();
            }
        }

        return null;    //will never get here
    }


    /**
     * Puts a new key value pair in a node or its subtree
     * @param newSlot New slot adding
     * @return New Slot if node too full
     */
    protected BTreeNodeSlot put(BTreeNodeSlot newSlot)
    {
        if (!this.isBaseNode())
        {
            for (int i = 0; i < this.size; i++)
            {
                if (this.getSlot(i).compare(newSlot) == 0)
                {
                    return this.addKeySlot(this.getSlot(i).getSubTree().put(newSlot));
                }

                else if ((i == this.size - 1 && this.getSlot(i).compare(newSlot) == -1) || (this.getSlot(i).compare(newSlot) == -1 && this.getSlot(i + 1).compare(newSlot) == 1))
                {
                    return this.addKeySlot(this.getSlot(i).getSubTree().put(newSlot));
                }
            }
        }

        return this.addBaseNodeSlot(newSlot);
    }


    /**
     * Augments a list to the end of another list
     * @param original Original list
     * @param augment List augmenting
     */
    private void augmentList(ArrayList<Row> original, ArrayList<Row> augment)
    {
        for (int i = 0; i < augment.size(); i++)
        {
            original.add(augment.get(i));
        }
    }

    /**
     * Gets the values at a baseNode where comparison is equal to a combination of 0 & 1, 0 & -1, 1 & -1, 0, 1, or 1
     * @param key Key for comparison
     * @param comparison1 0, -1, 1
     * @param comparison2 0, -1, 1
     * @return Rows from base
     */
    private ArrayList<Row> getValuesAtBaseNode(DataPoint key, Integer comparison1, Integer comparison2)
    {
        ArrayList<Row> baseValues = new ArrayList<>();

        for (int i = 0; i < this.size; i++)
        {
            int cmp = this.getSlot(i).compare(key);

            if (cmp == comparison1)
            {
                this.augmentList(baseValues, this.getSlot(i).getValue());
            }

            else if (comparison2 != null && cmp == comparison2)
            {
                this.augmentList(baseValues, this.getSlot(i).getValue());
            }
        }

        return baseValues;
    }


    /**
     * Gets the next level of a nodes for equal to
     * @param key Key equal to
     * @return Subtree
     */
    private BTreeNode getNextLevelForEqualTo(DataPoint key)
    {
        for (int i = 0; i < this.size; i++)
        {
            int cmp = this.getSlot(i).compare(key);

            if (cmp == 0 || (i == this.size - 1 && cmp == -1) || (cmp == -1 && this.getSlot(i + 1).compare(key) == 1) )
            {
                return this.getSlot(i).getSubTree();
            }
        }

        return null;
    }


    /**
     * Gets rows whose key equals given key
     * @param key Given key
     * @return Rows equal to key
     */
    protected ArrayList<Row> getEqualTo(DataPoint key)
    {
        if (this.isBaseNode()) return this.getValuesAtBaseNode(key, 0, null);

        return this.getNextLevelForEqualTo(key).getEqualTo(key);
    }


    /**
     * Gets the next level of a nodes for not equal to
     * @return BTreeNodes containing values not equal to key
     */
    private ArrayList<BTreeNode> getNextLevelForNotEqualTo()
    {
        ArrayList<BTreeNode> nextLevel = new ArrayList<BTreeNode>();

        for (int i = 0; i < this.size; i++)
        {
            nextLevel.add(this.slots.get(i).getSubTree());
        }

        return nextLevel;
    }


    /**
     * Gets rows whose key not equal to given key
     * @param key Given key
     * @return Rows not equal to key
     */
    protected ArrayList<Row> getNotEqualTo(DataPoint key)
    {
        if (this.isBaseNode()) return this.getValuesAtBaseNode(key, -1, 1);

        ArrayList<BTreeNode> nextLevel = this.getNextLevelForNotEqualTo();
        ArrayList<Row> rowsNotEqualTo = new ArrayList<>();

        for (int i = 0; i < nextLevel.size(); i++)
        {
            this.augmentList(rowsNotEqualTo, nextLevel.get(i).getNotEqualTo(key));
        }

        return rowsNotEqualTo;
    }


    /**
     * Gets the next level of a nodes for greater than
     * @param key Key greater than
     * @return Subtree
     */
    private ArrayList<BTreeNode> getNextLevelForGreaterThan(DataPoint key)
    {
        ArrayList<BTreeNode> nextLevel = new ArrayList<BTreeNode>();

        for (int i = 0; i < this.size; i++)
        {
            int cmp = this.getSlot(i).compare(key);

            if (i == this.size - 1)
            {
                nextLevel.add(this.slots.get(i).getSubTree());
                return nextLevel;
            }

            else if (cmp == 1 || ((cmp == -1 || cmp == 0) && this.getSlot(i + 1).compare(key) == 1))
            {
                for (int j = i; j < this.size; j++)
                {
                    nextLevel.add(this.slots.get(j).getSubTree());
                }

                 return nextLevel;
            }
        }

        return nextLevel;
    }


    /**
     * Gets rows whose key greater given key
     * @param key Given key
     * @return Rows greater than key
     */
    protected ArrayList<Row> getGreaterThan(DataPoint key)
    {
        if (this.isBaseNode()) return this.getValuesAtBaseNode(key, 1, null);

        ArrayList<BTreeNode> nextLevel = this.getNextLevelForGreaterThan(key);
        ArrayList<Row> rowsGreaterThan = new ArrayList<>();

        for (int i = 0; i < nextLevel.size(); i++)
        {
            this.augmentList(rowsGreaterThan, nextLevel.get(i).getGreaterThan(key));
        }

        return rowsGreaterThan;
    }


    /**
     * Gets the next level of a nodes for less than
     * @param key Key less than
     * @return Subtree
     */
    private ArrayList<BTreeNode> getNextLevelForLessThan(DataPoint key)
    {
        ArrayList<BTreeNode> nextLevel = new ArrayList<BTreeNode>();

        for (int i = 0; i < this.size; i++)
        {
            int cmp = this.getSlot(i).compare(key);

            if (cmp == 1 || cmp == 0)
            {
                break;
            }

            else
            {
                nextLevel.add(this.slots.get(i).getSubTree());
            }
        }

        return nextLevel;
    }


    /**
     * Gets rows whose key less than given key
     * @param key Given key
     * @return Rows less than key
     */
    protected ArrayList<Row> getLessThan(DataPoint key)
    {
        if (this.isBaseNode()) return this.getValuesAtBaseNode(key, -1, null);

        ArrayList<BTreeNode> nextLevel = this.getNextLevelForLessThan(key);
        ArrayList<Row> rowsLessThan = new ArrayList<>();

        for (int i = 0; i < nextLevel.size(); i++)
        {
            this.augmentList(rowsLessThan, nextLevel.get(i).getLessThan(key));
        }

        return rowsLessThan;
    }


    /**
     * Gets the next level of a nodes for greater than or equal to
     * @param key Key less than
     * @return Subtree
     */
    private ArrayList<BTreeNode> getNextLevelForGreaterThanOrEqualTo(DataPoint key)
    {
        ArrayList<BTreeNode> nextLevel = new ArrayList<BTreeNode>();

        for (int i = 0; i < this.size; i++)
        {
            int cmp = this.getSlot(i).compare(key);

            if (i == this.size - 1)
            {
                nextLevel.add(this.slots.get(i).getSubTree());
                return nextLevel;
            }

            else if (cmp == 0 || cmp == 1 || (cmp == -1 && (this.getSlot(i + 1).compare(key) == 0 || this.getSlot(i + 1).compare(key) == 1)))
            {
                for (int j = i; j < this.size; j++)
                {
                    nextLevel.add(this.slots.get(j).getSubTree());
                }

                return nextLevel;
            }
        }

        return nextLevel;
    }


    /**
     * Gets rows whose key greater than or equal to given key
     * @param key Given key
     * @return Rows greater than or equal to key
     */
    protected ArrayList<Row> getGreaterThanOrEqualTo(DataPoint key)
    {
        if (this.isBaseNode()) return this.getValuesAtBaseNode(key, 0, 1);

        ArrayList<BTreeNode> nextLevel = this.getNextLevelForGreaterThanOrEqualTo(key);
        ArrayList<Row> rowsGreaterThanOrEqualTo = new ArrayList<>();

        for (int i = 0; i < nextLevel.size(); i++)
        {
            this.augmentList(rowsGreaterThanOrEqualTo, nextLevel.get(i).getGreaterThanOrEqualTo(key));
        }

        return rowsGreaterThanOrEqualTo;
    }


    /**
     * Gets the next level of a nodes for less than or equal to
     * @return BTreeNodes containing values less then or equal to key
     */
    private ArrayList<BTreeNode> getNextLevelForLessThanOrEqualTo(DataPoint key)
    {
        ArrayList<BTreeNode> nextLevel = new ArrayList<BTreeNode>();

        for (int i = 0; i < this.size; i++)
        {
            int cmp = this.getSlot(i).compare(key);

            if (cmp == 0 || cmp == -1)
            {
                nextLevel.add(this.slots.get(i).getSubTree());
            }

            else
            {
                break;
            }
        }

        return nextLevel;
    }


    /**
     * Gets rows whose key less than or equal to given key
     * @param key Given key
     * @return Rows less than or equal to key
     */
    protected ArrayList<Row> getLessThanOrEqualTo(DataPoint key)
    {
        if (this.isBaseNode()) return this.getValuesAtBaseNode(key, -1, 0);

        ArrayList<BTreeNode> nextLevel = this.getNextLevelForLessThanOrEqualTo(key);
        ArrayList<Row> rowsLessThanOrEqualTo = new ArrayList<>();

        for (int i = 0; i < nextLevel.size(); i++)
        {
            this.augmentList(rowsLessThanOrEqualTo, nextLevel.get(i).getLessThanOrEqualTo(key));
        }

        return rowsLessThanOrEqualTo;
    }


    /**
     * Deletes a row from a tree
     * @param key Key to get base node row in
     * @param rowDeleting Row deleting
     */
    protected void delete(DataPoint key, Row rowDeleting)
    {
        ArrayList<Row> rowsThatMatchKey = this.getEqualTo(key);

        for (int i = 0; i < rowsThatMatchKey.size(); i++)
        {
            if (rowsThatMatchKey.get(i).equals(rowDeleting))
            {
                rowsThatMatchKey.remove(i);
                break;
            }
        }
    }


    public String toString()
    {
        String str = "(";

        for (int i = 0; i < this.size; i++)
        {
            str = str + this.slots.get(i);
        }

        return str + ")";
    }

}

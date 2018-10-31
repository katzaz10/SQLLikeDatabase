package edu.yu.data.finalProject;

public class BTree
{
    private BTreeNode root = new BTreeNode();

    protected BTree()
    {
        this.root.addSlot(0, new BTreeNodeSlot(null));
    }

    protected BTreeNode getRoot()
    {
        return this.root;
    }

    /**
     * Adds a row to the BtREE
     * @param key Key for placement
     * @param row Row to place
     */
    protected void put(DataPoint key, Row row)
    {
        BTreeNodeSlot newSlot = new BTreeNodeSlot(key, row);
        BTreeNodeSlot finalPush = this.root.put(newSlot);

        if (finalPush != null)
        {
            BTreeNodeSlot sentinel = new BTreeNodeSlot(this.root.getSlot(0).getKey(), this.root);
            BTreeNode newRoot = new BTreeNode();
            newRoot.addSlot(0, sentinel);
            newRoot.addSlot(1, finalPush);
            this.root = newRoot;
        }
    }

    public String toString()
    {
        if (this.root == null)
        {
            return "null";
        }

        return this.root.toString();
    }
}




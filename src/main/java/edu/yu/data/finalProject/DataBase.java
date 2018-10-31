package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import net.sf.jsqlparser.JSQLParserException;
import java.util.ArrayList;
import java.util.HashMap;



/**
 * System of tables that hold data in a single system
 * @author Avraham Katz
 */

public class DataBase {

    private final HashMap<String, Integer> tableNameToIndex = new HashMap<String, Integer>();
    private final ArrayList<Table> tables = new ArrayList<>();

    protected DataBase()
    {

    }


    /**
     * Adds a table to the database
     * @param tableName Name of table adding
     * @param table Table adding
     */
    private void addTable(String tableName, Table table)
    {
        for (int i = 0; i < tables.size(); i++)
        {
            if (tables.get(i).getTableName().toLowerCase().equals(tableName.toLowerCase()))
            {
                throw new IllegalArgumentException("table " + tableName + " already exists");
            }
        }

        this.tableNameToIndex.put(table.getTableName(), tables.size());
        this.tables.add(table);
    }


    /**
     * Gets a table from a database
     * @param tableName Name of table getting
     * @return Table getting
     */
    protected Table getTable(String tableName)
    {
        for (int i = 0; i < tables.size(); i++)
        {
            if (this.tables.get(i).getTableName().toLowerCase().equals(tableName.toLowerCase()))
            {
                break;
            }

            else if (i == tables.size() - 1)
            {
                throw new IllegalArgumentException("table " + tableName + " does not exist");
            }
        }

        int tableIndex = this.tableNameToIndex.get(tableName);
        return this.tables.get(tableIndex);
    }


    /**
     * Gets all the tables in the database
     * @return All the tables in the database
     */
    private ArrayList<Table> getAllTables()
    {
       return this.tables;
    }


    /**
     * Performs the actions laid out by a CreateTableQuery
     * @param tableQuery CreateTableQuery performing
     * @return ResultSet of action
     */
    private ResultSet performCreateTableQuery(CreateTableQuery tableQuery)
    {
        Table newTable = new Table(tableQuery.getTableName(), tableQuery.getColumnDescriptions().length, tableQuery.getPrimaryKeyColumn().getColumnName());
        newTable.addAllColumns(tableQuery.getColumnDescriptions());
        newTable.refactorPrimaryKey(newTable.getPrimaryKeyColumn());
        newTable.createTableIndex(newTable.getPrimaryKeyColumn(), newTable.getPrimaryKeyColumn() + "_Index");

        this.addTable(newTable.getTableName(), newTable);

        return new ResultSet(newTable.getColumnInfos());
    }


    /**
     * Performs the actions laid out by a InsertQuery
     * @param insertQuery InsertQuery performing
     * @return ResultSet of action
     */
    private ResultSet performInsertQuery(InsertQuery insertQuery)
    {
        String tableName = insertQuery.getTableName();

        this.getTable(tableName).insertRow(insertQuery.getColumnValuePairs());

        return new ResultSet(true);
    }


    /**
     * Performs the actions laid out by a DeleteQuery
     * @param deleteQuery DeleteQuery performing
     * @return ResultSet of action
     */
    private ResultSet performDeleteQuery(DeleteQuery deleteQuery)
    {
        String tableName = deleteQuery.getTableName();

        Delete deleter = new Delete(this.getTable(tableName), deleteQuery);
        deleter.delete();

        return new ResultSet(true);
    }


    /**
     * Performs the actions laid out by a UpdateQuery
     * @param updateQuery UpdateQuery performing
     * @return ResultSet of action
     */
    private ResultSet performUpdateQuery(UpdateQuery updateQuery)
    {
        String tableName = updateQuery.getTableName();
        Update updater = new Update(this.getTable(tableName), updateQuery);
        updater.update();

        return new ResultSet(true);
    }


    /**
     * Performs the actions laid out by a SelectQuery
     * @param selectQuery SelectQuery performing
     * @return ResultSet of action
     */
    private ResultSet performSelectQuery(SelectQuery selectQuery)
    {
        Select selecter = new Select(this.getTable(selectQuery.getFromTableNames()[0]), selectQuery);

        return selecter.runSelect();
    }

    private ResultSet performCreateIndexQuery(CreateIndexQuery createIndexQuery)
    {
        String tableName = createIndexQuery.getTableName();
        String columnName = createIndexQuery.getColumnName();
        String indexName = createIndexQuery.getIndexName();

        this.getTable(tableName).createTableIndex(columnName, indexName);

        return new ResultSet(true);
    }

    /**
     * Parses an SQL query as a String into a SQLQuery
     * @param SQL Query to parse
     * @return SQLQuery representation of query
     * @throws JSQLParserException
     */
    private SQLQuery parseQuery(String SQL) throws JSQLParserException
    {
        SQLParser parser = new SQLParser();
        return parser.parse(SQL);
    }


    /**
     * Determines what type of Query the SQLQuery is, and runs that query
     * @param query Query running
     * @return ResultSet of queries action
     */
    private ResultSet sortAndRun(SQLQuery query)
    {
        String queryType = query.getClass().toString();

        switch(queryType)
        {
            case("class edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateTableQuery"): return this.performCreateTableQuery((CreateTableQuery) query);
            case("class edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.CreateIndexQuery"): return this.performCreateIndexQuery((CreateIndexQuery) query);
            case("class edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.InsertQuery"):      return this.performInsertQuery((InsertQuery) query);
            case("class edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.DeleteQuery"):      return this.performDeleteQuery((DeleteQuery) query);
            case("class edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.SelectQuery"):      return this.performSelectQuery((SelectQuery) query);
            case("class edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.UpdateQuery"):      return this.performUpdateQuery((UpdateQuery) query);
            default:                                                                          return new ResultSet(false);
        }
    }


    /**
     * Parses and runs an SQL query
     * @param SQL String of query trying to execute
     * @return ResultSet of action
     */
    public ResultSet execute(String SQL)
    {
        try
        {
            SQLQuery query = parseQuery(SQL);
            return this.sortAndRun(query);
        }

        catch (JSQLParserException all)
        {
            return new ResultSet(false, all.getMessage());
        }

        catch (IllegalArgumentException all)
        {
            return new ResultSet(false, all.getMessage());
        }
    }


    protected void print()
    {
       ArrayList<Table> allTables = this.getAllTables();

       for (int i = 0; i < allTables.size(); i++)
       {
           allTables.get(i).print();
       }
    }

    @Override
    public String toString()
    {
        String dataBaseString = "";

        for (int i = 0; i < this.getAllTables().size(); i++)
        {
            dataBaseString = dataBaseString + this.getAllTables().get(i).toString();
        }

        return dataBaseString;
    }
}

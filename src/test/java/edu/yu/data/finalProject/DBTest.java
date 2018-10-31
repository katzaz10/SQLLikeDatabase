package edu.yu.data.finalProject;

import edu.yu.cs.dataStructures.fall2016.SimpleSQLParser.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for DataBase
 * @author Avraham Katz
 */


public class DBTest
{


////////////////    HELPER METHODS    /////////////////

    private DataBase createFilledTestBase()
    {
        DataBase testBase = new DataBase();

        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " CurrentStudent boolean DEFAULT true,"
                + " PRIMARY KEY (BannerID)"
                + ");");

        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni', 2.3, 800012345);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Avi','Katz',3.92, 800373678, 1234567890);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Eli','Weiss',3.76, 800456789, 0293847563);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Eli','Weiss',2.76, 800456780, 0293847566);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Jodan','Lewis',2.91, 800356780, 0293847567);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Lemar','Jurr',3.8, 800256780, 0293847568);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Julio','Gonzalez',3.2, 800156780, 0293847569);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Mark','Cohen',2.6, 800056780, 0293847570);");

        return testBase;
    }


    private void preExecutePrint(DataBase base)
    {
        System.out.println("DATABASE_BEFORE:");
        base.print();
    }

    private void postExecutePrint(DataBase base, ResultSet result, String query, String split)
    {
        System.out.println("DATABASE_AFTER_QUERY(" + query + ")");
        base.print();
        System.out.println("RESULTSET:");
        result.print();
        
        if (split.equals("split"))
        {
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
        }
    }


////////////////    CREATE TABLE    /////////////////



    @Test
    public void createTableHappyPath()
    {
        DataBase testBase = new DataBase();
        this.preExecutePrint(testBase);

        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");";

        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ColumnInfo[] resultColumns = new ColumnInfo[4];
        resultColumns[0] = new ColumnInfo("BannerID", ColumnDescription.DataType.INT);
        resultColumns[1] = new ColumnInfo("FirstName", ColumnDescription.DataType.VARCHAR);
        resultColumns[2] = new ColumnInfo("LastName", ColumnDescription.DataType.VARCHAR);
        resultColumns[3] = new ColumnInfo("GPA", ColumnDescription.DataType.DECIMAL);

        ResultSet solutionSet = new ResultSet(resultColumns);

        Assert.assertEquals(solutionSet, result);
        Assert.assertEquals("COLUMNS: GPA, FirstName, BannerID, LastName | ROWS: ", testBase.toString());
    }

    @Test
    public void createTablePrimaryKeyCannotBeDefault()
    {
        DataBase testBase = new DataBase();
        this.preExecutePrint(testBase);

        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (GPA)"
                + ");";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void createTableColumnNameTwice()
    {
        DataBase testBase = new DataBase();
        this.preExecutePrint(testBase);

        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " firstName int,"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (GPA)"
                + ");";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void createTableEmptyColumnName()
    {
        DataBase testBase = new DataBase();
        this.preExecutePrint(testBase);

        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " '    ' int,"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (GPA)"
                + ");";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");
        
        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void createTableNextLineSymbol()
    {
        DataBase testBase = new DataBase();
        this.preExecutePrint(testBase);

        String query = "CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " \n int,"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (GPA)"
                + ");";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
    }


////////////////    INSERT    /////////////////


    @Test
    public void insertHappyPath()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni', 2.3, 800012345);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");
        
        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: GPA, FirstName, BannerID, LastName | ROWS: <2.3 'Ploni' 800012345 'Almoni'>", testBase.toString());
    }

    @Test
    public void insertIntIntoBooleanColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " CurrentStudent boolean,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, CurrentStudent) VALUES ('Ploni','Almoni', 2.3, 800012345, 1);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertDecimalIntoBooleanColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " CurrentStudent boolean,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, CurrentStudent) VALUES ('Ploni','Almoni', 2.3, 800012345, 1.3);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertVarcharIntoBooleanColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " CurrentStudent boolean,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, BannerID, CurrentStudent) VALUES ('Ploni','Almoni', 800012345, 'Bad');";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");
        
        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertVarcharIntoDecimalColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni', 'bad', 800012345);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertBooleanIntoDecimalColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni', false, 800012345);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertDecimalIntoIntColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni', 2.4, 8.9);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertVarcharIntoIntColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni', 2.4, 'bad');";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertBooleanIntoIntColumn()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ploni','Almoni', 2.4, false);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertRowDoesNotUpholdColumnsUniqueStatus()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Ariella','Katz',3.95, 800373679, 0293847567);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertRowDoesNotUpholdColumnsNotNullStatus()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, GPA, BannerID, SSNum) VALUES ('Ariella',3.95, 800373680, 1234567896);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertRowUpholdsColumnDefaultValue()
    {
        DataBase testBase = new DataBase();

        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, BannerID, SSNum) VALUES ('Ariella','Katz', 800373680, 1234567896);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: GPA, FirstName, BannerID, LastName, SSNum | ROWS: <0.0 'Ariella' 800373680 'Katz' 1234567896>", testBase.toString());
    }

    @Test
    public void insertRowDoesNotUpholdColumnsVarcharLength()
    {
        DataBase testBase = new DataBase();

        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " FirstName varchar(255),"
                + " LastName varchar(2) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, BannerID, SSNum) VALUES ('Ariella','Katz', 800373680, 1234567896);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertRowDoesNotUpholdColumnsWholeNumberLength()
    {
        DataBase testBase = new DataBase();

        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Ariella','Katz', 213.2 800373680, 1234567896);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
    }

    @Test
    public void insertRowDoesNotUpholdColumnsFractionalLength()
    {
        DataBase testBase = new DataBase();

        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('Ariella','Katz', 3.224, 800373680);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        ResultSet solutionSet1 = new ResultSet(new DataPoint("false", ColumnDescription.DataType.BOOLEAN));

        Assert.assertEquals(solutionSet1, result);
        Assert.assertEquals("COLUMNS: GPA, FirstName, BannerID, LastName | ROWS: ", testBase.toString());
    }


    @Test
    public void insertEmptyVarchar()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        this.preExecutePrint(testBase);

        String query = "INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('    ','Empty', 2.3, 800012345);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: GPA, FirstName, BannerID, LastName | ROWS: <2.3 null 800012345 'Empty'>", testBase.toString());
    }


////////////////    INDEX    /////////////////

    @Test
    public void indexPrimaryKeyColumnAutimatically()
    {
        DataBase testBase = this.createFilledTestBase();
        Assert.assertEquals("BannerID_Index -> "
                    + "([null:([null:([null:][800012345:<true 2.3 'Ploni' 800012345 'Almoni' null>])]"
                    + "[800056780:([800056780:<true 2.6 'Mark' 800056780 'Cohen' 293847570>]"
                    + "[800156780:<true 3.2 'Julio' 800156780 'Gonzalez' 293847569>])])]"
                    + "[800256780:([800256780:([800256780:<true 3.8 'Lemar' 800256780 'Jurr' 293847568>]"
                    + "[800356780:<true 2.91 'Jodan' 800356780 'Lewis' 293847567>])]"
                    + "[800373678:([800373678:<true 3.92 'Avi' 800373678 'Katz' 1234567890>]"
                    + "[800456780:<true 2.76 'Eli' 800456780 'Weiss' 293847566>]"
                    + "[800456789:<true 3.76 'Eli' 800456789 'Weiss' 293847563>]"
                    + ")])])", testBase.getTable("YCStudent").getTableIndex("BannerID").toString());
    }

    @Test
    public void createIndex()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "CREATE INDEX GPA_Index on YCStudent (GPA);";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("GPA_Index -> "
                    + "([null:([null:][2.3:<true 2.3 'Ploni' 800012345 'Almoni' null>]"
                    + "[2.6:<true 2.6 'Mark' 800056780 'Cohen' 293847570>])]"
                    + "[2.76:([2.76:<true 2.76 'Eli' 800456780 'Weiss' 293847566>]"
                    + "[2.91:<true 2.91 'Jodan' 800356780 'Lewis' 293847567>]"
                    + "[3.2:<true 3.2 'Julio' 800156780 'Gonzalez' 293847569>])]"
                    + "[3.76:([3.76:<true 3.76 'Eli' 800456789 'Weiss' 293847563>]"
                    + "[3.8:<true 3.8 'Lemar' 800256780 'Jurr' 293847568>]"
                    + "[3.92:<true 3.92 'Avi' 800373678 'Katz' 1234567890>]"
                    + ")])", testBase.getTable("YCStudent").getTableIndex("GPA").toString());
    }


    @Test
    public void deleteGreaterThanDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE GPA > 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570> "
                + "<true 3.0 'Border' 800012222 'Person' 293847777>", testBase.toString());
    }

    @Test
    public void deleteLessThanDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE GPA < 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 3.0 'Border' 800012222 'Person' 293847777>", testBase.toString());
    }

    @Test
    public void deleteEqualToDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE GPA = 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteNotEqualToDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE GPA <> 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.0 'Border' 800012222 'Person' 293847777>", testBase.toString());
    }

    @Test
    public void deleteGreaterThanOrEqualToDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE GPA >= 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteLessThanOrEqualToDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE GPA <= 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569>", testBase.toString());
    }

    @Test
    public void deleteGreaterThanInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE BannerID > 800156780;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteLessThanInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE BannerID < 800156780;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569>", testBase.toString());
    }

    @Test
    public void deleteEqualTo()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE BannerID = 800012345;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteNotEqualToInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE BannerID <> 800012345;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null>", testBase.toString());
    }

    @Test
    public void deleteGreaterThanOrEqualToInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE BannerID >= 800156780;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteLessThanOrEqualToInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE BannerID <= 800156780;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568>", testBase.toString());
    }

    @Test
    public void deleteGreaterThanVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE FirstName > 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566>", testBase.toString());
    }

    @Test
    public void deleteLessThanVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE FirstName < 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteEqualToVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE FirstName = 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteNotEqualToVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE FirstName <> 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566>", testBase.toString());
    }

    @Test
    public void deleteGreaterThanOrEqualToVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE FirstName >= 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890>", testBase.toString());
    }

    @Test
    public void deleteLessThanOrEqualToVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE FirstName <= 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");
        
        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteGreaterThanBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent > False;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");
        

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<false 3.0 'Border' 800012222 'Person' 293847777>", testBase.toString());
    }

    @Test
    public void deleteLessThanBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent < True;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteEqualToBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent = True;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<false 3.0 'Border' 800012222 'Person' 293847777>", testBase.toString());
    }


    @Test
    public void deleteNotEqualToBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent <> True;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteGreaterThanOrEqualTo()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent >= False;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: ", testBase.toString());
    }

    @Test
    public void deleteGreaterThanOrEqualTo2()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent >= True;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<false 3.0 'Border' 800012222 'Person' 293847777>", testBase.toString());
    }

    @Test
    public void deleteLessThanOrEqualTo()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent <= True;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: ", testBase.toString());
    }

    @Test
    public void deleteLessThanOrEqualTo2()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, BannerID, SSNum) VALUES (false, 'Border','Person', 3.0, 800012222, 293847777);");
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent WHERE CurrentStudent <= False;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void deleteAll()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "DELETE FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: ", testBase.toString());
    }


    @Test
    public void deleteOnlyOnIndex()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        testBase.execute("CREATE INDEX GPA_Index on YCStudent (GPA);");
        String query = "DELETE FROM YCStudent WHERE (GPA > 3.75 AND BannerID < 800456789) OR GPA < 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569>", testBase.toString());
    }


////////////////   UPDATE    /////////////////


    @Test
    public void updateHappyPath()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "UPDATE YCStudent SET GPA=3.0 WHERE BannerID=800012345 OR FirstName='Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 3.0 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.0 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 3.0 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void updateUniqueColumnStopUpdate()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "UPDATE YCStudent SET BannerID=800012345 WHERE FirstName='Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void updateUniqueColumnContinueUpdate()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "UPDATE YCStudent SET BannerID=800012346 WHERE FirstName='Jodan';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800012346 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }


    @Test
    public void updateUniqueColumnContinueUpdateIndexOnly()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        ResultSet a = testBase.execute("CREATE INDEX GPA_Index on YCStudent (GPA);");
        String query = "UPDATE YCStudent SET GPA = 2.22 WHERE BannerID=800373678 OR GPA < 2.7;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.22 'Ploni' 800012345 'Almoni' null> "
                + "<true 2.22 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.22 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }


    @Test
    public void updateUsingNull()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "UPDATE YCStudent SET CurrentStudent = false WHERE SSNum <> NULL;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<false 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<false 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<false 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<false 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<false 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<false 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<false 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }

    @Test
    public void updateUsingEmptyVarcharAsNull()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID) VALUES ('    ','Empty', 2.3, 800012345);");
        this.preExecutePrint(testBase);

        String query = "UPDATE YCStudent SET CurrentStudent = false WHERE FirstName <> '    ';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(true), result);
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<false 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<false 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<false 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<false 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<false 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<false 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<false 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<false 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
    }


////////////////   SELECT    /////////////////


    @Test
    public void selectRows()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT * FROM YCStudent WHERE GPA > 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent BOOLEAN, GPA DECIMAL, FirstName VARCHAR, BannerID INT, LastName VARCHAR, SSNum INT | "
                + "RESULT: "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> |", result.toString());
    }

    @Test
    public void selectRowsOnlyOnIndex()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        testBase.execute("CREATE INDEX GPA_Index on YCStudent (GPA);");
        String query = "SELECT * FROM YCStudent WHERE (GPA > 3.0 AND BannerID = 800373678) OR GPA <= 2.91;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent BOOLEAN, GPA DECIMAL, FirstName VARCHAR, BannerID INT, LastName VARCHAR, SSNum INT "
                + "| RESULT: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570> |", result.toString());
    }


    @Test
    public void selectRowsAndColumns()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT FirstName, LastName FROM YCStudent WHERE GPA > 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<'Avi' 'Katz'> "
                + "<'Eli' 'Weiss'> "
                + "<'Lemar' 'Jurr'> "
                + "<'Julio' 'Gonzalez'> |", result.toString());
    }

    @Test
    public void selectComplexWhereCondition()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT FirstName, LastName, BannerID FROM YCStudent WHERE ((GPA <> 3.76 AND BannerID > 800300000) OR (GPA = 3.76 AND BannerID = 800456780)) OR SSNum <= null;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "FirstName VARCHAR, LastName VARCHAR, BannerID INT "
                + "| RESULT: "
                + "<'Ploni' 'Almoni' 800012345> "
                + "<'Avi' 'Katz' 800373678> "
                + "<'Eli' 'Weiss' 800456780> "
                + "<'Jodan' 'Lewis' 800356780> |", result.toString());
    }

    @Test
    public void selectDistinctRows()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE FirstName = 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "FirstName VARCHAR, LastName VARCHAR "
                + "| RESULT: "
                + "<'Eli' 'Weiss'> |", result.toString());
    }

    @Test
    public void selectDistinctRows2()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT DISTINCT FirstName, LastName, BannerID FROM YCStudent WHERE FirstName = 'Eli';";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "FirstName VARCHAR, LastName VARCHAR, BannerID INT "
                + "| RESULT: "
                + "<'Eli' 'Weiss' 800456789> "
                + "<'Eli' 'Weiss' 800456780> |", result.toString());
    }

    @Test
    public void orderByAscendingInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName, BannerID FROM YCStudent WHERE GPA > 3.0 ORDER BY BannerID ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR, BannerID INT | "
                + "RESULT: "
                + "<3.2 'Julio' 'Gonzalez' 800156780> "
                + "<3.8 'Lemar' 'Jurr' 800256780> "
                + "<3.92 'Avi' 'Katz' 800373678> "
                + "<3.76 'Eli' 'Weiss' 800456789> |", result.toString());
    }

    @Test
    public void orderByDescendingInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName, BannerID FROM YCStudent WHERE GPA > 3.0 ORDER BY BannerID DESC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR, BannerID INT | "
                + "RESULT: "
                + "<3.76 'Eli' 'Weiss' 800456789> "
                + "<3.92 'Avi' 'Katz' 800373678> "
                + "<3.8 'Lemar' 'Jurr' 800256780> "
                + "<3.2 'Julio' 'Gonzalez' 800156780> |", result.toString());
    }

    @Test
    public void orderByAscendingDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName, BannerID FROM YCStudent WHERE GPA > 3.0 ORDER BY GPA ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR, BannerID INT | "
                + "RESULT: "
                + "<3.2 'Julio' 'Gonzalez' 800156780> "
                + "<3.76 'Eli' 'Weiss' 800456789> "
                + "<3.8 'Lemar' 'Jurr' 800256780> "
                + "<3.92 'Avi' 'Katz' 800373678> |", result.toString());
    }

    @Test
    public void orderByDescendingDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName, BannerID FROM YCStudent WHERE GPA > 3.0 ORDER BY GPA DESC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR, BannerID INT | "
                + "RESULT: "
                + "<3.92 'Avi' 'Katz' 800373678> "
                + "<3.8 'Lemar' 'Jurr' 800256780> "
                + "<3.76 'Eli' 'Weiss' 800456789> "
                + "<3.2 'Julio' 'Gonzalez' 800156780> |", result.toString());
    }

    @Test
    public void orderByAscendingBoolean()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " CurrentStudent boolean,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, CurrentStudent) VALUES ('Ploni','Almoni', 2.3, 800012345, true);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi','Katz',3.92, 800373678, 1234567890, true);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Eli','Weiss',2.76, 800456780, 0293847566, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Jodan','Lewis',2.91, 800356780, 0293847567, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Lemar','Jurr',3.8, 800256780, 0293847568);");
        this.preExecutePrint(testBase);

        String query = "SELECT CurrentStudent, FirstName, LastName FROM YCStudent ORDER BY CurrentStudent ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent BOOLEAN, FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<null 'Lemar' 'Jurr'> "
                + "<false 'Jodan' 'Lewis'> "
                + "<false 'Eli' 'Weiss'> "
                + "<true 'Avi' 'Katz'> "
                + "<true 'Ploni' 'Almoni'> |", result.toString());
    }

    @Test
    public void orderByDescendingBoolean()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE YCStudent"
                + "("
                + " BannerID int,"
                + " SSNum int UNIQUE,"
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " GPA decimal(1,2) DEFAULT 0.00,"
                + " CurrentStudent boolean,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, CurrentStudent) VALUES ('Ploni','Almoni', 2.3, 800012345, true);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi','Katz',3.92, 800373678, 1234567890, true);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Eli','Weiss',2.76, 800456780, 0293847566, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Jodan','Lewis',2.91, 800356780, 0293847567, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Lemar','Jurr',3.8, 800256780, 0293847568);");
        this.preExecutePrint(testBase);

        String query = "SELECT CurrentStudent, FirstName, LastName FROM YCStudent ORDER BY CurrentStudent DESC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent BOOLEAN, FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<true 'Avi' 'Katz'> "
                + "<true 'Ploni' 'Almoni'> "
                + "<false 'Jodan' 'Lewis'> "
                + "<false 'Eli' 'Weiss'> "
                + "<null 'Lemar' 'Jurr'> |", result.toString());
    }

    @Test
    public void orderByAscendingVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName, BannerID FROM YCStudent WHERE GPA > 3.0 ORDER BY LastName ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR, BannerID INT | "
                + "RESULT: "
                + "<3.2 'Julio' 'Gonzalez' 800156780> "
                + "<3.8 'Lemar' 'Jurr' 800256780> "
                + "<3.92 'Avi' 'Katz' 800373678> "
                + "<3.76 'Eli' 'Weiss' 800456789> |", result.toString());
    }

    @Test
    public void orderByDescendingVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName, BannerID FROM YCStudent WHERE GPA > 3.0 ORDER BY LastName DESC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR, BannerID INT | "
                + "RESULT: "
                + "<3.76 'Eli' 'Weiss' 800456789> "
                + "<3.92 'Avi' 'Katz' 800373678> "
                + "<3.8 'Lemar' 'Jurr' 800256780> "
                + "<3.2 'Julio' 'Gonzalez' 800156780> |", result.toString());
    }

    @Test
    public void orderByDoubleAscending()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName FROM YCStudent WHERE GPA < 3.77 ORDER BY FirstName ASC, GPA ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<2.76 'Eli' 'Weiss'> "
                + "<3.76 'Eli' 'Weiss'> "
                + "<2.91 'Jodan' 'Lewis'> "
                + "<3.2 'Julio' 'Gonzalez'> "
                + "<2.6 'Mark' 'Cohen'> "
                + "<2.3 'Ploni' 'Almoni'> |", result.toString());
    }


    @Test
    public void orderByDoubleDescending()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.1, 800371111, 1234561111, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.2, 800372222, 1234562222, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.3, 800373333, 1234563333, false);");
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName FROM YCStudent WHERE GPA > 2.6 AND GPA < 3.77 ORDER BY LastName DESC, GPA DESC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<3.76 'Eli' 'Weiss'> "
                + "<2.76 'Eli' 'Weiss'> "
                + "<2.91 'Jodan' 'Lewis'> "
                + "<3.3 'Avi' 'Katz'> "
                + "<3.2 'Avi' 'Katz'> "
                + "<3.1 'Avi' 'Katz'> "
                + "<3.2 'Julio' 'Gonzalez'> |", result.toString());
    }

    @Test
    public void orderByAscendingDescending()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.1, 800371111, 1234561111, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.2, 800372222, 1234562222, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.3, 800373333, 1234563333, false);");
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName FROM YCStudent WHERE GPA > 2.6 AND GPA < 3.77 ORDER BY LastName ASC, GPA DESC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<3.2 'Julio' 'Gonzalez'> "
                + "<3.3 'Avi' 'Katz'> "
                + "<3.2 'Avi' 'Katz'> "
                + "<3.1 'Avi' 'Katz'> "
                + "<2.91 'Jodan' 'Lewis'> "
                + "<3.76 'Eli' 'Weiss'> "
                + "<2.76 'Eli' 'Weiss'> |", result.toString());
    }

    @Test
    public void orderByDescendingAscending()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.1, 800371111, 1234561111, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.2, 800372222, 1234562222, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.3, 800373333, 1234563333, false);");
        this.preExecutePrint(testBase);

        String query = "SELECT GPA, FirstName, LastName FROM YCStudent WHERE GPA > 2.6 AND GPA < 3.77 ORDER BY LastName DESC, GPA ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "GPA DECIMAL, FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<2.76 'Eli' 'Weiss'> "
                + "<3.76 'Eli' 'Weiss'> "
                + "<2.91 'Jodan' 'Lewis'> "
                + "<3.1 'Avi' 'Katz'> "
                + "<3.2 'Avi' 'Katz'> "
                + "<3.3 'Avi' 'Katz'> "
                + "<3.2 'Julio' 'Gonzalez'> |", result.toString());
    }


    @Test
    public void orderByDistinctDescendingAscending()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.1, 800371111, 1234561111, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.2, 800372222, 1234562222, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Katz', 3.3, 800373333, 1234563333, false);");
        this.preExecutePrint(testBase);

        String query = "SELECT DISTINCT FirstName, LastName FROM YCStudent WHERE GPA > 2.6 AND GPA < 3.77 ORDER BY LastName DESC, GPA ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<'Eli' 'Weiss'> "
                + "<'Jodan' 'Lewis'> "
                + "<'Avi' 'Katz'> "
                + "<'Julio' 'Gonzalez'> |", result.toString());
    }


    @Test
    public void orderByTripleRequirement()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Mark', 'Katz', 3.2, 800372222, 1234562222, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Barry', 'Katz', 3.1, 800371111, 1234561111, false);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum, CurrentStudent) VALUES ('Avi', 'Shapiro', 3.3, 800373333, 1234563333, false);");
        this.preExecutePrint(testBase);

        String query = "SELECT DISTINCT CurrentStudent, FirstName, LastName FROM YCStudent WHERE GPA > 2.6 AND GPA < 3.77 ORDER BY CurrentStudent ASC, LastName ASC, FirstName ASC;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent BOOLEAN, FirstName VARCHAR, LastName VARCHAR | "
                + "RESULT: "
                + "<false 'Barry' 'Katz'> "
                + "<false 'Mark' 'Katz'> "
                + "<false 'Avi' 'Shapiro'> "
                + "<true 'Julio' 'Gonzalez'> "
                + "<true 'Jodan' 'Lewis'> "
                + "<true 'Eli' 'Weiss'> |", result.toString());
    }

    @Test
    public void selectAverageDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT AVG(GPA) FROM YCStudent WHERE GPA > 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "AVG(GPA) DECIMAL | "
                + "RESULT: <3.67> |", result.toString());
    }

    @Test
    public void selectAverageInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT AVG(BannerID) FROM YCStudent WHERE GPA > 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "AVG(BannerID) DECIMAL | "
                + "RESULT: <8.0031100675E8> |", result.toString()); //prints string with E8, but same as calculztor output 800311006.75
    }

    @Test
    public void selectAverageBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT AVG(CurrentStudent) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void selectAverageVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT AVG(FirstName) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void selectCountWithoutNullValues()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT COUNT(GPA) FROM YCStudent WHERE GPA > 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "COUNT(GPA) INT | "
                + "RESULT: <4> |", result.toString());
    }

    @Test
    public void selectCountWithNullValues()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT COUNT(SSNum) FROM YCStudent WHERE GPA < 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "COUNT(SSNum) INT | "
                + "RESULT: <3> |", result.toString());
    }

    @Test
    public void selectMaxInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MAX(SSNum) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "MAX(SSNum) INT | "
                + "RESULT: <1234567890> |", result.toString());
    }

    @Test
    public void selectMaxDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MAX(GPA) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "MAX(GPA) DECIMAL | "
                + "RESULT: <3.92> |", result.toString());
    }

    @Test
    public void selectMaxVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MAX(LastName) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "MAX(LastName) VARCHAR | "
                + "RESULT: <'Weiss'> |", result.toString());
    }

    @Test
    public void selectMaxBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MAX(CurrentStudent) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void selectMinInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MIN(SSNum) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "MIN(SSNum) INT | "
                + "RESULT: <293847563> |", result.toString());
    }

    @Test
    public void selectMinDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MIN(GPA) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "MIN(GPA) DECIMAL | "
                + "RESULT: <2.3> |", result.toString());
    }

    @Test
    public void selectMinVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MIN(LastName) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "MIN(LastName) VARCHAR | "
                + "RESULT: <'Almoni'> |", result.toString());
    }

    @Test
    public void selectMinBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT MIN(CurrentStudent) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void selectSumInt()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT SUM(BannerID) FROM YCStudent WHERE BannerID > 800400000;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "SUM(BannerID) INT | "
                + "RESULT: <1600913569> |", result.toString());
    }

    @Test
    public void selectSumDecimal()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT SUM(GPA) FROM YCStudent WHERE GPA > 3.0;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "SUM(GPA) DECIMAL | "
                + "RESULT: <14.68> |", result.toString());
    }

    @Test
    public void selectSumBoolean()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT SUM(CurrentStudent) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");
        
        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void selectSumVarchar()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT SUM(FirstName) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");
        
        Assert.assertEquals(new ResultSet(false), result);
    }

    @Test
    public void selectCountDistinctColumn()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT COUNT(DISTINCT FirstName) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "COUNT(DISTINCT FirstName) INT | "
                + "RESULT: <7> |", result.toString());
    }

    @Test
    public void selectCountDistinctColumn2()
    {
        DataBase testBase = this.createFilledTestBase();
        this.preExecutePrint(testBase);

        String query = "SELECT COUNT(DISTINCT CurrentStudent) FROM YCStudent;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "COUNT(DISTINCT CurrentStudent) INT | "
                + "RESULT: <1> |", result.toString());
    }

    @Test
    public void selectSumDistinctColumn()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Shmuel','Marcus',3.92, 800137780, 0292547569);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('John','Shalom',3.92, 800227780, 0322547569);");
        this.preExecutePrint(testBase);

        String query = "SELECT SUM(DISTINCT GPA) FROM YCStudent WHERE GPA > 3.77;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "SUM(DISTINCT GPA) DECIMAL | "
                + "RESULT: <7.72> |", result.toString());
    }


    @Test
    public void selectSumDistinctColumnMasterTest()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Shmuel','Marcus',3.92, 800137780, 0292547569);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('John','Shalom',3.92, 800227780, 0322547569);");
        this.preExecutePrint(testBase);

        String query = "SELECT FirstName, SUM(DISTINCT GPA) FROM YCStudent WHERE GPA > 3.77;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "FirstName VARCHAR, SUM(DISTINCT GPA) DECIMAL | "
                + "RESULT: "
                + "<'Avi' 7.72> "
                + "<'Lemar' 7.72> "
                + "<'Shmuel' 7.72> "
                + "<'John' 7.72> |", result.toString());
    }

    @Test
    public void selectSumDistinctColumnMasterTestDoubleDistinct()
    {
        DataBase testBase = this.createFilledTestBase();
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Shmuel','Marcus',3.92, 800137780, 0292547569);");
        testBase.execute("INSERT INTO YCStudent (FirstName, LastName, GPA, BannerID, SSNum) VALUES ('Avi','Shalom',3.92, 800227780, 0322547569);");
        this.preExecutePrint(testBase);

        String query = "SELECT DISTINCT FirstName, SUM(DISTINCT GPA), MAX(SSNum) FROM YCStudent WHERE GPA > 3.77;";
        ResultSet result = testBase.execute(query);
        this.postExecutePrint(testBase, result, query, "split");

        Assert.assertEquals("COLUMNS: "
                + "FirstName VARCHAR, SUM(DISTINCT GPA) DECIMAL, MAX(SSNum) INT | "
                + "RESULT: "
                + "<'Avi' 7.72 1234567890> "
                + "<'Lemar' 7.72 1234567890> "
                + "<'Shmuel' 7.72 1234567890> |", result.toString());
    }

    @Test
    public void multipleTables()
    {
        DataBase testBase = new DataBase();
        testBase.execute("CREATE TABLE Students"
                + "("
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " BannerID int,"
                + " PRIMARY KEY (BannerID)"
                + ");");
        testBase.execute("CREATE TABLE Teachers"
                + "("
                + " FirstName varchar(255),"
                + " LastName varchar(255) NOT NULL,"
                + " TeacherID int,"
                + " PRIMARY KEY (TeacherID)"
                + ");");
        this.preExecutePrint(testBase);

        String query1 = "INSERT INTO Students (FirstName, LastName, BannerID) VALUES ('Ploni','Almoni', 800012345);";
        ResultSet result1 = testBase.execute(query1);
        this.postExecutePrint(testBase, result1, query1, "noSplit");

        String query2 = "INSERT INTO Teachers (FirstName, LastName, TeacherID) VALUES ('Ploni','Almoni', 900023456);";
        ResultSet result2 = testBase.execute(query2);
        this.postExecutePrint(testBase, result2, query2, "split");
    }
}


/*
        Assert.assertEquals("COLUMNS: "
                + "CurrentStudent, GPA, FirstName, BannerID, LastName, SSNum "
                + "| ROWS: "
                + "<true 2.3 'Ploni' 800012345 'Almoni' null> "
                + "<true 3.92 'Avi' 800373678 'Katz' 1234567890> "
                + "<true 3.76 'Eli' 800456789 'Weiss' 293847563> "
                + "<true 2.76 'Eli' 800456780 'Weiss' 293847566> "
                + "<true 2.91 'Jodan' 800356780 'Lewis' 293847567> "
                + "<true 3.8 'Lemar' 800256780 'Jurr' 293847568> "
                + "<true 3.2 'Julio' 800156780 'Gonzalez' 293847569> "
                + "<true 2.6 'Mark' 800056780 'Cohen' 293847570>", testBase.toString());
*/
package com.epam.files;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class DbUnitTest {

    private static final String DB_URL = "jdbc:h2:mem:dbunit_test;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    private IDatabaseTester databaseTester;

    @BeforeAll
    static void initSchema() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS PLAYERS (
                    id INT PRIMARY KEY,
                    name VARCHAR(100),
                    score INT
                );
            """);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        databaseTester = new JdbcDatabaseTester("org.h2.Driver", DB_URL, USER, PASS, "PUBLIC");
        databaseTester.getConnection().getConfig().setProperty(
                "http://www.dbunit.org/properties/datatypeFactory",
                new H2DataTypeFactory()
        );

        try (InputStream in = getClass().getResourceAsStream("/dataset.xml")) {
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(in);
            databaseTester.setDataSet(dataSet);
            databaseTester.onSetup();
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        databaseTester.onTearDown();
    }

    @Test
    void test1_InitialDatasetLoaded() throws Exception {
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("PLAYERS");
        Assertions.assertEquals(3, actualTable.getRowCount());
        Assertions.assertEquals("Alice", actualTable.getValue(0, "name"));
    }

    @Test
    void test2_InsertNewPlayer() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO PLAYERS VALUES (4, 'David', 400)");
        }

        ITable table = databaseTester.getConnection().createDataSet().getTable("PLAYERS");
        Assertions.assertEquals(4, table.getRowCount());
    }

    @Test
    void test3_UpdatePlayerScore() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE PLAYERS SET score = 999 WHERE id = 1");
        }

        ITable table = databaseTester.getConnection().createQueryTable("PLAYER_1", "SELECT score FROM PLAYERS WHERE id = 1");
        Assertions.assertEquals(999, table.getValue(0, "score"));
    }

    @Test
    void test4_DeletePlayer() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM PLAYERS WHERE id = 2");
        }

        ITable table = databaseTester.getConnection().createDataSet().getTable("PLAYERS");
        Assertions.assertEquals(2, table.getRowCount());
    }

    @Test
    void test5_VerifyScoreAggregation() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT SUM(score) FROM PLAYERS");
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals(600, rs.getInt(1));
        }
    }
}

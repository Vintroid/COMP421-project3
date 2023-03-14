import java.sql.* ;
import java.util.Calendar;
import java.util.Scanner;

class soccer {


    public  static void listInfo(){

    }
    public static  void initPlayerInfo(){

    }
    public static void main ( String [ ] args ) throws SQLException
    {
        // Unique table names.  Either the user supplies a unique identifier as a command line argument, or the program makes one up.
        String tableName = "";
        int sqlCode=0;      // Variable to hold SQLCODE
        String sqlState="00000";  // Variable to hold SQLSTATE

        if ( args.length > 0 )
            tableName += args [ 0 ] ;
        else
            tableName += "exampletbl";

        // Register the driver.  You must register the driver before you can use it.
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        // This is the url you must use for DB2.
        //Note: This url may not valid now ! Check for the correct year and semester and server name.
        String url = "jdbc:db2://winter2023-comp421.cs.mcgill.ca:50000/cs421";

        //REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = "cs412g126";
        String your_password = "Comp421group126";
        //AS AN ALTERNATIVE, you can just set your password in the shell environment in the Unix (as shown below) and read it from there.
        //$  export SOCSPASSWD=yoursocspasswd
        if(your_userid == null && (your_userid = System.getenv("SOCSUSER")) == null)
        {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        if(your_password == null && (your_password = System.getenv("SOCSPASSWD")) == null)
        {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement ( ) ;
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running){

            System.out.print("Soccer Main Menu\n    1. List information of matches of a country\n    2. Insert initial player information for a match\n     3. For you to design\n     4. Exit application\n Please Enter Your Option: ");
            int c= scanner.nextInt();
            switch (c){
                case 1:{
                    while (true){
                        //list info
                        System.out.println("Please enter a country name: ");
                        String country = scanner.nextLine();
                        try
                        {
                            String querySQL = "SELECT team_name1,team_name2,date_and_time, round from matches"  + " WHERE NAME ="+ country;
                            System.out.println (querySQL) ;
                            java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;

                            while ( rs.next ( ) )
                            {
                                int id = rs.getInt ( 1 ) ;
                                String name = rs.getString (2);
                                System.out.println ("id:  " + id);
                                System.out.println ("name:  " + name);
                            }
                        }
                        catch (SQLException e)
                        {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE
                            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                            System.out.println(e);
                        }
                        System.out.println("Enter [A] to find matches of another country, [P] to go to the previous menu:");
                        if(scanner.nextLine()=="P"){
                            break;
                        }
                    }

                }
                case 2:{
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    
                }
                case 3:{}
                case 4:{}
                    running =false;
            }
        }

//        // Creating a table
//        try
//        {
//            String createSQL = "CREATE TABLE " + tableName + " (id INTEGER, name VARCHAR (25)) ";
//            System.out.println (createSQL ) ;
//            statement.executeUpdate (createSQL ) ;
//            System.out.println ("DONE");
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        // Inserting Data into the table
//        try
//        {
//            String insertSQL = "INSERT INTO " + tableName + " VALUES ( 1 , \'Vicki\' ) " ;
//            System.out.println ( insertSQL ) ;
//            statement.executeUpdate ( insertSQL ) ;
//            System.out.println ( "DONE" ) ;
//
//            insertSQL = "INSERT INTO " + tableName + " VALUES ( 2 , \'Vera\' ) " ;
//            System.out.println ( insertSQL ) ;
//            statement.executeUpdate ( insertSQL ) ;
//            System.out.println ( "DONE" ) ;
//            insertSQL = "INSERT INTO " + tableName + " VALUES ( 3 , \'Franca\' ) " ;
//            System.out.println ( insertSQL ) ;
//            statement.executeUpdate ( insertSQL ) ;
//            System.out.println ( "DONE" ) ;
//
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        // Querying a table
//        try
//        {
//            String querySQL = "SELECT id, name from " + tableName + " WHERE NAME = \'Vicki\'";
//            System.out.println (querySQL) ;
//            java.sql.ResultSet rs = statement.executeQuery ( querySQL ) ;
//
//            while ( rs.next ( ) )
//            {
//                int id = rs.getInt ( 1 ) ;
//                String name = rs.getString (2);
//                System.out.println ("id:  " + id);
//                System.out.println ("name:  " + name);
//            }
//            System.out.println ("DONE");
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }
//
//        //Updating a table
//        try
//        {
//            String updateSQL = "UPDATE " + tableName + " SET NAME = \'Mimi\' WHERE id = 3";
//            System.out.println(updateSQL);
//            statement.executeUpdate(updateSQL);
//            System.out.println("DONE");
//
//            // Dropping a table
//            String dropSQL = "DROP TABLE " + tableName;
//            System.out.println ( dropSQL ) ;
//            statement.executeUpdate ( dropSQL ) ;
//            System.out.println ("DONE");
//        }
//        catch (SQLException e)
//        {
//            sqlCode = e.getErrorCode(); // Get SQLCODE
//            sqlState = e.getSQLState(); // Get SQLSTATE
//
//            // Your code to handle errors comes here;
//            // something more meaningful than a print would be good
//            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
//            System.out.println(e);
//        }

        // Finally but importantly close the statement and connection
        statement.close ( ) ;
        con.close ( ) ;
    }
}

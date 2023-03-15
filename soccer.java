import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.StyledEditorKit;

import java.util.ArrayList;

class soccer {

    public static void listInfo() {

    }

    public static void initPlayerInfo() {

    }

    public static void main(String[] args) throws SQLException {
        // Unique table names. Either the user supplies a unique identifier as a command
        // line argument, or the program makes one up.
        String tableName = "";
        int sqlCode = 0; // Variable to hold SQLCODE
        String sqlState = "00000"; // Variable to hold SQLSTATE

        if (args.length > 0)
            tableName += args[0];
        else
            tableName += "exampletbl";

        // Register the driver. You must register the driver before you can use it.
        try {
            DriverManager.registerDriver(new com.ibm.db2.jcc.DB2Driver());
        } catch (Exception cnfe) {
            System.out.println("Class not found");
        }

        // This is the url you must use for DB2.
        // Note: This url may not valid now ! Check for the correct year and semester
        // and server name.
        String url = "jdbc:db2://winter2023-comp421.cs.mcgill.ca:50000/cs421";

        // REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = "cs421g126";
        String your_password = "Comp421group126";
        // AS AN ALTERNATIVE, you can just set your password in the shell environment in
        // the Unix (as shown below) and read it from there.
        // $ export SOCSPASSWD=yoursocspasswd
        if (your_userid == null && (your_userid = System.getenv("SOCSUSER")) == null) {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        if (your_password == null && (your_password = System.getenv("SOCSPASSWD")) == null) {
            System.err.println("Error!! do not have a password to connect to the database!");
            System.exit(1);
        }
        Connection con = DriverManager.getConnection(url, your_userid, your_password);
        Statement statement = con.createStatement();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean chance = true;
        int freeticket =0;
        while (running) {

            System.out.print(
                    "Soccer Main Menu\n    1. List information of matches of a country\n    2. Insert initial player information for a match\n    3. Get a FREE TICKET\n    4. Exit application\n Please Enter Your Option: ");
            int c = scanner.nextInt();
            scanner.nextLine();
            if (c == 1) {
                menuLoop: while (true) {
                    // list info
                    System.out.println("Please enter a country name: ");

                    String country = scanner.nextLine();

                    try {
                        String querySQL = "select team_name1,team_name2,date_and_time,round,team1_goals,team2_goals,  coalesce(t1.seats_sold,0) as seats_sold from matches left join (SELECT mid ,COUNT(*) AS seats_sold FROM tickets  group by mid)t1 on t1.mid = matches.mid"
                                + " WHERE team_name1 = '" + country + "' or team_name2 = '" + country + "'";

                        java.sql.ResultSet rs = statement.executeQuery(querySQL);

                        while (rs.next()) {
                            String team1 = rs.getString(1);
                            String team2 = rs.getString(2);
                            java.sql.Date date = rs.getDate(3);
                            String round = rs.getString(4);
                            String goal1 = rs.getString(5);
                            String goal2 = rs.getString(6);
                            String seats = rs.getString(7);
                            System.out.print(team1 + "   " + team2 + "   " + date + "   " + round + "   " + goal1
                                    + "   " + goal2 + "   " + seats + "\n");
                        }
                    } catch (SQLException e) {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE
                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                    }
                    System.out.println(
                            "Enter [A] to find matches of another country, [P] to go to the previous menu:");
                    String options = scanner.nextLine();
                    if (options.equals("P")) {
                        break menuLoop;
                    }
                }

            } else if (c == 2) {
                // get the date of 3 days after today
                java.sql.Date afterdate = new java.sql.Date(
                        Calendar.getInstance().getTime().getTime() + 24 * 60 * 60 * 1000 * 3);

                System.out.println(afterdate);
                try {
                    String querySQL = "select mid,team_name1,team_name2,date_and_time,round from matches where date_and_time <= '"
                            + afterdate + "'";

                    java.sql.ResultSet rs = statement.executeQuery(querySQL);
                    System.out.println("Matches: ");
                    while (rs.next()) {
                        int mid = rs.getInt(1);
                        String team1 = rs.getString(2);
                        String team2 = rs.getString(3);
                        java.sql.Date date = rs.getDate(4);
                        String round = rs.getString(5);
                        System.out.print(mid);
                        System.out.print(" " + team1 + " " + team2 + " " + date + " " + round + "\n");
                    }

                } catch (SQLException e) {
                    sqlCode = e.getErrorCode(); // Get SQLCODE
                    sqlState = e.getSQLState(); // Get SQLSTATE

                    System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                    System.out.println(e);
                }
                System.out.println(
                        "Please enter the match identifier and the country name OR enter [P] to return to the previous menu");
                String str = scanner.nextLine();
                if (!str.equals("P")) {
                    String[] strs = str.split(" ");
                    if (strs.length <= 1 || strs.length > 2) {
                        System.out.println("wrong input format");
                    } else {
                        String id = strs[0];
                        String enteredCountry = strs[1];
                        // QUERY for given id and country;
                        int max = 3;
                        int numofplayer = 0;
                        try {
                            String querySQL = "SELECT M.mname,shirt_number,PL.position,minute_in,minute_out,yellow_cards,red_cards FROM members M, players P, plays PL WHERE P.mname = PL.mname AND  P.mname = M.mname AND PL.mid = "
                                    + id + " AND M.country = '" + enteredCountry + "';";
                            java.sql.ResultSet rs = statement.executeQuery(querySQL);

                            while (rs.next()) {
                                String mname = rs.getString(1);
                                String sn = rs.getString(2);
                                String position = rs.getString(3);
                                String minin = rs.getString(4);
                                String minout = rs.getString(5);
                                String yellow_cards = rs.getString(6);
                                String red_cards = rs.getString(7);
                                System.out.println(mname + " " + sn + " " + position + " from minute " + minin
                                        + " to minute " + minout + " yellow: " + yellow_cards + " red: " + red_cards);
                                numofplayer++;
                            }
                        } catch (SQLException e) {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                            System.out.println(e);
                        }
                        System.out.println("Possible players not yet selected:");
                        // Query for remaining players in the team;
                        Map<Integer, ArrayList<String>> map = new HashMap<>();
                        try {
                            String querySQL = "SELECT M.mname,shirt_number,position FROM members M, players P WHERE M.country = '"
                                    + enteredCountry
                                    + "' AND M.mname = P.mname EXCEPT SELECT M.mname,shirt_number,P.position FROM members M, players P, plays PL WHERE M.country = '"
                                    + enteredCountry + "' AND M.mname = P.mname AND P.mname = PL.mname AND PL.mid ="
                                    + id;
                            java.sql.ResultSet rs = statement.executeQuery(querySQL);
                            int counter = 1;
                            while (rs.next()) {
                                String mname = rs.getString(1);
                                String sn = rs.getString(2);
                                String position = rs.getString(3);
                                System.out.println(counter + ". " + mname + " " + sn + " " + position);
                                ArrayList<String> ary = new ArrayList<>();
                                ary.add(mname);
                                ary.add(sn);
                                ary.add(position);
                                map.put(counter, ary);
                                counter++;
                            }
                        } catch (SQLException e) {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                            System.out.println(e);
                        }
                        boolean add = true;
                        while (add) {
                            if (numofplayer <= 3) {
                                System.out.println(
                                        "Enter the number of the player you want to insert or [P]to go to the previous menu.");
                                // insert data given list index

                                String st = scanner.nextLine();
                                if (!st.equals("P")) {
                                    int i = Integer.parseInt(st);
                                    ArrayList<String> tmp = map.get(i);

                                    try {
                                        // "INSERT INTO plays VALUES ('name',"+id+",0,NULL,'id',0,0)";
                                        String insertSQL = "INSERT INTO plays VALUES ('" + tmp.get(0) + "'," + id
                                                + ",0,NULL,'" + tmp.get(2) + "',0,0)";
                                        System.out.println(
                                                tmp.get(0) + " will now play as " + tmp.get(2) + " in match " + id);
                                        statement.executeUpdate(insertSQL);
                                        numofplayer++;
                                    } catch (SQLException e) {
                                        sqlCode = e.getErrorCode(); // Get SQLCODE
                                        sqlState = e.getSQLState(); // Get SQLSTATE

                                        // Your code to handle errors comes here;
                                        // something more meaningful than a print would be good
                                        System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                                        System.out.println(e);
                                    }
                                } else {
                                    add = false;
                                }

                            } else {
                                add = false;
                                System.out.println(
                                        "You have reached the team limit, cannot add more players, returning to main menu");
                            }

                        }

                    }

                }
            } else if (c == 3) {
                
                
                if (chance && freeticket ==0) {
                    System.out.println("\n!!!!!!!!It is your lucky day!!!!!!!! You just got a free ticket\n ");
                    java.sql.Date afterdate = new java.sql.Date(
                            Calendar.getInstance().getTime().getTime());
                            Map<Integer, ArrayList<String>> amap = new HashMap<>();
                    try {
                        String querySQL = "select mid,team_name1,team_name2,date_and_time,round from matches where date_and_time >= '"
                                + afterdate + "'";

                        java.sql.ResultSet rs = statement.executeQuery(querySQL);
                        System.out.println("Matches: ");
                        int counter = 1;
                        
                        while (rs.next()) {
                            String mid = rs.getString(1);
                            String team1 = rs.getString(2);
                            String team2 = rs.getString(3);
                            java.sql.Date date = rs.getDate(4);
                            String round = rs.getString(5);
                            System.out.print(counter + ". " + team1 + " " + team2 + " " + date + " " + round + "\n");
                            ArrayList<String> ary = new ArrayList<>();
                            ary.add(mid);
                            ary.add(team1);
                            ary.add(team2);
                            ary.add(date.toString());
                            amap.put(counter, ary);
                            counter++;
                        }

                    } catch (SQLException e) {
                        sqlCode = e.getErrorCode(); // Get SQLCODE
                        sqlState = e.getSQLState(); // Get SQLSTATE

                        System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
                        System.out.println(e);
                    }
                    System.out.println("\n\nHere are all the matches avalible for selection. \n............................");
                    System.out.println(
                            "Please select which matched you would like to watch or press [P] to give up this free ticket");
                    String str = scanner.nextLine();
                    if (!str.equals("P")) {
                        int a = Integer.parseInt(str);
                        ArrayList<String> tmp = amap.get(a);
                        int maxseatsid =0;
                        int sid =0;
                        int maxtid=0;
                        //get max seat_id
                        try {
                            String querySQL = "select max(seat_id) from seats";
                            java.sql.ResultSet rs = statement.executeQuery(querySQL);
                            if(rs.next()){
                                maxseatsid = rs.getInt(1)+1;
                                //System.out.println("seat id done");
                            }
                            
                        } catch (SQLException e) {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                            System.out.println(e);
                        }
                        // get sid
                        try {
                            String querySQL = "select sid from hosts where mid = "+tmp.get(0);
                            java.sql.ResultSet rs = statement.executeQuery(querySQL);
                            if(rs.next()){
                                sid = rs.getInt(1);
                                //System.out.println("sid done");
                            }
                           
                            
                        } catch (SQLException e) {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                            System.out.println(e);
                        }
                        String location = null; String sname = null;
                        //get stadiums name and location
                        try {
                            String querySQL = "select location,sname from stadiums where sid = "+sid;
                            java.sql.ResultSet rs = statement.executeQuery(querySQL);
                            if(rs.next()){

                            
                            location = rs.getString(1);
                            sname = rs.getString(2);
                            //System.out.println("statdiums done");
                            } 
                        } catch (SQLException e) {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                            System.out.println(e);
                        }
                        //get max tid
                        try {
                            String querySQL = "select max(tid) from tickets";
                            java.sql.ResultSet rs = statement.executeQuery(querySQL);
                            if(rs.next()){
                                maxtid = rs.getInt(1)+1;
                                //System.out.println("tid done");
                            }
                            
                        } catch (SQLException e) {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                            System.out.println(e);
                        }

                        //insert new tickets
                        try {
                            String ins = " insert into seats VALUES("+maxseatsid+","+sid+",'"+sname+"',"+"'occupied'"+")";
                            statement.executeUpdate(ins);
                            String insertSQL = "INSERT INTO tickets VALUES ("+maxtid+","+maxseatsid+","+sid+","+tmp.get(0)+",0)";
                            statement.executeUpdate(insertSQL);
                            System.out.println("Match: "+tmp.get(1)+" against "+tmp.get(2) +" in Statium " + sname +"located in "+location+"on day " +tmp.get(3));
                            System.out.println(" You ticket number: "+maxtid +" seat_number: " + maxseatsid);
                            System.out.println("Have a nice day, redirecting to Main Menu\n..............................\n\n=============");
                        } catch (SQLException e) {
                            sqlCode = e.getErrorCode(); // Get SQLCODE
                            sqlState = e.getSQLState(); // Get SQLSTATE

                            // Your code to handle errors comes here;
                            // something more meaningful than a print would be good
                            System.out.println("Code: " + sqlCode + " sqlState: " + sqlState);
                            System.out.println(e);
                        }
                    } else
                        chance = false;
                } else if(chance && freeticket>0)
                    System.out.println("No more free tikets!");
                else 
                    System.out.println("You had your chance! Now you will not have any free tickets!");

            } else if (c == 4)
                running = false;
        }
        statement.close();
        con.close();
    }
}

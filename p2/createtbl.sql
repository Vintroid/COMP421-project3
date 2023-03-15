-- Include your create table DDL statements in this file.

-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO cs421;

-- Remember to put the create table ddls for the tables with foreign key references
--    ONLY AFTER the parent tables has already been created.

-- This is only an example of how you add create table ddls to this file.
--   You may remove it.

create table Clients (cid integer not null, client_name varchar(30) not null, primary key(cid));
create table Stadiums (sid integer not null, location varchar(30) , sname varchar(30) not null, capacity integer,primary key(sid));
create table Seats(seat_id integer not null ,sid integer not null, seat_location varchar(30) not null,seat_status varchar(30)not null, primary key(seat_id,sid),foreign key (sid) references Stadiums);
create table Teams(country varchar(30) not null, group varchar(30) not null,primary key(country));
create table Associations(aname varchar(30)not null, URL varchar(50),primary key(aname));
create table Supervises(aname varchar(30)not null,country varchar(30),primary key(aname),foreign key (aname) references Associations,foreign key (country) references Teams);
create table Members(mname varchar(30) not null,country varchar(30) not null,DOB date, primary key(mname),foreign key(country) references Teams);
create table Coaches(mname varchar(30) not null, role varchar(30), primary key(mname),foreign key(mname) references Members);
create table Players(mname varchar(30) not null, position varchar(30),shirt_number integer not null,primary key(mname),foreign key(mname) references Members);
create table Belongs(mname varchar(30) not null,country varchar(30) not null,primary key(mname,country),foreign key(mname) references Members,foreign key (country) references Teams);
create table Matches(mid integer not null, round varchar(30),date_and_time datetime,team_name1 varchar(30),team_name2 varchar(30),lenghth integer,primary key(mid));
create table Referees(rname varchar(30) not null, years_of_experience integer,country varchar(30),primary key(rname));
create table Oversees (rname varchar(30) not null ,mid integer not null,role varchar(30),primary key(rname,mid),foreign key(rname) references Referees,foreign key(mid) references Matches);
create table Hosts(sid integer not null,mid integer not null,primary key(sid, mid),foreign key(sid) references Stadiums,foreign key(mid) references Matches);
create table Tickets(tid integer not null, seat_id integer not null,sid integer not null,mid integer, price integer not null, primary key (tid),foreign key(seat_id,sid) references Seats,foreign key(mid) references Matches);
create table Bookings(tid integer not null,cid integer not null,booking_date date, primary key(tid,cid),foreign key(cid) references Clients,foreign key (tid) references Tickets);
create table Plays(mname varchar(30) not null,mid integer not null,minute_in integer not null,minute_out integer not null,position varchar(30) not null ,yellow_cards int,red_cards int,primary key(mname,mid),foreign key(mid) references Matches,foreign key(mname) references Players);
create table Goals(occurence varchar(30) not null,mid integer not null ,time integer,goal_type varchar(30) not null,country varchar(30) not null,primary key(occurence,mid),foreign key(mid) references Matches);

create table Scores(mname varchar(30) not null,occurence varchar(30) not null,mid integer not null,foreign key(mname) references Members,foreign key(occurence,mid) references Goals);
create table Happens(occurence varchar(30) not null , mid integer not null, country varchar(30) not null,foreign key(occurence,mid) references Goals,foreign key(country) references Teams);




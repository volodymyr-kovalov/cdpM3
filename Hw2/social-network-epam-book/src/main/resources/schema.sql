CREATE TABLE user(
  Username VARCHAR(50) NOT NULL,
  Name VARCHAR(50),
  Date_Of_Birth DATE,
  Password  VARCHAR(100) NOT NULL,
  UNIQUE (Username)
);
CREATE TABLE timeline(
  Id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  Owner VARCHAR(50) NOT NULL,
  Author VARCHAR(50) NOT NULL,
  Note_Text VARCHAR(50) NOT NULL
);
CREATE TABLE user_friends(
  User_Username VARCHAR(50) NOT NULL,
  Friends_Username VARCHAR(50) NOT NULL
);
CREATE TABLE user_role(
  User_Username VARCHAR(50) NOT NULL,
  Role_Role_Id int NOT NULL
);
CREATE TABLE role(
  Role_Id int NOT NULL,
  Role_Name VARCHAR(50) NOT NULL
);

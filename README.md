# CS180_Group43

*Report submitted by Noah Mundy, Code submitted by Noah Mundy*


Welcome to the Java Learning Management System Discussion board as presented by CS180 Group 43.

This program implements the basic framework of a discussion board using network I/O.

Running:
- 
To Compile and run this project, you must first run the "ServerMain" class to boot up the server. With the server up, you may
run the "ClientMain" class to connect to the server. Any number of ClientMain classes may be run simultaneously to connect to 
the server. For convenience, a secondary class "ClientMain2" has been provided to test synchronized functionality on the same 
machine. This class is simply a clone of ClientMain.

Core functionality:

- Login page allows teachers/students to login/create accounts.
- Teachers are able to create courses and forums for said courses. Students can then navigate these courses and reply to specific forums.
- Data for all interactions on the board persists across program instances through the use of .txt "Databases" and basic File I/O.

With these basic functionalities implemented, more fine details are added:
- Teachers can upload files with a discussion topic to create a new forum / Students can upload files with their replies
- Replies to forums can be voted on / Graded by teachers.
- Students can view graded replies
- Teachers can edit/delete Student responses

Technical Description
=
The program as a whole can be broken down into 4 distinct functional elements: Reference classes, User classes, Data files, and Menu logic,  

The reference classes are Course.java, Forum.java, and Reply.java. Each of these classes formats relevant data into variables easily accessible
from anywhere in the program. Also, they provide methods that process that data in elementary ways. For example, a Reply object will store the
content, votes, and comments for a specific reply. Additionally, it will also provide methods that adds a comment or a vote. In this way, the 
class retains all basic storing and mutation functionality for a basic section of the program.

The Data files are relatively simple: all they do is store the data contained within the reference classes in a manner that is easily
read in by file readers/writers. Thus, they add data persistence functionality as the Data files are not overwritten when the program is reset.

The User classes are used to differentiate Students and Teachers and allot permissions accordingly. They are each sub-classes of a "Person" class,
and thus they contain mostly identical data. Permissions in the main menu logic are often processed according to the class type of a person object.

Finally, the Menu logic is the main logical sequence of the entire program. By calling upon the elementary data of the reference classes and the 
specific permissions of the user class, the menu is generated according to user input at all points in the program. 


Individual Class Description
= 
ServerMain
-
Basic logic that begins the server. Receives clients and assigns a thread to each.

ServerThread
-
Server class that individually handles a client. Undertakes read and write requests to transfer to client. 

ClientConnection
-
Data class to store and distribute information about a socket connection.

ClientMain
- 
Starts off the main logic of the program, including logging in and diverting to menu flow.

Settings
-
Handles all logic pertaining to a user changing its account details: Username/password changes, account deletion is handled here.

Menu
- 
Controls Vast majority of logic flow in the program. All user interaction from login to initial menus is handled here.

CourseMenu
-
Controls logic flow regarding the creation of courses (for Teachers) and the viewing of courses (for all users).

ForumMenu
-
Controls logic flow regarding forums, replies, comments, grading, up to exiting the program.

Person
- 
Superclass for users. Has username/password and their respective setters/getters

Student
- 
Subclass of person. Indicates lower level of permissions with the Learning managements system

Teacher
- 
Subclass of person. Indicates Higher level of permissions with the Learning management system.

Course
-
Highest level reference class. Contains data on the course name and all associated forums. Additionally, contains 
methods for editing forums. All reference classes have their own Readers/Writers for data persistence

Forum
-
Mid level reference class. Contains data on forum name and an array list of all forum replies. Also contains
data on associated student scores. All reference classes have their own Readers/Writers for data persistence

Reply
-
Lowest level reference class. Contains data on everything related to a reply: content, poster, number of votes, comments, etc.
Contains methods that do basic operations with these elements. All reference classes have their own Readers/Writers for data persistence




File Formatting
=

TO UPLOAD FILES FOR FORUMS/REPLIES:
-
Upload a .txt file with the content of the desired area. Files may not contain the characters ':' or "~"

login.txt
- 
- This file saves all the user login information, including Username, Password, and Type
- The format is {Per Line}-- [Username]:[Password]:[User type (Denoted by S or T)]

Courses.txt
- 
- This simple file lists all available courses
- The format is {Per Line}-- [Course Name]

[Course]_forums.txt
- 
- This file is a generalized format, meaning there is one implemented for EVERY course generated
- This file lists all relevant forum data for a specific course
- The format is {Per Line}--  [Forum Name]~[Timestamp]

[Course]_[Forum]_r.txt
- 
- this file is a generalized format, meaning there is one implemented for EVERY forum generated
- This file stores all data relevant to a reply object.
- The format is:
  - Line 1: [Username]~[Timestamp]~[Content]~[Votes]
  - Line 2: [Usernames that have voted] {Concatenated with the '~' Delimiter}
  - Line 3: [Comments] {Concatenated with the '~' Delimiter}
- This process is repeated for every reply in the forum

[Course]_[Forum]_scores.txt
- 
- This file is a generalized format, meaning there is one implemented for EVERY forum generated.
- This file will keep track of all students that have received a grade for the forum.
- The format is {Per Line}-- [Username]:[Score]

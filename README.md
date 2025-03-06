
# Application Manager Project
Overview
The Application Manager is a Java-based project designed to manage application processes, including tracking their states, managing notes, and performing state transitions. This system leverages design patterns such as State Pattern and Singleton Pattern to handle application states effectively. The application also provides file I/O capabilities for loading and saving applications, ensuring data persistence.

Features
State Management:

Supports application states: Review, Interview, Waitlist, RefCheck, Offer, and Closed.
Transitions between states based on user commands such as ACCEPT, REJECT, STANDBY, and REOPEN.
Uses the State Pattern for state-specific logic.
File I/O:

Load applications from files using AppReader.
Save applications to files using AppWriter.
Singleton Pattern:

AppManager ensures that only one instance of the application manager is created during execution.
Command Processing:

Commands encapsulate user actions to initiate state transitions.
Application Types:
New, Old, and Hired applications are managed based on business rules.


How to Run the Project
Clone the repository:
git clone <repository-url>

Import the project into your favorite Java IDE (e.g., Eclipse).

Compile and run the project using the AppManagerGUI (if GUI is provided) or through the CLI methods via the AppManager class.


Usage
Creating Applications:
Add applications via AppList with a type, summary, and initial note.

Executing Commands:
Use the Command class to encapsulate user actions that trigger state transitions.

Reading and Writing Applications to Files:
Use AppReader to load applications and AppWriter to save them.

State Transitions:
Applications move through predefined states based on user actions. Invalid transitions throw exceptions.
Testing

JUnit Tests:
The project uses JUnit for testing. Test files are stored in the test-files/ directory. 

Design Patterns Used
State Pattern:
Each application state (e.g., ReviewState, OfferState) has its own class implementing AppState. The Application class delegates behavior to these state classes.

Singleton Pattern:
AppManager ensures only one instance is created during execution.

Error Handling
IllegalArgumentException is thrown for:
Invalid parameters during application creation.
Invalid state transitions.
File I/O errors.

UnsupportedOperationException is thrown for:
Commands that cannot be executed in the current state.

Test Files
Test File Directory: /test-files/
Usage: These files are essential for testing the correctness of the application:
app1.txt – Valid application file.
app2.txt – Duplicate IDs and unsorted applications.
exp_app_review.txt – Expected output for review state.
Future Improvements
Additional Application Types:
Add more application types as required.

Author
Matthew Stanaland (he/him)

Acknowledgments
Instructor and Teaching Assistants for providing guidance and certain code.
JUnit for unit testing framework.
Zybook for reference materials on design patterns and state management.
This README provides a comprehensive guide to using, testing, and expanding the Application Manager Project.







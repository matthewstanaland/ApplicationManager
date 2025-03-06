package edu.ncsu.csc216.app_manager.model.application;

import static org.junit.jupiter.api.Assertions.*;

import edu.ncsu.csc216.app_manager.model.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tests the WaitlistState class using various commands and file-based scenarios.
 */
public class WaitlistStateTest {

    private Application application;
    private WaitlistState waitlistState;

    private static final String WAITLIST = "test-files/exp_app_waitlist.txt";
    private static final String REVIEW = "test-files/exp_app_review.txt";
    private static final String INTERVIEW = "test-files/exp_app_interview.txt";

    @BeforeEach
    public void setUp() {
        application = new Application(1, "Waitlist", "New", 
                "Waiting for review", null, false, null, new ArrayList<>());
        waitlistState = new WaitlistState(application);
        application.setState(waitlistState);
    }

    /**
     * Tests reopening an application from the waitlist state.
     */
    @Test
    public void testValidReopenCommand() {
        Application app = new Application(3, Application.WAITLIST_NAME, "Old", 
                                          "Application summary", "reviewer", 
                                          false, null, new ArrayList<>());

        Command reopenCommand = new Command(Command.CommandValue.REOPEN, 
                                            "reviewer", null, "Reopening application");

        // Verify the application starts in the Waitlist state
        assertEquals(Application.WAITLIST_NAME, app.getStateName());

        // Execute the command to reopen the application
        app.update(reopenCommand);

        // Ensure the state transitions to Review
        assertEquals(Application.REVIEW_NAME, app.getStateName());
    }



    /**
     * Tests accepting an application from the waitlist state.
     */
    @Test
    public void testValidAcceptCommand() {
        Command acceptCommand = new Command(Command.CommandValue.ACCEPT, "reviewer", 
                Command.Resolution.INTCOMPLETED, "Accepted for interview");
        waitlistState.updateState(acceptCommand);

        assertEquals("Interview", application.getStateName());
        assertEquals("reviewer", application.getReviewer());
        assertTrue(application.isProcessed());
    }

    /**
     * Tests rejecting an application from the waitlist state.
     * This should throw an exception.
     */
    @Test
    public void testInvalidRejectCommand() {
        Application app = new Application(3, Application.WAITLIST_NAME, "Old", 
                                          "Application summary", "reviewer", 
                                          false, null, new ArrayList<>());

        Command invalidCommand = new Command(Command.CommandValue.REJECT, 
                                             null, Command.Resolution.INTCOMPLETED, 
                                             "Invalid reject command");

        // Assert that the invalid command throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> {
            app.update(invalidCommand);
        });
    }


    /**
     * Tests loading a valid application in the waitlist state from a file.
     */
    @Test
    public void testLoadValidApplicationFromFile() throws FileNotFoundException {
        Application appFromFile = loadApplicationFromFile(WAITLIST);

        assertEquals("Waitlist", appFromFile.getStateName());
        assertEquals("Application summary", appFromFile.getSummary());
    }

    /**
     * Tests adding a note with valid content.
     */
    @Test
    public void testAddValidNote() {
        application.addNote("This is a valid note.");
        assertTrue(application.getNotesString().contains("This is a valid note."));
    }

    /**
     * Tests adding a note with invalid (null) content.
     */
    @Test
    public void testAddInvalidNote() {
        assertThrows(IllegalArgumentException.class, () -> {
            application.addNote(null);
        });
    }

    /**
     * Tests transition to Interview state via a valid ACCEPT command.
     */
    @Test
    public void testTransitionToInterviewState() {
        Command acceptCommand = new Command(Command.CommandValue.ACCEPT, "reviewer", 
                Command.Resolution.INTCOMPLETED, "Accepted for interview");
        waitlistState.updateState(acceptCommand);

        assertEquals("Interview", application.getStateName());
    }

    /**
     * Utility method to load an application from a file.
     * 
     * @param fileName the file path.
     * @return the Application object created from the file.
     * @throws FileNotFoundException if the file is not found.
     */
    private Application loadApplicationFromFile(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        List<String> notes = new ArrayList<>();

        String[] header = scanner.nextLine().split(",");
        int id = Integer.parseInt(header[0].substring(1));
        String state = header[1];
        String type = header[2];
        String summary = header[3];
        String reviewer = header[4].equals("null") ? null : header[4];
        boolean processed = Boolean.parseBoolean(header[5]);
        String resolution = header.length > 6 ? header[6] : null;

        while (scanner.hasNextLine()) {
            notes.add(scanner.nextLine().substring(2)); // Skip the '- ' prefix
        }
        scanner.close();

        return new Application(id, state, type, summary, reviewer, processed, resolution, new ArrayList<>(notes));
    }
}

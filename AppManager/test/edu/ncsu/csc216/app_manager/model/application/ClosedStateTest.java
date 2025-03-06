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
 * Tests the ClosedState class using commands and file-based scenarios.
 */
public class ClosedStateTest {

    private Application application;
    private ClosedState closedState;

    private static final String CLOSED = "test-files/exp_app_closed.txt";
    private static final String REVIEW = "test-files/exp_app_review.txt";

    @BeforeEach
    public void setUp() {
        application = new Application(4, "Closed", "Old", 
                "Application summary", null, true, "OfferCompleted", new ArrayList<>());
        closedState = new ClosedState(application);
        application.setState(closedState);
    }

    /**
     * Tests reopening the application from Closed to Review state.
     */
    @Test
    public void testReopenApplication() {
        Command reopenCommand = new Command(Command.CommandValue.REOPEN, null, 
                null, "Reopened from closed");

        closedState.updateState(reopenCommand);

        assertEquals("Review", application.getStateName());
        assertEquals(Application.A_OLD, application.getAppType());
        assertTrue(application.getNotesString().contains("[Closed] Reopened from closed"));
    }

    /**
     * Tests an invalid command in the Closed state.
     */
    @Test
    public void testInvalidCommand() {
        Command invalidCommand = new Command(Command.CommandValue.ACCEPT, null, null, 
                "Invalid command in closed state");

        assertThrows(UnsupportedOperationException.class, () -> {
            closedState.updateState(invalidCommand);
        });
    }

    /**
     * Tests loading an application from the CLOSED state file.
     */
    @Test
    public void testLoadApplicationFromFile() throws FileNotFoundException {
        Application appFromFile = loadApplicationFromFile(CLOSED);

        assertEquals("Closed", appFromFile.getStateName());
        assertEquals("Old", appFromFile.getAppType());
        assertEquals("Application summary", appFromFile.getSummary());
    }

    /**
     * Tests reopening the application with an invalid resolution.
     */
    @Test
    public void testReopenWithInvalidResolution() {
        application.setResolution("InvalidResolution");

        Command reopenCommand = new Command(Command.CommandValue.REOPEN, null, 
                null, "Attempted reopen with invalid resolution");

        assertThrows(UnsupportedOperationException.class, () -> {
            closedState.updateState(reopenCommand);
        });
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

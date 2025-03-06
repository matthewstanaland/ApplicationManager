package edu.ncsu.csc216.app_manager.model.application;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Tests the Application class using the provided test files and various scenarios.
 */
public class ApplicationTest {

    private static final String VALID_FILE = "test-files/app1.txt";
    private static final String VALID_FILE_ID = "test-files/app2.txt";
    private static final String INVALID_STATE = "test-files/app3.txt";
    private static final String INVALID_TYPE = "test-files/app4.txt";
    private static final String NEGATIVE_ID = "test-files/app18.txt";
    private Application application;

    @BeforeEach
    public void setUp() {
        application = new Application(1, "Review", "New", "Test summary", null, false, null, new ArrayList<>());
    }

    /**
     * Tests creating an application with valid parameters.
     */
    @Test
    public void testValidApplicationCreation() {
        assertEquals(1, application.getAppId());
        assertEquals("Review", application.getStateName());
        assertEquals("New", application.getAppType());
        assertEquals("Test summary", application.getSummary());
    }

    /**
     * Tests creating an application with invalid parameters.
     */
    @Test
    public void testInvalidApplicationCreation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Application(-1, "Review", "New", "Test summary", null, false, null, new ArrayList<>());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Application(1, null, "New", "Test summary", null, false, null, new ArrayList<>());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Application(1, "Review", null, "Test summary", null, false, null, new ArrayList<>());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Application(1, "Review", "New", "", null, false, null, new ArrayList<>());
        });
    }

    /**
     * Tests state transitions using valid commands.
     */
    @Test
    public void testValidStateTransitions() {
        Command command = new Command(Command.CommandValue.ACCEPT, "reviewer", null, "Accepted");
        application.update(command);

        assertEquals("Interview", application.getStateName());
        assertEquals("reviewer", application.getReviewer());
        assertTrue(application.getNotesString().contains("[Interview] Accepted"));
    }

    /**
     * Tests state transitions using invalid commands.
     */
    @Test
    public void testInvalidStateTransitions() {
        Command invalidCommand = new Command(Command.CommandValue.REOPEN, null, null, "Invalid reopen");

        assertThrows(UnsupportedOperationException.class, () -> {
            application.update(invalidCommand);
        });
    }

    /**
     * Tests adding valid and invalid notes.
     */
    @Test
    public void testAddNote() {
        application.addNote("Valid note");
        assertTrue(application.getNotesString().contains("[Review] Valid note"));

        assertThrows(IllegalArgumentException.class, () -> {
            application.addNote(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            application.addNote("");
        });
    }

    /**
     * Tests loading an application from a file.
     */
    @Test
    public void testLoadApplicationFromFile() throws FileNotFoundException {
        Application appFromFile = loadApplicationFromFile(VALID_FILE);
        assertEquals(1, appFromFile.getAppId());
        assertEquals("Review", appFromFile.getStateName());
        assertEquals("New", appFromFile.getAppType());
    }

    /**
     * Tests an application with a negative ID.
     */
    @Test
    public void testNegativeIdApplication() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> {
            loadApplicationFromFile(NEGATIVE_ID);
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

    /**
     * Tests setting and getting the resolution.
     */
    @Test
    public void testSetAndGetResolution() {
        application.setResolution("InterviewCompleted");
        assertEquals("InterviewCompleted", application.getResolution());

        assertThrows(IllegalArgumentException.class, () -> {
            application.setResolution(null);
        });
    }
}

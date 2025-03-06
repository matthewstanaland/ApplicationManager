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
 * Tests the RefChkState class using various commands and file-based scenarios.
 */
public class RefChkStateTest {

    private Application application;
    private RefChkState refChkState;

    private static final String REF_CHECK = "test-files/exp_app_refcheck.txt";
    private static final String OFFER = "test-files/exp_app_offer.txt";
    private static final String REVIEW = "test-files/exp_app_review.txt";

    @BeforeEach
    public void setUp() {
        application = new Application(7, "RefCheck", "Old", 
                "Application summary", "reviewer", true, null, new ArrayList<>());
        refChkState = new RefChkState(application);
        application.setState(refChkState);
    }

    /**
     * Tests the transition from RefCheck to Offer state using a valid ACCEPT command.
     */
    @Test
    public void testValidAcceptCommand() {
        Command acceptCommand = new Command(Command.CommandValue.ACCEPT, "reviewer", 
                Command.Resolution.REFCHKCOMPLETED, "Accepted after reference check");
        refChkState.updateState(acceptCommand);

        assertEquals("Offer", application.getStateName());
        assertTrue(application.getNotesString().contains("[RefCheck] Accepted after reference check"));
    }

    /**
     * Tests the REJECT command from RefCheck state.
     */
    @Test
    public void testValidRejectCommand() {
        Command rejectCommand = new Command(Command.CommandValue.REJECT, null, 
                Command.Resolution.REFCHKCOMPLETED, "Rejected after reference check");
        refChkState.updateState(rejectCommand);

        assertEquals("Closed", application.getStateName());
    }

    /**
     * Tests an invalid STANDBY command in the RefCheck state.
     */
    @Test
    public void testInvalidStandbyCommand() {
        Command standbyCommand = new Command(Command.CommandValue.STANDBY, null, 
                null, "Standby not allowed in RefCheck");

        assertThrows(UnsupportedOperationException.class, () -> {
            refChkState.updateState(standbyCommand);
        });
    }

    /**
     * Tests loading a valid application in RefCheck state from a file.
     */
    @Test
    public void testLoadValidApplicationFromFile() throws FileNotFoundException {
        Application appFromFile = loadApplicationFromFile(REF_CHECK);

        assertEquals("RefCheck", appFromFile.getStateName());
        assertEquals("reviewer", appFromFile.getReviewer());
    }

    /**
     * Tests adding a valid note in RefCheck state.
     */
    @Test
    public void testAddValidNote() {
        application.addNote("Reference checked.");
        assertTrue(application.getNotesString().contains("Reference checked."));
    }

    /**
     * Tests adding an invalid (null) note.
     */
    @Test
    public void testAddInvalidNote() {
        assertThrows(IllegalArgumentException.class, () -> {
            application.addNote(null);
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

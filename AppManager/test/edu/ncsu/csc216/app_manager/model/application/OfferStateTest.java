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
 * Tests the OfferState class using various commands and file-based scenarios.
 */
public class OfferStateTest {

    private Application application;
    private OfferState offerState;

    private static final String OFFER = "test-files/exp_app_offer.txt";
    private static final String CLOSED = "test-files/exp_app_closed.txt";

    @BeforeEach
    public void setUp() {
        application = new Application(1, "Offer", "Old", 
                "Offer made", null, false, null, new ArrayList<>());
        offerState = new OfferState(application);
        application.setState(offerState);
    }

    /**
     * Tests accepting the offer, transitioning to the Closed state.
     */
    @Test
    public void testAcceptOffer() {
        Command acceptCommand = new Command(Command.CommandValue.ACCEPT, "reviewer", 
                Command.Resolution.OFFERCOMPLETED, "Offer accepted");
        offerState.updateState(acceptCommand);

        assertEquals("Closed", application.getStateName());
        assertEquals("Hired", application.getAppType());
        assertEquals("reviewer", application.getReviewer());
        assertTrue(application.isProcessed());
        assertTrue(application.getNotesString().contains("[Offer] Offer accepted"));
    }

    /**
     * Tests rejecting the offer, transitioning to the Closed state.
     */
    @Test
    public void testRejectOffer() {
        Command rejectCommand = new Command(Command.CommandValue.REJECT, null, 
                Command.Resolution.OFFERCOMPLETED, "Offer rejected");
        offerState.updateState(rejectCommand);

        assertEquals("Closed", application.getStateName());
        assertEquals("Old", application.getAppType());
        assertNull(application.getReviewer());
        assertTrue(application.getNotesString().contains("[Offer] Offer rejected"));
    }

    /**
     * Tests that using an invalid command in the Offer state throws an exception.
     */
    @Test
    public void testInvalidCommand() {
        Command invalidCommand = new Command(Command.CommandValue.STANDBY, null, null, 
                "Invalid command in Offer state");

        assertThrows(UnsupportedOperationException.class, () -> {
            offerState.updateState(invalidCommand);
        });
    }

    /**
     * Tests loading an application in the Offer state from a file.
     */
    @Test
    public void testLoadApplicationFromFile() throws FileNotFoundException {
        Application appFromFile = loadApplicationFromFile(OFFER);

        assertEquals("Offer", appFromFile.getStateName());
        assertEquals("Application summary", appFromFile.getSummary());
    }

    /**
     * Tests transitioning from Offer to Closed state via a valid accept command.
     */
    @Test
    public void testTransitionToClosedState() {
        Command acceptCommand = new Command(Command.CommandValue.ACCEPT, "reviewer", 
                Command.Resolution.OFFERCOMPLETED, "Offer accepted");
        offerState.updateState(acceptCommand);

        assertEquals("Closed", application.getStateName());
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

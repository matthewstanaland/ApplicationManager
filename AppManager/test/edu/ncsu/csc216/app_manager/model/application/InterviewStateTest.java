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
 * Tests the InterviewState class using commands and file-based scenarios.
 */
public class InterviewStateTest {

    private Application application;
    private InterviewState interviewState;

    private static final String INTERVIEW = "test-files/exp_app_interview.txt";
    private static final String WAITLIST = "test-files/exp_app_waitlist.txt";
    private static final String CLOSED = "test-files/exp_app_closed.txt";

    @BeforeEach
    public void setUp() {
        application = new Application(2, "Interview", "Old", 
                "In interview process", null, false, null, new ArrayList<>());
        interviewState = new InterviewState(application);
        application.setState(interviewState);
    }

    /**
     * Tests accepting the interview and transitioning to the Closed state.
     */
    @Test
    public void testAcceptInterview() {
        Command acceptCommand = new Command(Command.CommandValue.ACCEPT, "reviewer", 
                Command.Resolution.INTCOMPLETED, "Interview completed");
        interviewState.updateState(acceptCommand);

        assertEquals("Closed", application.getStateName());
        assertEquals("Old", application.getAppType());
        assertEquals("reviewer", application.getReviewer());
        assertTrue(application.isProcessed());
        assertTrue(application.getNotesString().contains("[Interview] Interview completed"));
    }

    /**
     * Tests rejecting the interview and transitioning to the Waitlist state.
     */
    @Test
    public void testRejectInterview() {
        Command rejectCommand = new Command(Command.CommandValue.REJECT, null, 
                Command.Resolution.INTCOMPLETED, "Interview rejected");
        interviewState.updateState(rejectCommand);

        assertEquals("Waitlist", application.getStateName());
        assertNull(application.getReviewer());
        assertFalse(application.isProcessed());
        assertTrue(application.getNotesString().contains("[Interview] Interview rejected"));
    }

    /**
     * Tests using an invalid command in the Interview state.
     */
    @Test
    public void testInvalidCommand() {
        Command invalidCommand = new Command(Command.CommandValue.STANDBY, null, null, 
                "Invalid command in Interview state");

        assertThrows(UnsupportedOperationException.class, () -> {
            interviewState.updateState(invalidCommand);
        });
    }

    /**
     * Tests loading an application from the Interview state file.
     */
    @Test
    public void testLoadApplicationFromFile() throws FileNotFoundException {
        Application appFromFile = loadApplicationFromFile(INTERVIEW);

        assertEquals("Interview", appFromFile.getStateName());
        assertEquals("Application summary", appFromFile.getSummary());
    }

    /**
     * Tests transition from Interview to Waitlist using a reject command.
     */
    @Test
    public void testTransitionToWaitlist() {
        Command rejectCommand = new Command(Command.CommandValue.REJECT, null, 
                Command.Resolution.INTCOMPLETED, "Rejected interview");
        interviewState.updateState(rejectCommand);

        assertEquals("Waitlist", application.getStateName());
    }

    /**
     * Tests transition from Interview to Closed state with an accept command.
     */
    @Test
    public void testTransitionToClosedState() {
        Command acceptCommand = new Command(Command.CommandValue.ACCEPT, "reviewer", 
                Command.Resolution.INTCOMPLETED, "Accepted interview");
        interviewState.updateState(acceptCommand);

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

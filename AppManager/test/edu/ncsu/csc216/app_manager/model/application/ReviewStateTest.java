package edu.ncsu.csc216.app_manager.model.application;

import static org.junit.jupiter.api.Assertions.*;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.io.AppReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the ReviewState class.
 */
public class ReviewStateTest {
    private Application application;
    private ReviewState reviewState;

    private static final String REVIEW = "test-files/exp_app_review.txt";

    @BeforeEach
    public void setUp() {
        application = new Application(1, "Review", "New", 
                "Application summary", null, false, null, new ArrayList<>());
        reviewState = new ReviewState(application);
        application.setState(reviewState);
    }

    @Test
    public void testValidAcceptCommand() {
        Command command = new Command(Command.CommandValue.ACCEPT, "reviewer", null, "Accepted for interview");
        reviewState.updateState(command);
        assertEquals("Interview", application.getStateName());
        assertEquals("reviewer", application.getReviewer());
    }

    @Test
    public void testInvalidRejectCommand() {
        Command command = new Command(Command.CommandValue.REJECT, null, null, "Rejected");
        assertThrows(UnsupportedOperationException.class, () -> reviewState.updateState(command));
    }

    @Test
    public void testLoadReviewApplication() throws FileNotFoundException {
        Application loadedApp = loadApplicationFromFile(REVIEW);
        assertEquals("Review", loadedApp.getStateName());
    }

    private Application loadApplicationFromFile(String fileName) throws FileNotFoundException {
        List<Application> apps = AppReader.readAppsFromFile(fileName);
        return apps.get(0); // Assuming only one application per file for this test
    }
}

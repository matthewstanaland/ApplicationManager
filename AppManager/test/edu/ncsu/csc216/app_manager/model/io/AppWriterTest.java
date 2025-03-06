package edu.ncsu.csc216.app_manager.model.io;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.app_manager.model.application.Application;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Tests the AppWriter class for various scenarios.
 */
public class AppWriterTest {

    private static final String CLOSED_FILE = "test-files/act_app_closed.txt";
    private static final String EXPECTED_CLOSED = "test-files/exp_app_closed.txt";

    private static final String INTERVIEW_FILE = "test-files/act_app_interview.txt";
    private static final String EXPECTED_INTERVIEW = "test-files/exp_app_interview.txt";

    @Test
    public void testWriteClosedApplication() throws Exception {
        Application closedApp = new Application(4, "Closed", "Old", 
                                                "Application summary", null, true, 
                                                "OfferCompleted", new ArrayList<>());

        List<Application> apps = new ArrayList<>();
        apps.add(closedApp);

        AppWriter.writeAppsToFile(CLOSED_FILE, apps);

        List<String> expected = Files.readAllLines(Paths.get(EXPECTED_CLOSED));
        List<String> actual = Files.readAllLines(Paths.get(CLOSED_FILE));

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteInterviewApplication() throws Exception {
        Application interviewApp = new Application(5, "Interview", "New", 
                                                   "Interview in progress", "reviewer", false, 
                                                   null, new ArrayList<>());

        List<Application> apps = new ArrayList<>();
        apps.add(interviewApp);

        AppWriter.writeAppsToFile(INTERVIEW_FILE, apps);

        List<String> expected = Files.readAllLines(Paths.get(EXPECTED_INTERVIEW));
        List<String> actual = Files.readAllLines(Paths.get(INTERVIEW_FILE));

        assertEquals(expected, actual);
    }

    @Test
    public void testWriteToInvalidPath() {
        List<Application> apps = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            AppWriter.writeAppsToFile("/invalid-path/invalid.txt", apps);
        });
    }
}

package edu.ncsu.csc216.app_manager.model.io;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import edu.ncsu.csc216.app_manager.model.application.Application;

/**
 * Tests the AppReader class for various scenarios.
 */
public class AppReaderTest {

    private static final String VALID_FILE = "test-files/app1.txt";
    private static final String INVALID_STATE = "test-files/app3.txt";
    private static final String MISSING_SUM = "test-files/app6.txt";
    private static final String FILE_DNE = "test-files/app7.txt";

    @Test
    public void testReadValidFile() {
        List<Application> apps = AppReader.readAppsFromFile(VALID_FILE);
        assertNotNull(apps);
        assertEquals(3, apps.size());
    }

    @Test
    public void testReadInvalidStateFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            AppReader.readAppsFromFile(INVALID_STATE);
        });
    }

    @Test
    public void testMissingSummaryInFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            AppReader.readAppsFromFile(MISSING_SUM);
        });
    }

    @Test
    public void testReadNonExistentFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            AppReader.readAppsFromFile(FILE_DNE);
        });
    }

    @Test
    public void testFileWithMultipleApplications() {
        List<Application> apps = AppReader.readAppsFromFile(VALID_FILE);
        assertEquals(3, apps.size());
        assertEquals("Application summary 1", apps.get(0).getSummary());
    }
}

package edu.ncsu.csc216.app_manager.model.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.command.Command;

public class AppManagerTest {
    private AppManager manager;

    @Before
    public void setUp() {
        manager = AppManager.getInstance();
        manager.createNewAppList();
    }

    @Test
    public void testSingleton() {
        AppManager anotherManager = AppManager.getInstance();
        assertSame(manager, anotherManager);
    }

    @Test
    public void testAddAndGetApplication() {
        manager.addAppToList(Application.AppType.NEW, "Test App", "Initial note");
        Object[][] apps = manager.getAppListAsArray();
        assertEquals(1, apps.length);
        assertEquals(1, apps[0][0]); // App ID
        assertEquals(Application.REVIEW_NAME, apps[0][1]); // State
        assertEquals(Application.A_NEW, apps[0][2]); // Type
        assertEquals("Test App", apps[0][3]); // Summary
    }

    @Test
    public void testExecuteCommand() {
        manager.addAppToList(Application.AppType.NEW, "Test App", "Initial note");
        manager.executeCommand(1, new Command(Command.CommandValue.ACCEPT, "reviewer1", null, "Accepted"));
        Application app = manager.getAppById(1);
        assertEquals(Application.INTERVIEW_NAME, app.getStateName());
    }

    @Test
    public void testDeleteApplication() {
        manager.addAppToList(Application.AppType.NEW, "Test App", "Initial note");
        manager.deleteAppById(1);
        assertNull(manager.getAppById(1));
    }

    @Test
    public void testSaveAndLoadApplications() {
        manager.addAppToList(Application.AppType.NEW, "Test App 1", "Note 1");
        manager.addAppToList(Application.AppType.OLD, "Test App 2", "Note 2");
        manager.saveAppsToFile("test_apps.txt");

        manager.createNewAppList();
        assertTrue(manager.getAppListAsArray().length == 0);

        manager.loadAppsFromFile("test_apps.txt");
        Object[][] loadedApps = manager.getAppListAsArray();
        assertEquals(2, loadedApps.length);
    }

    @Test
    public void testGetAppListAsArrayByAppType() {
        manager.addAppToList(Application.AppType.NEW, "New App", "Note 1");
        manager.addAppToList(Application.AppType.OLD, "Old App", "Note 2");

        Object[][] newApps = manager.getAppListAsArrayByAppType(Application.A_NEW);
        assertEquals(1, newApps.length);
        assertEquals("New App", newApps[0][3]);

        Object[][] oldApps = manager.getAppListAsArrayByAppType(Application.A_OLD);
        assertEquals(1, oldApps.length);
        assertEquals("Old App", oldApps[0][3]);
    }
}
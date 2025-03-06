package edu.ncsu.csc216.app_manager.model.manager;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;
import edu.ncsu.csc216.app_manager.model.io.AppReader;
import edu.ncsu.csc216.app_manager.model.io.AppWriter;

import java.util.List;

/**
 * Singleton class that manages AppLists and file operations.
 */
public class AppManager {
    private static AppManager instance;
    private AppList appList;

    /**
     * Private constructor for Singleton pattern.
     */
    private AppManager() {
        appList = new AppList();
    }

    /**
     * Retrieves the single instance of AppManager.
     *
     * @return The AppManager instance.
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * Creates a new AppList.
     */
    public void createNewAppList() {
        appList = new AppList();
    }

    /**
     * Adds an application to the list.
     *
     * @param appType The type of the application.
     * @param summary The summary of the application.
     * @param note    The initial note for the application.
     */
    public void addAppToList(AppType appType, String summary, String note) {
        appList.addApp(appType, summary, note);
    }

    /**
     * Loads applications from a file.
     *
     * @param filename The file to load applications from.
     */
    public void loadAppsFromFile(String filename) {
        List<Application> apps = AppReader.readAppsFromFile(filename);
        appList.addApps(apps);
    }

    /**
     * Saves applications to a file.
     *
     * @param filename The file to save applications to.
     */
    public void saveAppsToFile(String filename) {
        AppWriter.writeAppsToFile(filename, appList.getApplications());
    }

    /**
     * Deletes an application by its ID.
     *
     * @param appId The ID of the application to delete.
     */
    public void deleteAppById(int appId) {
        appList.deleteAppById(appId);
    }

    /**
     * Gets an application by its ID.
     *
     * @param appId The ID of the application.
     * @return The application if found, otherwise null.
     */
    public Application getAppById(int appId) {
        return appList.getAppById(appId);
    }

    /**
     * Executes a command on an application by ID.
     *
     * @param appId   The ID of the application.
     * @param command The command to execute.
     */
    public void executeCommand(int appId, Command command) {
        appList.executeCommand(appId, command);
    }

    /**
     * Gets a 2D array of all applications for the GUI.
     *
     * @return A 2D Object array of applications.
     */
    public Object[][] getAppListAsArray() {
        List<Application> apps = appList.getApplications();
        return convertAppsToArray(apps);
    }

    /**
     * Gets a 2D array of applications by type for the GUI.
     *
     * @param appType The application type.
     * @return A 2D Object array of applications matching the type.
     */
    public Object[][] getAppListAsArrayByAppType(String appType) {
        if (appType == null) {
            throw new IllegalArgumentException("Application type cannot be null.");
        }
        List<Application> apps = appList.getApplicationsByType(AppType.valueOf(appType.toUpperCase()));
        return convertAppsToArray(apps);
    }

    /**
     * Converts a list of applications to a 2D Object array.
     *
     * @param apps The list of applications.
     * @return A 2D Object array.
     */
    private Object[][] convertAppsToArray(List<Application> apps) {
        Object[][] array = new Object[apps.size()][4];
        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            array[i][0] = app.getAppId();
            array[i][1] = app.getStateName();
            array[i][2] = app.getAppType();
            array[i][3] = app.getSummary();
        }
        return array;
    }
}

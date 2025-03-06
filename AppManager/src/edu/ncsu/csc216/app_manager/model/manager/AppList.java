package edu.ncsu.csc216.app_manager.model.manager;

import edu.ncsu.csc216.app_manager.model.application.Application;
import edu.ncsu.csc216.app_manager.model.application.Application.AppType;
import edu.ncsu.csc216.app_manager.model.command.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages a list of applications and supports various operations.
 */
public class AppList {
    private List<Application> applications;
    private int nextAppId;

    /**
     * Constructs a new AppList and resets the counter to 0.
     */
    public AppList() {
        this.applications = new ArrayList<>();
        this.nextAppId = 0;
    }

    /**
     * Adds a collection of applications to the list in sorted order, ignoring duplicates.
     *
     * @param apps The list of applications to add.
     */
    public void addApps(List<Application> apps) {
        for (Application app : apps) {
            addApp(app);
        }
        updateNextAppId();
    }

    /**
     * Adds a new application to the list.
     *
     * @param appType The type of the application.
     * @param summary The summary of the application.
     * @param note    The initial note for the application.
     */
    public void addApp(AppType appType, String summary, String note) {
        Application app = new Application(nextAppId++, appType, summary, note);
        addApp(app);
    }

    /**
     * Adds a single application to the list in sorted order, ignoring duplicates.
     *
     * @param app The application to add.
     */
    private void addApp(Application app) {
        if (getAppById(app.getAppId()) != null) {
            return; // Ignore duplicate IDs
        }
        applications.add(app);
        Collections.sort(applications, (a1, a2) -> Integer.compare(a1.getAppId(), a2.getAppId()));
    }

    /**
     * Removes an application by its ID.
     *
     * @param appId The ID of the application to remove.
     */
    public void deleteAppById(int appId) {
        applications.removeIf(app -> app.getAppId() == appId);
    }

    /**
     * Searches for an application by ID.
     *
     * @param appId The ID of the application.
     * @return The application if found, otherwise null.
     */
    public Application getAppById(int appId) {
        return applications.stream()
                .filter(app -> app.getAppId() == appId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Executes a command on an application by ID.
     *
     * @param appId   The ID of the application.
     * @param command The command to execute.
     */
    public void executeCommand(int appId, Command command) {
        Application app = getAppById(appId);
        if (app != null) {
            app.update(command);
        }
    }

    /**
     * Returns the entire list of applications.
     *
     * @return A list of all applications.
     */
    public List<Application> getApplications() {
        return new ArrayList<>(applications);
    }

    /**
     * Returns a list of applications filtered by application type.
     *
     * @param appType The application type to filter by.
     * @return A list of applications matching the type.
     */
    public List<Application> getApplicationsByType(AppType appType) {
        List<Application> filteredApps = new ArrayList<>();
        for (Application app : applications) {
            if (app.getAppType().equals(appType.name())) {
                filteredApps.add(app);
            }
        }
        return filteredApps;
    }

    /**
     * Updates the next application ID based on the current list.
     */
    private void updateNextAppId() {
        if (!applications.isEmpty()) {
            nextAppId = applications.get(applications.size() - 1).getAppId() + 1;
        }
    }
}

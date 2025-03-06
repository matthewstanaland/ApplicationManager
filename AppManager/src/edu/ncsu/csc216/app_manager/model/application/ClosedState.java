package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Represents the Closed state of an application.
 */
public class ClosedState implements AppState {

    /** Reference to the associated Application */
    private Application application;

    /**
     * Constructor for ClosedState.
     * 
     * @param application The application associated with this state.
     */
    public ClosedState(Application application) {
        this.application = application;
    }

    /**
     * Updates the state based on the given command.
     * 
     * @param command The command to process.
     * @throws UnsupportedOperationException if the command is invalid for the current state.
     */
    @Override
    public void updateState(Command command) {
        if (command.getCommand() == Command.CommandValue.REOPEN) {
            // Check if the command has a valid resolution to reopen the application
            if (command.getResolution() == Command.Resolution.REVCOMPLETED) {
                // Transition from New application to Old and switch to Review State
                application.setState(application.getReviewState());
                application.addNote("[Reopened] " + command.getNote());
            } else {
                throw new UnsupportedOperationException("Cannot reopen application with the given resolution.");
            }
        } else {
            throw new UnsupportedOperationException("Invalid command for Closed state.");
        }
    }

    /**
     * Gets the name of the current state.
     * 
     * @return The name of the state.
     */
    @Override
    public String getStateName() {
        return Application.CLOSED_NAME;
    }
}

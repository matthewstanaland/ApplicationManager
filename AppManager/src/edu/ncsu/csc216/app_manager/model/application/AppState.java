package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Interface for application states in the application management system.
 */
public interface AppState {
    /**
     * Updates the state based on the given command.
     * @param command The command to process.
     * @throws UnsupportedOperationException if the command is invalid for the current state.
     */
    void updateState(Command command);

    /**
     * Gets the name of the current state.
     * @return The name of the state.
     */
    String getStateName();
}
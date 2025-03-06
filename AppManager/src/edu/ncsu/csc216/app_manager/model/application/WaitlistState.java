package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Represents the Waitlist state of an application.
 */
public class WaitlistState implements AppState {

    /** Reference to the associated Application */
    private Application application;

    /**
     * Constructor for WaitlistState.
     * 
     * @param application The application associated with this state.
     */
    public WaitlistState(Application application) {
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
        switch (command.getCommand()) {
            case REOPEN:
                // Transition to ReviewState if REOPEN is valid
                application.setState(application.getReviewState());
                application.addNote(command.getNote());
                break;

            default:
                // Unsupported commands result in an exception
                throw new UnsupportedOperationException("Invalid command for Waitlist state.");
        }
    }

    /**
     * Gets the name of the current state.
     * 
     * @return The name of the state.
     */
    @Override
    public String getStateName() {
        return Application.WAITLIST_NAME;
    }
}

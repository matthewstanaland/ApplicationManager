package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Represents the Reference Check state of an application.
 */
public class RefChkState implements AppState {

    /** Reference to the associated Application */
    private Application application;

    /**
     * Constructor for RefChkState.
     * 
     * @param application The application associated with this state.
     */
    public RefChkState(Application application) {
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
            case ACCEPT:
                application.setState(application.getOfferState());
                application.addNote("[Accepted] " + command.getNote());
                break;

            case REJECT:
                application.setState(application.getClosedState());
                application.addNote("[Rejected] " + command.getNote());
                break;

            default:
                throw new UnsupportedOperationException("Invalid command for Reference Check state.");
        }
    }

    /**
     * Gets the name of the current state.
     * 
     * @return The name of the state.
     */
    @Override
    public String getStateName() {
        return Application.REFCHK_NAME;
    }
}

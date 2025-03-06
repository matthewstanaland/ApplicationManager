package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Represents the Review state of an application.
 */
public class ReviewState implements AppState {

    /** Reference to the associated Application */
    private Application application;

    /**
     * Constructor for ReviewState.
     * 
     * @param application The application associated with this state.
     */
    public ReviewState(Application application) {
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
                application.setReviewer(command.getReviewerId());
                application.setState(application.getInterviewState());
                application.addNote("[Accepted] " + command.getNote());
                break;

            case REJECT:
                application.setState(application.getClosedState());
                application.addNote("[Rejected] " + command.getNote());
                break;

            default:
                throw new UnsupportedOperationException("Invalid command for Review state.");
        }
    }

    /**
     * Gets the name of the current state.
     * 
     * @return The name of the state.
     */
    @Override
    public String getStateName() {
        return Application.REVIEW_NAME;
    }
}

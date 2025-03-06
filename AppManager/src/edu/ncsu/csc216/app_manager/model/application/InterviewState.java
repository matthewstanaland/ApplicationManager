package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Represents the Interview state of an application.
 */
public class InterviewState implements AppState {

    /** Reference to the associated Application */
    private Application application;

    /**
     * Constructor for InterviewState.
     * 
     * @param application The application associated with this state.
     */
    public InterviewState(Application application) {
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
                // Mark paperwork as processed and transition to Reference Check state
                application.setReviewer(command.getReviewerId());
                application.setState(application.getRefChkState());
                application.addNote("[Accepted] " + command.getNote());
                break;

            case REJECT:
                // Mark the resolution and transition to Closed state
                application.setState(application.getClosedState());
                application.addNote("[Rejected] " + command.getNote());
                break;

            case STANDBY:
                // Transition the application to Waitlist state
                application.setState(application.getWaitlistState());
                application.addNote("[Standby] " + command.getNote());
                break;

            default:
                throw new UnsupportedOperationException("Invalid command for Interview state.");
        }
    }

    /**
     * Gets the name of the current state.
     * 
     * @return The name of the state.
     */
    @Override
    public String getStateName() {
        return Application.INTERVIEW_NAME;
    }
}

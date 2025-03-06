package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;

/**
 * Represents the Offer state of an application.
 * The Offer state manages the transition to Closed state based on ACCEPT or REJECT commands.
 */
public class OfferState implements AppState {

    /** Reference to the associated Application */
    private Application application;

    /**
     * Constructor for OfferState.
     * 
     * @param application The application associated with this state.
     */
    public OfferState(Application application) {
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
                application.setProcessPaperwork(true); // Paperwork is processed on acceptance
                application.addNote(command.getNote());
                application.setState(application.getClosedState());
                break;

            case REJECT:
                application.addNote(command.getNote());
                application.setState(application.getClosedState());
                break;

            default:
                throw new UnsupportedOperationException("Invalid command for Offer state.");
        }
    }

    /**
     * Gets the name of the current state.
     * 
     * @return The name of the state.
     */
    @Override
    public String getStateName() {
        return Application.OFFER_NAME;
    }
}

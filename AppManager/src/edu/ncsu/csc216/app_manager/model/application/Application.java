package edu.ncsu.csc216.app_manager.model.application;

import edu.ncsu.csc216.app_manager.model.command.Command;
import java.util.ArrayList;

/**
 * Represents an application managed in the system with state transitions.
 */
public class Application {
    // Public Constants for State Names and App Types
    public static final String A_NEW = "New";
    public static final String A_OLD = "Old";
    public static final String A_HIRED = "Hired";
    public static final String REVIEW_NAME = "Review";
    public static final String INTERVIEW_NAME = "Interview";
    public static final String WAITLIST_NAME = "Waitlist";
    public static final String REFCHK_NAME = "RefCheck";
    public static final String OFFER_NAME = "Offer";
    public static final String CLOSED_NAME = "Closed";
    
    /**
     * Enumeration for the type of application.
     */
    public enum AppType {
        NEW, OLD, HIRED
    }

    // Application fields
    private int appId;
    private String summary;
    private String reviewer;
    private boolean processPaperwork;
    private ArrayList<String> notes;
    private AppState currentState;
    private AppType appType;

    // Final instances of all states
    private final AppState reviewState;
    private final AppState interviewState;
    private final AppState waitlistState;
    private final AppState refChkState;
    private final AppState offerState;
    private final AppState closedState;

    /**
     * Constructor to create a new Application.
     * 
     * @param id Application ID.
     * @param appType 
     * @param summary Summary of the application.
     * @param note Initial note for the application.
     * @throws IllegalArgumentException if parameters are invalid.
     */
    public Application(int id, AppType appType, String summary, String note) {
        if (id < 1 || summary == null || summary.isEmpty() || note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Application cannot be created.");
        }

        this.appId = id;
        this.summary = summary;
        this.notes = new ArrayList<>();
        this.notes.add(note);
        this.processPaperwork = false;
        this.reviewer = null;

        // Initialize state instances
        this.reviewState = new ReviewState(this);
        this.interviewState = new InterviewState(this);
        this.waitlistState = new WaitlistState(this);
        this.refChkState = new RefChkState(this);
        this.offerState = new OfferState(this);
        this.closedState = new ClosedState(this);

        // Set initial state
        this.currentState = reviewState;
    }

    /**
     * Constructor to create an Application with specific parameters, typically used
     * when loading from a file.
     * 
     * @param id Application ID.
     * @param state The name of the initial state.
     * @param appType The type of application (New, Old, Hired).
     * @param summary The summary of the application.
     * @param reviewer The reviewer ID.
     * @param processPaperwork Whether the paperwork has been processed.
     * @param resolution The resolution of the application.
     * @param notes The notes associated with the application.
     * @throws IllegalArgumentException if any parameters are invalid.
     */
    public Application(int id, String state, String appType, String summary, 
            String reviewer, boolean processPaperwork, String resolution, 
            ArrayList<String> notes) {
    		if (id < 1 || summary == null || summary.isEmpty() || notes == null) {
    			throw new IllegalArgumentException("Application cannot be created.");
    		}
    		this.appId = id;
    		this.summary = summary;
    		this.reviewer = reviewer;
    		this.processPaperwork = processPaperwork;
    		this.notes = notes;

    		// Initialize state instances
    		this.reviewState = new ReviewState(this);
    		this.interviewState = new InterviewState(this);
    		this.waitlistState = new WaitlistState(this);
    		this.refChkState = new RefChkState(this);
    		this.offerState = new OfferState(this);
    		this.closedState = new ClosedState(this);

    		// Set the initial state using the provided state name
    		setState(state);
    }


    /**
     * Sets the current state of the application based on the state name.
     * 
     * @param state The name of the state.
     * @throws IllegalArgumentException if the state name is invalid.
     */
    public void setState(String state) {
        switch (state) {
            case REVIEW_NAME:
                this.currentState = reviewState;
                break;
            case INTERVIEW_NAME:
                this.currentState = interviewState;
                break;
            case WAITLIST_NAME:
                this.currentState = waitlistState;
                break;
            case REFCHK_NAME:
                this.currentState = refChkState;
                break;
            case OFFER_NAME:
                this.currentState = offerState;
                break;
            case CLOSED_NAME:
                this.currentState = closedState;
                break;
            default:
                throw new IllegalArgumentException("Invalid state name: " + state);
        }
    }

    
	/**
     * Updates the state of the application based on the given command.
     * 
     * @param command The command to process.
     * @throws UnsupportedOperationException if the command is invalid for the current state.
     */
    public void update(Command command) {
        currentState.updateState(command);
    }

    /**
     * Adds a note to the application.
     * 
     * @param note The note to add.
     */
    public void addNote(String note) {
        if (note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Note cannot be empty.");
        }
        notes.add("[" + currentState.getStateName() + "] " + note);
    }

    /**
     * Sets the state of the application.
     * 
     * @param state The new state to set.
     */
    public void setState(AppState state) {
        this.currentState = state;
    }

    /**
     * Gets the application's ID.
     * 
     * @return The application ID.
     */
    public int getAppId() {
        return appId;
    }

    /**
     * Gets the summary of the application.
     * 
     * @return The summary.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the reviewer ID for the application.
     * 
     * @param reviewer The reviewer ID.
     */
    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    /**
     * Gets the reviewer ID.
     * 
     * @return The reviewer ID.
     */
    public String getReviewer() {
        return reviewer;
    }

    /**
     * Sets whether the paperwork for this application has been processed.
     * 
     * @param processed True if processed, false otherwise.
     */
    public void setProcessPaperwork(boolean processed) {
        this.processPaperwork = processed;
    }

    /**
     * Checks if the paperwork has been processed.
     * 
     * @return True if processed, false otherwise.
     */
    public boolean isProcessed() {
        return processPaperwork;
    }

    /**
     * Gets the list of notes as a single formatted string.
     * 
     * @return The notes string.
     */
    public String getNotesString() {
        StringBuilder notesString = new StringBuilder();
        for (String note : notes) {
            notesString.append("- ").append(note).append("\n");
        }
        return notesString.toString();
    }

    /**
     * Gets the current state name.
     * 
     * @return The name of the current state.
     */
    public String getStateName() {
        return currentState.getStateName();
    }

    /**
     * Gets the ReviewState instance.
     * 
     * @return The ReviewState.
     */
    public AppState getReviewState() {
        return reviewState;
    }

    /**
     * Gets the InterviewState instance.
     * 
     * @return The InterviewState.
     */
    public AppState getInterviewState() {
        return interviewState;
    }

    /**
     * Gets the WaitlistState instance.
     * 
     * @return The WaitlistState.
     */
    public AppState getWaitlistState() {
        return waitlistState;
    }

    /**
     * Gets the RefChkState instance.
     * 
     * @return The RefChkState.
     */
    public AppState getRefChkState() {
        return refChkState;
    }

    /**
     * Gets the OfferState instance.
     * 
     * @return The OfferState.
     */
    public AppState getOfferState() {
        return offerState;
    }

    /**
     * Gets the ClosedState instance.
     * 
     * @return The ClosedState.
     */
    public AppState getClosedState() {
        return closedState;
    }

    /**
     * Converts the application to a string representation for saving.
     * 
     * @return The string representation of the application.
     */
    @Override
    public String toString() {
        return String.format("* %d\n%s\n%s", appId, summary, getNotesString());
    }

    /**
     * Gets the application type as a string.
     * 
     * @return The string representation of the application type.
     */
    public String getAppType() {
        return appType.name();
    }
    
    /**
     * Sets the resolution of the application based on the provided string value.
     * 
     * @param resolution The resolution to set.
     * @throws IllegalArgumentException if the resolution is invalid.
     */
    public void setResolution(String resolution) {
        if (resolution == null || resolution.isEmpty()) {
            throw new IllegalArgumentException("Resolution cannot be null or empty.");
        }

        switch (resolution) {
            case "ReviewCompleted":
                this.currentState = getRefChkState(); // Set appropriate state if needed
                break;
            case "InterviewCompleted":
                this.currentState = getOfferState(); // Set appropriate state if needed
                break;
            case "ReferenceCheckCompleted":
            case "OfferCompleted":
                this.currentState = getClosedState(); // Application transitions to Closed state
                break;
            default:
                throw new IllegalArgumentException("Invalid resolution: " + resolution);
        }
    }


    /**
     * Gets the resolution of the application based on its current state.
     * 
     * @return The resolution as a string or "No Resolution" if not applicable.
     */
    public String getResolution() {
        if (currentState == getClosedState()) {
            return "OfferCompleted";
        } else if (currentState == getRefChkState()) {
            return "ReferenceCheckCompleted";
        } else if (currentState == getOfferState()) {
            return "InterviewCompleted";
        } else if (currentState == getReviewState()) {
            return "ReviewCompleted";
        } else {
            return "No Resolution";
        }
    }
}
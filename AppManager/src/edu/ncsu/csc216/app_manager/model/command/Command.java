package edu.ncsu.csc216.app_manager.model.command;

/**
 * Represents a user command that encapsulates an action 
 * and any necessary details to transition the state of an 
 * Application within the Application Manager FSM.
 * 
 * 
 * @author Matthew Stanaland
 */
public class Command {

    /** String constant for the "ReviewCompleted" resolution */
    public static final String R_REVCOMPLETED = "ReviewCompleted";
    /** String constant for the "InterviewCompleted" resolution */
    public static final String R_INTCOMPLETED = "InterviewCompleted";
    /** String constant for the "ReferenceCheckCompleted" resolution */
    public static final String R_REFCHKCOMPLETED = "ReferenceCheckCompleted";
    /** String constant for the "OfferCompleted" resolution */
    public static final String R_OFFERCOMPLETED = "OfferCompleted";

    /**
     * Enumeration for the different command values that can be issued
     * in the Application FSM.
     */
    public enum CommandValue { ACCEPT, REJECT, STANDBY, REOPEN }

    /**
     * Enumeration for the different resolution types available in the 
     * Application FSM.
     */
    public enum Resolution { REVCOMPLETED, INTCOMPLETED, REFCHKCOMPLETED, OFFERCOMPLETED }

    /** The command value of this Command object */
    private CommandValue command;
    /** The reviewer ID associated with the Command, if required */
    private String reviewerId;
    /** The resolution associated with the Command, if applicable */
    private Resolution resolution;
    /** A note for the Command */
    private String note;

    /**
     * Constructs a Command object with the given parameters. Validates the parameters
     * to ensure they align with the expected behavior for the FSM.
     * 
     * @param command The command value (cannot be null)
     * @param reviewerId The reviewer ID (required for ACCEPT command)
     * @param resolution The resolution (required for STANDBY and REJECT commands)
     * @param note A note for the command (cannot be null or empty)
     * @throws IllegalArgumentException if any validation fails
     */
    public Command(CommandValue command, String reviewerId, Resolution resolution, String note) {
        if (command == null) {
            throw new IllegalArgumentException("Invalid information.");
        }
        if (command == CommandValue.ACCEPT && (reviewerId == null || reviewerId.isEmpty())) {
            throw new IllegalArgumentException("Invalid information.");
        }
        if ((command == CommandValue.STANDBY || command == CommandValue.REJECT) && resolution == null) {
            throw new IllegalArgumentException("Invalid information.");
        }
        if (note == null || note.isEmpty()) {
            throw new IllegalArgumentException("Invalid information.");
        }

        this.command = command;
        this.reviewerId = reviewerId;
        this.resolution = resolution;
        this.note = note;
    }

    /**
     * Gets the command value of this Command.
     * 
     * @return the command value
     */
    public CommandValue getCommand() {
        return command;
    }

    /**
     * Gets the reviewer ID associated with this Command.
     * 
     * @return the reviewer ID, or null if not applicable
     */
    public String getReviewerId() {
        return reviewerId;
    }

    /**
     * Gets the resolution associated with this Command.
     * 
     * @return the resolution, or null if not applicable
     */
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * Gets the note associated with this Command.
     * 
     * @return the note
     */
    public String getNote() {
        return note;
    }
}

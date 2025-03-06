package edu.ncsu.csc216.app_manager.model.command;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests the Command class.
 */
public class CommandTest {

    /**
     * Tests the constructor with valid parameters.
     */
    @Test
    public void testCommandConstructorValid() {
        Command command = new Command(Command.CommandValue.ACCEPT, "reviewer123", 
                                      Command.Resolution.REVCOMPLETED, "Valid note");

        assertEquals(Command.CommandValue.ACCEPT, command.getCommand());
        assertEquals("reviewer123", command.getReviewerId());
        assertEquals(Command.Resolution.REVCOMPLETED, command.getResolution());
        assertEquals("Valid note", command.getNote());
    }

    /**
     * Tests the constructor with null CommandValue.
     */
    @Test
    public void testConstructorNullCommandValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(null, "reviewer123", Command.Resolution.REVCOMPLETED, "Valid note");
        });
    }

    /**
     * Tests the constructor with empty reviewerId for ACCEPT command.
     */
    @Test
    public void testConstructorEmptyReviewerId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(Command.CommandValue.ACCEPT, "", 
                        Command.Resolution.REVCOMPLETED, "Valid note");
        });
    }

    /**
     * Tests the constructor with null resolution for STANDBY command.
     */
    @Test
    public void testConstructorNullResolution() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(Command.CommandValue.STANDBY, null, null, "Valid note");
        });
    }

    /**
     * Tests the constructor with a null note.
     */
    @Test
    public void testConstructorNullNote() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(Command.CommandValue.REJECT, "reviewer123", 
                        Command.Resolution.INTCOMPLETED, null);
        });
    }

    /**
     * Tests the constructor with an empty note.
     */
    @Test
    public void testConstructorEmptyNote() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Command(Command.CommandValue.REJECT, "reviewer123", 
                        Command.Resolution.INTCOMPLETED, "");
        });
    }

    /**
     * Tests all getter methods.
     */
    @Test
    public void testGetters() {
        Command command = new Command(Command.CommandValue.REOPEN, "reviewer123", 
                                      Command.Resolution.REFCHKCOMPLETED, "Reopen note");

        assertEquals(Command.CommandValue.REOPEN, command.getCommand());
        assertEquals("reviewer123", command.getReviewerId());
        assertEquals(Command.Resolution.REFCHKCOMPLETED, command.getResolution());
        assertEquals("Reopen note", command.getNote());
    }
}

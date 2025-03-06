package edu.ncsu.csc216.app_manager.model.io;

import edu.ncsu.csc216.app_manager.model.application.Application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Handles writing Applications to a file.
 */
public class AppWriter {

    /**
     * Writes a list of Applications to a file.
     * 
     * @param filename     The name of the file to write to.
     * @param applications The list of Applications to write.
     * @throws IllegalArgumentException if the file cannot be written to.
     */
    public static void writeAppsToFile(String filename, List<Application> applications) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Application app : applications) {
                writer.print(app.toString());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to save file.", e);
        }
    }
}

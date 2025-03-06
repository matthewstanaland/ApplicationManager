package edu.ncsu.csc216.app_manager.model.io;

import edu.ncsu.csc216.app_manager.model.application.Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles reading Applications from a file.
 */
public class AppReader {

    /**
     * Reads applications from a file and returns them as a List.
     * 
     * @param filename The name of the file to read from.
     * @return A list of Applications read from the file.
     * @throws IllegalArgumentException if the file cannot be read or processed.
     */
    public static List<Application> readAppsFromFile(String filename) {
        List<Application> applications = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            StringBuilder fileContent = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                fileContent.append(fileScanner.nextLine()).append("\n");
            }

            String[] appBlocks = fileContent.toString().split("\\r?\\n?[*]");
            for (String block : appBlocks) {
                if (!block.trim().isEmpty()) {
                    applications.add(processApplication(block));
                }
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unable to load file.", e);
        }
        return applications;
    }

    /**
     * Processes a block of text to create an Application object.
     * 
     * @param block The block of text representing an Application.
     * @return The created Application object.
     * @throws IllegalArgumentException if the Application cannot be created.
     */
    private static Application processApplication(String block) {
        String[] lines = block.split("\\r?\\n?[-]");
        String[] fields = lines[0].split(",");

        if (fields.length != 7) {
            throw new IllegalArgumentException("Unable to load file.");
        }

        int id = Integer.parseInt(fields[0].trim());
        String state = fields[1].trim();
        String appType = fields[2].trim();
        String summary = fields[3].trim();
        String reviewer = fields[4].trim().isEmpty() ? null : fields[4].trim();
        boolean processPaperwork = Boolean.parseBoolean(fields[5].trim());
        String resolution = fields[6].trim().isEmpty() ? null : fields[6].trim();

        ArrayList<String> notes = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            notes.add(lines[i].trim());
        }

        return new Application(id, state, appType, summary, reviewer, processPaperwork, resolution, notes);
    }
}

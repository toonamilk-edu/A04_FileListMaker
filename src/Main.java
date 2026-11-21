import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static ArrayList<String> list = new ArrayList<>();
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        boolean done = false;
        String choice;

        do {
            printList();
            System.out.println("A - Add an item");
            System.out.println("D - Delete an item");
            System.out.println("I - Insert an item");
            System.out.println("P - Print the list");
            System.out.println("Q - Quit");
            System.out.println("O - Open a file");
            System.out.println("S - Save the list");

            choice = SafeInput.getRegExString(in, "Enter your choice", "[AaDdIiPpQqOoSs]");

            switch (choice) {
                case "A":
                case "a":
                    addItem();
                    break;
                case "D":
                case "d":
                    deleteItem();
                    break;
                case "I":
                case "i":
                    insertItem();
                    break;
                case "P":
                case "p":
                    printList();
                    break;
                case "Q":
                case "q":
                    done = quitProgram();
                    break;
                case "O":
                case "o":
                    loadListFile();
                    break;
                case "S":
                case "s":
                    saveListFile();
                    break;
            }
        } while (!done);
    }

    private static void addItem() {
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to add");
        list.add(item);
    }

    private static void deleteItem() {
        if (list.size() == 0) {
            System.out.println("There's nothing to delete.");
        } else {
            int itemNum = SafeInput.getRangedInt(in, "Enter the number of the item to delete", 1, list.size());
            list.remove(itemNum - 1);
        }
    }

    private static void insertItem() {
        int position = SafeInput.getRangedInt(in, "Enter the position to insert at", 1, list.size() + 1);
        String item = SafeInput.getNonZeroLenString(in, "Enter the item to insert");
        list.add(position - 1, item);
    }

    private static void printList() {
        System.out.println("Your list:");
        if (list.size() == 0) {
            System.out.println("(empty)");
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i));
        }
        System.out.println();
    }

    private static boolean quitProgram() {
        return SafeInput.getYNConfirm(in, "Are you sure you want to quit");
    }

    private static void loadListFile() {
        JFileChooser chooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            System.out.println("Open canceled.");
            return;
        }

        Path selectedPath = chooser.getSelectedFile().toPath();

        try {
            List<String> lines = Files.readAllLines(selectedPath);
            list.clear();
            for (String line : lines) {
                list.add(line);
            }

            System.out.println();
            System.out.println("File name: " + selectedPath.getFileName().toString());
            System.out.println("Total lines: " + lines.size());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void saveListFile() {
        JFileChooser chooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);
        chooser.setDialogTitle("Save File");

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            System.out.println("Save canceled.");
            return;
        }

        String fileName = chooser.getSelectedFile().getPath();
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        Path filePath = new File(fileName).toPath();
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String line : list) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("File saved successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
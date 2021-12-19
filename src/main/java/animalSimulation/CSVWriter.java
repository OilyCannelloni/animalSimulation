package animalSimulation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class CSVWriter {
    private PrintWriter writer;
    private int nColumns;
    public CSVWriter(String path) {
        try {
            this.writer = new PrintWriter(path);
        } catch (FileNotFoundException e) {
            System.out.println("CSVWriter: Invalid file path!");
            this.writer = null;
        }
    }

    public void writeHeader(LinkedList<String> columnNames) {
        this.nColumns = columnNames.size();
        this.writer.write(String.join(",", columnNames));
        this.writer.write("\n");
    }

    public void writeEntry(LinkedList<Number> entry) {
        if (entry.size() != this.nColumns) {
            System.out.println("CSVWriter: Invalid entry!");
            return;
        }
        LinkedList<String> strEntry = new LinkedList<>();
        for (Number n : entry) {
            strEntry.add(n.toString());
        }
        this.writer.write(String.join(",", strEntry));
        this.writer.write("\n");
    }

    public void close() {
        this.writer.close();
    }
}

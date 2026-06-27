package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class ReportService<T> {

    public void print(List<T> lines, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(this.getHeaders());
            for (T line : lines) {
                writer.newLine();
                writer.write(this.mapToString(line));
            }
        } catch (IOException _) {
        }
    }

    public abstract String getHeaders();
    public abstract String mapToString(T data);
}

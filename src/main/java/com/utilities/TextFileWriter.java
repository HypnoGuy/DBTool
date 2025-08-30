package com.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFileWriter {

    private String filePathAndName ;

    public TextFileWriter(String filePathAndName) {
        this.filePathAndName = filePathAndName;
    }

    public void Append(String textString) {
        WriteToFile(textString, true);
    }

    public void WriteAll(String textString) {
        WriteToFile(textString, false);
    }

    private void WriteToFile(String textString, boolean append) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathAndName, append))) {
                writer.write(textString);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the log file.");
            e.printStackTrace();
        }
    }
}

package de.PowerShell.JavaParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import com.google.gson.Gson;

import de.PowerShell.JavaParser.parsedResults.JavaSerializableClass;

public class Application {

    public static void main(String[] args) {

        if (args == null || args.length == 0) {
            System.out.println("Please enter the path to a *.java file as an argument.");
            System.exit(1);
        }

        try {
            JavaParsingWorker app = new JavaParsingWorker();
            File file = new File(args[0]);
            JavaSerializableClass parsedClass = app.parse(file);
            parsedClass.setPath(args[0]);
            Gson gson = new Gson();
            String json = gson.toJson(parsedClass);
            System.out.println(json);
        } catch (FileNotFoundException fileEx) {
            System.out.println(fileEx.getMessage());
            System.exit(1);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
            System.exit(1);
        }
    }
}

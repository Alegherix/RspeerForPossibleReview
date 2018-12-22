package Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class FileReading {

    public static List<String> readText(String textFileName){
        List<String> names = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("C:\\"+ textFileName +".txt")));
            names = bufferedReader.lines().collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return names;
    }

}

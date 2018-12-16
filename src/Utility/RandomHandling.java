package Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomHandling {

    public static int randomNumber(int n, int m){
        return ThreadLocalRandom.current().nextInt(n,m);
    }

    public static List<String> charactersToKill() {
        File file = new File("C:\\Users\\Martin.DESKTOP-1AEIPCT\\Documents\\RSPeer\\konton.txt");
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            return bf.lines().collect(Collectors.toList());
        }
        catch (Exception e){
            System.out.println("Could not read the file properly ");
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        charactersToKill().stream().forEach(System.out::println);
    }
}

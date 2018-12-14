package Utility;

import java.util.concurrent.ThreadLocalRandom;

public class RandomHandling {

    public static int randomNumber(int n, int m){
        return ThreadLocalRandom.current().nextInt(n,m);
    }

}

package System;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;


public class Randomizer {
    private static Random random;
    private static State state;
    private static List<Double> data = new ArrayList<Double>();
    enum State {
        NORMAL,
        CRYPTO
    }

    public Randomizer (State state) {
        setState(state);
    }

    private static void setState(State state){
        Randomizer.state = state;
        switch (Randomizer.state) {
            case NORMAL:
                random = new Random();
                break;
            case CRYPTO:
                try {
                    random = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    random = new SecureRandom();
                }

                break;
            default:
                random = new Random();
                Randomizer.state = State.NORMAL;
                break;

        }
    }

    public static void changeState(){
        if (state == State.CRYPTO) {
            setState(State.NORMAL);
        } else {
            setState(State.CRYPTO);
        }
    }

    public static double get(){
        double nr = random.nextDouble();
        data.add(nr);
        return nr;
    }

    public static State getState(){
        return Randomizer.state;
    }

    public static List<Double> getData(){
        return data;
    }

    public static void clearData(){
        data.clear();
    }
}

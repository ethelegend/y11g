package main;
public class Dice {
    public static int RollDice(int base, int[] dice) {
        int result = base;
        for (int i : dice) {
            result += (int) Math.floor(Math.random()*i+1);
        }
        return result;
    }
}

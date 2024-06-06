package util;
public class Dice { // Simulates rolling dice
    public static int rollDice(int base, int[] dice) {
        int result = base;
        for (int i : dice) {
            result += (int) Math.floor(Math.random()*i+1);
        }
        return result;
    }
    public static int rollCheck(int base) {
        int result = (int) Math.floor(Math.random()*20+1);
        if (result == 1) {
            return 0;
        } if (result == 20) {
            return 99;
        } return result + base;
    }
}

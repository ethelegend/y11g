package util;
public class Dice { // Simulates rolling dice
    public static int rollDice(int base, int[] dice) {
        int result = base; // Adds the base
        for (int i : dice) {
            result += (int) Math.floor(Math.random()*i+1); // Rolls each dice
        }
        return result; // Returns result
    }
    public static boolean rollCheck(int base, int check) {
        int result = (int) Math.floor(Math.random()*20+1); // Roll a d20
        if (result == 1) {
            return false; // Nat 1
        } if (result == 20) {
            return true; // Nat 20
        } return (result + base >= check);
    }
}

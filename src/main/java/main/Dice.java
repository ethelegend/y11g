package main;
public class Dice { // Simulates rolling dice
    public static int rollDice(int base, int[] dice) {
        int result = base;
        for (int i : dice) {
            result += (int) Math.floor(Math.random()*i+1);
        }
        return result;

    }
    public static int rollWithAdvantage(int base, int[] dice, int advantage) {
        boolean advantageDirection = (advantage > 0);
        advantage = Math.abs(advantage);
        int champion = (advantageDirection) ? 1 : 20;
        while (advantage >= 0) {
            int contender = rollDice(base,dice);
            if (Boolean.logicalXor((champion > contender), advantageDirection)) {
                champion = contender;
            }
            advantage--;
        }
        return champion;
    }
}

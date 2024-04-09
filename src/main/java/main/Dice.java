package main;
public class Dice {
    public static int RollDice(int base, int[] dice) {
        int result = base;
        for (int i : dice) {
            result += (int) Math.floor(Math.random()*i+1);
        }
        return result;

    }
    public static int RollWithAdvantage(int base, int[] dice, int advantage) {
        boolean advantageDirection = (advantage > 0);
        advantage = Math.abs(advantage);
        int champion = (advantageDirection) ? 1 : 20;
        while (advantage >= 0) {
            int contender = RollDice(base,dice);
            if (Boolean.logicalXor((champion > contender), advantageDirection)) {
                champion = contender;
            }
            advantage--;
        }
        return champion;
    }
}

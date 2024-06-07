package oop.entity;
import util.Dice;
import oop.weapon.Weapon;
import oop.weapon.Longsword;
import oop.weapon.Longbow;


public class Hobgoblin extends Entity {
    public Hobgoblin(int p, int g) {
        this.name = "Hobgoblin";
        this.ac = 18;
        this.speed = 30;
        this.hp = Dice.rollDice(2,new int[]{8,8}); // 2d8+2
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Longbow(3,1), // +3 to hit, 1d8+1
                new Longsword(3,1) // +3 to hit, 1d10+1
        };
        this.pos = p;
        this.gold = g;
    }
}

package oop.entity;
import main.Dice;
import oop.weapon.Weapon;
import oop.weapon.Longsword;
import oop.weapon.Longbow;


public class Hobgoblin extends Entity {
    public Hobgoblin(int p, int g) {
        this.name = "Hobgoblin";
        this.ac = 18;
        this.speed = 30;
        this.hp = Dice.rollDice(2,new int[]{8,8});
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Longbow(3,1),
                new Longsword(3,1)
        };
        this.pos = p;
        this.gold = g;
    }
}

package oop.entity;
import util.Dice;
import oop.weapon.Weapon;
import oop.weapon.Bite;


public class GiantRat extends Entity {
    public GiantRat(int p, int g) {
        this.name = "Giant Rat";
        this.ac = 12;
        this.speed = 30;
        this.hp = Dice.rollDice(0,new int[]{6,6});
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Bite(4,2,new int[]{4})
        };
        this.pos = p;
        this.gold = g;
    }
}


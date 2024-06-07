package oop.entity;
import oop.weapon.Weapon;
import oop.weapon.Bite;
import main.Static;


public class GiantRat extends Entity {
    public GiantRat(int p, int g) {
        this.name = "Giant Rat";
        this.ac = 12;
        this.speed = 30;
        this.hp = Static.rollDice(0,new int[]{6,6}); // 2d6
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Bite(4,2,new int[]{4}) // +4 to hit, 1d4+2
        };
        this.pos = p;
        this.gold = g;
    }
}
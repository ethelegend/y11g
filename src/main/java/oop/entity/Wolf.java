package oop.entity;
import util.Dice;
import oop.weapon.Weapon;
import oop.weapon.Bite;


public class Wolf extends Entity {
    public Wolf(int p, int g) {
        this.name = "Wolf";
        this.ac = 13;
        this.speed = 40;
        this.hp = Dice.rollDice(2,new int[]{8,8}); // 2d8+2
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Bite(4,2,new int[]{4,4}) // +4 to hit, 2d4+2
        };
        this.pos = p;
        this.gold = g;
    }
}


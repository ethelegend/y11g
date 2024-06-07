package oop.entity;
import oop.weapon.Weapon;
import oop.weapon.Scimitar;
import oop.weapon.Shortbow;
import main.Static;


public class Goblin extends Entity {
    public Goblin(int p, int g) {
        this.name = "Goblin";
        this.ac = 15;
        this.speed = 30;
        this.hp = Static.rollDice(0,new int[]{6,6}); // 2d6
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Shortbow(4,2), // +4 to hit, 1d6+2
                new Scimitar(4,2)  // +4 to hit, 1d6+2
        };
        this.pos = p;
        this.gold = g;
    }
}
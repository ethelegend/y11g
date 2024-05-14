package oop.entity;
import main.Dice;
import oop.weapon.Weapon;
import oop.weapon.Longsword;
import oop.weapon.Longbow;


public class Hobgoblin extends Entity {
    public Hobgoblin() {
        this.name = "Hobgoblin";
        this.ac = 18;
        this.speed = 30;
        this.hp = Dice.rollDice(2,new int[]{8,8});
        this.maxHP = this.hp;
        this.weapons = new Weapon[]{
                new Longsword(4,1),
                new Longbow(4,1)
        };
    }
}

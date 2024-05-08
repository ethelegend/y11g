package oop.entity;
import main.Dice;
import oop.weapon.Weapon;
import oop.weapon.Scimitar;
import oop.weapon.Shortbow;


public class Goblin extends Entity {
    public Goblin() {
        this.ac = 15;
        this.speed = 30;
        this.hp = Dice.rollDice(0,new int[]{6,6});
        this.weapons = new Weapon[]{
                new Scimitar(4,2),
                new Shortbow(4,2)
        };
    }
}
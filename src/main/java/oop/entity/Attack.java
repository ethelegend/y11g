package oop.entity;

public class Attack {
    Entity player;
    Entity[] monsters;
    int roomRadius;
    int i;
    public Attack(Entity p, Entity[] m, int r) {
        player = p;
        monsters = m;
        roomRadius = r;
        i = m.length - m.length;
        battle();
    }

    public void battle() {
        if (i == monsters.length) {
            i = 0;
            player.manualAttack();
        }
    }
}

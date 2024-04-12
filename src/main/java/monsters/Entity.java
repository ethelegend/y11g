package monsters;

import weapons.Weapon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Container;
import java.awt.GridLayout;

public class Entity {
    int ac;
    int speed;
    int hp;
    int maxHP;
    Weapon[] weapons;
    int pos;
    boolean player;
    int i;
    Object waiter;
    public boolean Attack(JFrame window, Entity target, int roomRadius) {
        i = 0;
        if (this.player) {
            return this.ManualAttack(window, target, roomRadius);
        } else {
            try {
                return this.AutomaticAttack(window, target, roomRadius);
            } catch (InterruptedException e) {
                return false;
            }
        }

    }
    public boolean ManualAttack(JFrame window, Entity target, int roomRadius) {
        return true;
    }
    public boolean AutomaticAttack(JFrame window, Entity target, int roomRadius) throws InterruptedException {
        JButton confirm = new JButton("OK");
        confirm.addActionListener(l -> {
            waiter.notifyAll();
        });
        /*  Explanation:
            The enemies are programmed to advance towards their target, and retreat when under half health.
            The other parts of the code is about if they are close to a wall or their target, in which case they should be stopped.
        */
        int right = (this.pos > target.pos) ? 1 : -1;
        if (this.maxHP / this.hp < 2) {
            int movementTarget = target.pos + 5 * right;
            if (Math.abs(this.pos - movementTarget) > this.speed) {
                this.pos -= this.speed * right;
                InfoPopup(window,"The enemy moved towards you (" + Math.abs(target.pos - this.pos) + "ft)", new JButton[]{confirm});
            } else {
                this.pos = target.pos + 5 * right;
                InfoPopup(window,"The enemy moved next to you (5ft)", new JButton[]{confirm});
            }
        } else {
            int movementTarget = roomRadius * right;
            if (Math.abs(this.pos - movementTarget) > this.speed) {
                this.pos += this.speed * right;
                InfoPopup(window,"The enemy moved away from you (" + Math.abs(target.pos - this.pos) + "ft)", new JButton[]{confirm});
            } else {
                this.pos = target.pos + 5 * right;
                InfoPopup(window,"The enemy moved against the wall (" + Math.abs(target.pos - this.pos) + "ft)", new JButton[]{confirm});
            }
        }
        waiter.wait();
        InfoPopup(window,"The enemy was too far away to attack", new JButton[] {confirm});
        for (Weapon w : this.weapons) {
            if (w.canAttack(this.pos = target.pos)) {
                int damage = w.attack(target.ac);
                InfoPopup(window,"The enemy attacked with their " + w.name + " and dealt " + damage + " damage", new JButton[] {confirm});
                target.hp -= damage;
            }
        }
        waiter.wait();
        if (target.hp > 0) {
            InfoPopup(window,"You have passed out", new JButton[] {confirm});
            waiter.wait();
            System.exit(0);
        }
        return true;
    }
    public void InfoPopup(JFrame window, String labelText, JButton[] button){
        window.getContentPane().removeAll();
        window.setLayout(new GridLayout(2,1));
        window.add(new JLabel(labelText,SwingConstants.CENTER));

        Container buttons = new Container();
        window.add(buttons);
        buttons.setLayout(new GridLayout());
        for (JButton b:button) {
            buttons.add(b);
        }

        window.setSize(button.length*100,200);
        window.repaint();
        window.revalidate();

}
}

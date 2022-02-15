package cz.cvut.fel.pjv.game.model.attacks;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.enemies.CloseEnemy;
import cz.cvut.fel.pjv.game.model.enemies.RangeEnemy;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FireTest {
    private JFXPanel panel = new JFXPanel();
    private Handler handler;
    private Fire fire;

    @BeforeEach
    public void setup(){
        handler = new Handler();
        fire = new Fire(100, 100, Gfx.FIRE_ATTACK, handler, 120, 120);
        handler.addSprite(fire);
    }

    @AfterEach
    public void clear(){
        handler = null;
        fire = null;
    }

    @Test
    public void dealDamageTest(){
        CloseEnemy enemy1 = new CloseEnemy(100,100,Gfx.CLOSE_ENEMY,handler);
        RangeEnemy enemy2 = new RangeEnemy(100,100,Gfx.RANGE_ENEMY,handler);
        handler.addSprite(enemy1);
        handler.addSprite(enemy2);
        int enemy1_health = enemy1.health - fire.getDamage();
        int enemy2_health = enemy2.health - fire.getDamage();

        fire.dealDamage(enemy1);
        fire.dealDamage(enemy2);

        assertEquals(enemy1_health , enemy1.health);
        assertEquals(enemy2_health , enemy2.health);
        for (int i = 0; i < 10; i++) {
            assertTrue(enemy1.isBurnt());
            assertTrue(enemy2.isBurnt());

            enemy1.update();
            enemy2.update();

            enemy1_health -= 1;
            enemy2_health -= 1;

            assertEquals(enemy1_health , enemy1.health);
            assertEquals(enemy2_health , enemy2.health);
        }

        assertFalse(enemy1.isBurnt());
        assertFalse(enemy2.isBurnt());

    }
}
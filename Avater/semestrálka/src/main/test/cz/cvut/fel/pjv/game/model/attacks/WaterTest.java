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

public class WaterTest {
    private JFXPanel panel = new JFXPanel();
    private Handler handler;
    private Water water;

    @BeforeEach
    public void setup(){
        handler = new Handler();
        water = new Water(100, 100, Gfx.WATER_ATTACK, handler, 120, 120);
        handler.addSprite(water);
    }

    @AfterEach
    public void clear(){
        handler = null;
        water = null;
    }

    @Test
    public void dealDamageTest(){
        RangeEnemy enemy = new RangeEnemy(100,100,Gfx.RANGE_ENEMY,handler);
        handler.addSprite(enemy);
        int expectedHealth = enemy.health - water.getDamage();
        int expectedHandlerSize = handler.getSprites().size() - 1;

        water.dealDamage(enemy);

        assertEquals(expectedHandlerSize, handler.getSprites().size());
        assertEquals(expectedHealth, enemy.health);
        assertTrue(enemy.isFrozen());
    }

    @Test
    public void killEnemyTest(){
        CloseEnemy enemy = new CloseEnemy(100,100,Gfx.CLOSE_ENEMY,handler);
        enemy.health = 1;
        handler.addSprite(enemy);

        water.dealDamage(enemy);

        assertTrue(enemy.isDead());
    }
}
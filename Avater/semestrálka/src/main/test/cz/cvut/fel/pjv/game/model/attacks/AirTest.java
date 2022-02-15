package cz.cvut.fel.pjv.game.model.attacks;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.enemies.CloseEnemy;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AirTest {
    private JFXPanel panel = new JFXPanel();
    private Handler handler;
    private Air air;

    @BeforeEach
    public void setup(){
        handler = new Handler();
        air = new Air(100, 100, Gfx.AIR_ATTACK, handler, 120, 120);
        handler.addSprite(air);
    }

    @AfterEach
    public void clear(){
        handler = null;
        air = null;
    }

    @Test
    public void dealDamageTest(){
        CloseEnemy enemy1 = new CloseEnemy(100,100,Gfx.CLOSE_ENEMY,handler);
        handler.addSprite(enemy1);
        int expectedHandlerSize = handler.getSprites().size() - 1;
        float expectedVelX = enemy1.getVelX() + (air.velX * 2);
        float expectedVelY = enemy1.getVelY() + (air.velY * 2);
        int expHealth = enemy1.health - air.getDamage();


        air.dealDamage(enemy1);

        assertEquals(expectedVelX, enemy1.getVelX());
        assertEquals(expectedVelY, enemy1.getVelY());
        assertEquals(expectedHandlerSize, handler.getSprites().size());
        assertEquals(expHealth, enemy1.health);


    }
}
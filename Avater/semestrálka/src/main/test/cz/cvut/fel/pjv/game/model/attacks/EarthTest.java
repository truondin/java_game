package cz.cvut.fel.pjv.game.model.attacks;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.model.enemies.CloseEnemy;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EarthTest {
    private JFXPanel panel = new JFXPanel();
    private Handler handler;
    private Earth earth;

    @BeforeEach
    public void setup(){
        handler = new Handler();
        earth = new Earth(100, 100, Gfx.EARTH_ATTACK, handler, 120, 120);
        handler.addSprite(earth);
    }

    @AfterEach
    public void clear(){
        handler = null;
        earth = null;
    }

    @Test
    public void dealDamageTest(){
        Player player = mock(Player.class);
        CloseEnemy enemy1 = new CloseEnemy(100,100,Gfx.CLOSE_ENEMY,handler);
        enemy1.setTarget(player);
        enemy1.setVelX(4);
        enemy1.setVelY(2);
        handler.addSprite(enemy1);
        int expectedHealth = enemy1.health - earth.getDamage();
        int expectedHandlerSize = handler.getSprites().size() -1;
        float expectedVelX = enemy1.getVelX() / 2;
        float expectedVelY = enemy1.getVelY() / 2;

        earth.dealDamage(enemy1);
        enemy1.update();

        assertEquals(expectedHealth, enemy1.health);
        assertTrue(enemy1.isSlowed());
        assertEquals(expectedVelX, enemy1.getVelX());
        assertEquals(expectedVelY, enemy1.getVelY());
        assertEquals(expectedHandlerSize, handler.getSprites().size());
    }
}
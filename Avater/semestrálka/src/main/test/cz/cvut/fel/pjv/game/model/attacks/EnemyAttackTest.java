package cz.cvut.fel.pjv.game.model.attacks;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyAttackTest {
    private JFXPanel panel = new JFXPanel();
    private Handler handler;
    private EnemyAttack enemyAttack;

    @BeforeEach
    public void setup(){
        handler = new Handler();
        enemyAttack = new EnemyAttack(100, 100, Gfx.EARTH_ATTACK, handler, 120, 120, 3);
        handler.addSprite(enemyAttack);
    }

    @AfterEach
    public void clear(){
        handler = null;
        enemyAttack = null;
    }

    @Test
    public void dealDamageTest() {
        Player player = new Player(100, 100, Gfx.PLAYER, handler);
        handler.addSprite(player);
        int damage = enemyAttack.getDamage();
        int expectedHealth = player.health - damage;
        int expectedHandlerSize = handler.getSprites().size() - 1;

        enemyAttack.dealDamage(player);

        assertEquals(expectedHealth, player.health);
        assertEquals(expectedHandlerSize, handler.getSprites().size());
    }

    @Test
    public void movingTest(){
        double expectedX = (enemyAttack.getX() + enemyAttack.getVelX());
        double expectedY = (enemyAttack.getY() + enemyAttack.getVelY());

        for (int i = 0; i < 5; i++){
            enemyAttack.update();
            assertEquals(expectedX, enemyAttack.getX(), "x");
            assertEquals(expectedY, enemyAttack.getY(), "y");
            expectedX = (enemyAttack.getX() + enemyAttack.getVelX());
            expectedY = (enemyAttack.getY() + enemyAttack.getVelY());

        }
    }


}
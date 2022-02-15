package cz.cvut.fel.pjv.game.model;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.attacks.AttackType;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private JFXPanel panel = new JFXPanel();
    private Handler handler;
    private Player player;

    @BeforeEach
    public void setup(){
        handler = new Handler();
        player = new Player(100, 100, Gfx.PLAYER, handler);
        handler.addSprite(player);
    }

    @AfterEach
    public void clear(){
        handler = null;
        player = null;
    }

    @ParameterizedTest
    @CsvSource({"0,0",
                "0,3",
                "3,0",
                "3,3,",
                "-3,-3",
                "-3,3",
                "3,-3",
    })
    public void moveTest(int velX, int velY) {
        double expectedX = player.getX() + velX;
        double expectedY = player.getY() + velY;
        player.setVelX(velX);
        player.setVelY(velY);

        player.move();

        assertEquals(expectedX, player.getX());
        assertEquals(expectedY, player.getY());
    }

    @Test
    public void changeAttackTest() {
        AttackType[] attacks = player.getAttackTypes();

        for (int i = 1; i <= 4; i++){
            player.changeAttack();
            if (i < 4){
                assertSame(attacks[i], player.getAttack());
            }else{
                assertSame(attacks[0], player.getAttack());
            }

        }
    }

    @Test
    public void airAttackTest() {
        AttackType currAttack = player.getAttack();
        int expectedHandlerSpritesSize = handler.getSprites().size() + 1;
        int expectedAmount = player.airAmmo - 1;

        player.attack(150,150);
        Sprite attack = handler.getSprites().get(handler.getSprites().size() -1);

        assertSame(AttackType.AIR, currAttack);
        assertEquals(expectedHandlerSpritesSize, handler.getSprites().size());
        assertSame(Gfx.AIR_ATTACK, attack.getGfx());
        assertEquals(expectedAmount, player.airAmmo);
    }
}
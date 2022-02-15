package cz.cvut.fel.pjv.game.model.objects;

import cz.cvut.fel.pjv.game.controller.Handler;
import cz.cvut.fel.pjv.game.model.Player;
import cz.cvut.fel.pjv.game.model.attacks.EnemyAttack;
import cz.cvut.fel.pjv.game.view.Gfx;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AirAmmoTest {
    private JFXPanel panel = new JFXPanel();
    private Handler handler;
    private AirAmmo ammo;

    @BeforeEach
    public void setup(){
        handler = new Handler();
        ammo = new AirAmmo(100, 100, Gfx.EARTH_ATTACK, handler);
        handler.addSprite(ammo);
    }

    @AfterEach
    public void clear(){
        handler = null;
        ammo = null;
    }

    @Test
    public void addAmmoTest(){
        Player player = new Player(100,100,Gfx.PLAYER, handler);
        handler.addSprite(player);
        int amount = ammo.getAmount();
        int expectedAirAmmo = player.airAmmo + amount;
        int expectedHandlerSize = handler.getSprites().size() - 1;

        ammo.addAmmo(player);

        assertEquals(expectedAirAmmo, player.airAmmo);
        assertEquals(expectedHandlerSize, handler.getSprites().size());
    }
}
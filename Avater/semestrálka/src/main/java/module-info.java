module org.example {
    requires javafx.controls;
    requires java.desktop;
    requires java.logging;
    requires javafx.media;
    requires javafx.swing;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports cz.cvut.fel.pjv.game.view;
    exports cz.cvut.fel.pjv.game.model;
    exports cz.cvut.fel.pjv.game.model.attacks;
    exports cz.cvut.fel.pjv.game.model.enemies;
    exports cz.cvut.fel.pjv.game.model.objects;
    exports cz.cvut.fel.pjv.game.model.gameWorld;
    exports cz.cvut.fel.pjv.game.controller;

    opens cz.cvut.fel.pjv.game.controller;
    opens cz.cvut.fel.pjv.game.model;
    opens cz.cvut.fel.pjv.game.model.attacks;
    opens cz.cvut.fel.pjv.game.model.enemies;
    opens cz.cvut.fel.pjv.game.model.objects;
    opens cz.cvut.fel.pjv.game.model.gameWorld;
}
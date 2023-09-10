package rzab.gui;

import javax.swing.*;
import java.awt.*;
import java.net.URISyntaxException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebView;
import rzab.code.js.Bridge;

public class WebLoader {


    static JFXPanel jfxPanel;
    static WebView webView;
    public static JFrame frame;

    static void build() {
        double decr = .8;
        if (frame == null) {
            frame = new MoveJFrame();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout());
            frame.setBounds(0, 0, 0, 0);
        }
        if (jfxPanel == null) {
            jfxPanel = new JFXPanel();
            jfxPanel.setPreferredSize(new Dimension((int) (1700 * decr), (int) (900 * decr)));
            frame.add(jfxPanel, BorderLayout.CENTER);
        }
        if (webView == null) {
            Platform.runLater(() -> {
                webView = new WebView();
                webView.setContextMenuEnabled(false);
                webView.setMinSize((int) (1700 * decr), (int) (900 * decr));
                webView.setCache(false);
                Bridge.resolve(webView);
                ScrollPane pane = new ScrollPane(webView);
                pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                Scene scene = new Scene(pane);
                jfxPanel.setScene(scene);
            });
        }
    }

    public void runLauncher() {
        build();
        Platform.runLater(() ->
        {
            try {
                webView.getEngine().load(getClass().getResource("/res/index.html").toURI().toString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
        frame.setVisible(true);
        frame.pack();
    }

    public void view(boolean b) {
        frame.setVisible(b);
    }
}



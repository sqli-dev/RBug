package rzab.code.js;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import rzab.DebugAgent;
import rzab.code.tools.DynamicPause;

public class Bridge {
    private static JavaConnector javaConnector = new JavaConnector();

    public static void resolve(WebView view) {
        WebEngine webEngine = view.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED == newValue) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("rdebugger", javaConnector);
                ToJs.asChild(view);
            }
        });
    }

    public static class JavaConnector {

        public void pause() {
            DebugAgent.pause();
        }

        public void resume() {
            DebugAgent.resume();
        }

        public void schedule(String name) {
            DynamicPause.getRequest(name);
        }

        public void resume(String UUID) {
            DynamicPause.PauseRequest r = DynamicPause.findRequest(UUID);
            if (r != null)
                r.unpause();
        }
    }
}

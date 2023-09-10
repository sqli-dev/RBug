package rzab.code.js;

import javafx.application.Platform;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import rzab.DebugAgent;
import rzab.code.tools.DynamicPause;

public class ToJs {
    static JSObject javascriptConnector;

    public static void asChild(WebView view) {
        javascriptConnector = (JSObject) view.getEngine().executeScript("setup_java_bridge()");
        DebugAgent.loaded();
    }

    /**
     * @param state 0 for incoming method, 1 for outcoming method
     * @param name  determine name of function
     */
    public static void pushMethod(int state, String name) {
        Platform.runLater(() -> javascriptConnector.call("pushMethod", state, name));
    }

    public static void pauseState(boolean state) {
        Platform.runLater(() -> javascriptConnector.call("pauseState", state));
    }

    public static void addSchedule(DynamicPause.PauseRequest request) {
        Platform.runLater(() -> javascriptConnector.call("addSchedule", request.methodName, request.stopped, request.arg, request.originalCast != null ? request.originalCast.getName() : null,request.uuid.toString()));
    }
}

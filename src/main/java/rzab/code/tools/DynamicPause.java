package rzab.code.tools;

import rzab.code.js.ToJs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DynamicPause {

    public static final List<PauseRequest> pauseList = new ArrayList<>();

    public static PauseRequest getRequest(String methodName) {
        PauseRequest pauseRequest = new PauseRequest(methodName);
        pauseList.add(pauseRequest);
        return pauseRequest;
    }
    public static PauseRequest findRequest(String request) {
        for (int i = 0; i < pauseList.size(); i++) {
            if(pauseList.get(i).uuid.toString().equals(request)) {
                return pauseList.get(i);
            }
        }
        return null;
    }

    public static PauseRequest enter(UUID uuid, Object arg) {
        for (int i = 0; i < pauseList.size(); i++) {
            PauseRequest request = pauseList.get(i);
            if (request == null) continue;
            if (request.uuid.equals(uuid)) {
                request.call();
                request.setArg(arg);
                return request;
            }
        }
        return null;
    }

    public static PauseRequest first(String name, Object arg) {
        for (int i = 0; i < pauseList.size(); i++) {
            PauseRequest request = pauseList.get(i);
            if (request == null) continue;
            if (request.methodName.equals(name)) {
                request.call();
                request.setArg(arg);
                return request;
            }
        }
        return null;
    }

    public static class PauseRequest {
        public boolean stopped = true;
        public final String methodName;
        public Object arg;
        public Class originalCast;
        public UUID uuid;

        public void setArg(Object arg) {
            if (arg != null) {
                this.originalCast = arg.getClass();
                this.arg = arg;
            }
        }

        public PauseRequest(String methodName) {
            this.uuid = UUID.randomUUID();
            this.methodName = methodName;
        }

        public void call() {
            System.out.println("K");
            ToJs.addSchedule(this);
            while (pauseList.contains(this) && this.stopped) {
                try {
                    Thread.sleep(100);
                    System.out.println(this.uuid);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void unpause() {
            pauseList.remove(this);
            this.stopped = false;
        }
    }
}

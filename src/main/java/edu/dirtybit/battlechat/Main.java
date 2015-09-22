package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.controller.Controller;
import edu.dirtybit.battlechat.controller.SessionController;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import java.util.List;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        staticFileLocation("/app");
        port(getHerokuAssignedPort());
        activateControllers(collectControllers());
    }

    private static void activateControllers(List<Controller> controllers) {
        controllers.forEach(c -> c.registerWebSockets());
        controllers.forEach(c -> c.buildRoutes());
    }

    private static List<Controller> collectControllers() {
        List<Controller> controllers = new ArrayList<>();
        controllers.add(new SessionController());
        return controllers;
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8080;
    }
}

package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.controller.Controller;
import edu.dirtybit.battlechat.controller.SessionController;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        staticFileLocation("/public");
        port(getHerokuAssignedPort());
        Controller session = new SessionController();
        session.buildRoutes();
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8080;
    }

}

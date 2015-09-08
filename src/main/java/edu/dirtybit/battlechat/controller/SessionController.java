package edu.dirtybit.battlechat.controller;

import static spark.Spark.*;

public class SessionController implements Controller {

    public void buildRoutes() {
           get("/hello", (req, res) -> sayHello());
    }

    private String sayHello() {
        return "Hello Heroku";
    }
}

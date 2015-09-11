package edu.dirtybit.battlechat.controller;

import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class SessionController implements Controller {

    public void buildRoutes() {
        get("/hello", (req, res) -> renderTemplate());
        post("/", (req, res) -> renderTemplate());
    }

    private String renderTemplate() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Test Bro");
        MustacheTemplateEngine engine = new MustacheTemplateEngine();
        return engine.render(new ModelAndView(map, "example.mustache"));
    }
}

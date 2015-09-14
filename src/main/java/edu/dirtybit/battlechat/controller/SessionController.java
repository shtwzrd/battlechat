package edu.dirtybit.battlechat.controller;

import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;

import edu.dirtybit.battlechat.SessionManager;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class SessionController implements Controller {
    private SessionManager manager = new SessionManager();

    public void buildRoutes() {
        get("/hello", (req, res) -> renderTemplate());
        get("/poop", (req, res) -> {
            String session = manager.enterQueue().toString();
            res.redirect("/session/" + session);
            return new String();
        });
    }

    private String renderTemplate() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Test Bro");
        MustacheTemplateEngine engine = new MustacheTemplateEngine();
        return engine.render(new ModelAndView(map, "example.mustache"));
    }
}

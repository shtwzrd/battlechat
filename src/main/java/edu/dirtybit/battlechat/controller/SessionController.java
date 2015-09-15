package edu.dirtybit.battlechat.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

import edu.dirtybit.battlechat.Session;
import edu.dirtybit.battlechat.SessionManager;
import edu.dirtybit.battlechat.model.Player;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class SessionController implements Controller {
    private SessionManager manager = new SessionManager();

    public void buildRoutes() {
        get("/session/:id", (req, res) -> {
            UUID id = UUID.fromString(req.params(":id"));
            Session session = manager.getSessionContainingPlayer(id);
            Map map = new HashMap<String,String>();
            map.put("name", session.getPlayers().get(0).getGivenName());
            return renderTemplate(map);
        });
        post("/", (req, res) -> {
            Player player = new Player(req.queryParams("username"));
            String session = manager.enterQueue(player).toString();
            res.redirect("/session/" + session);
            return "";
        });
    }

    private String renderTemplate(Map<String, String> map) {
        MustacheTemplateEngine engine = new MustacheTemplateEngine();
        return engine.render(new ModelAndView(map, "example.mustache"));
    }
}

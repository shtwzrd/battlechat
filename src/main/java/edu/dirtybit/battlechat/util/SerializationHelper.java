package edu.dirtybit.battlechat.util;

import edu.dirtybit.battlechat.model.*;
import edu.dirtybit.battlechat.BattleShipConfiguration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class SerializationHelper {

    public static GameMessage deserializeMessage(String msg) {
        Gson gson = new Gson();
        GameMessage obj = gson.fromJson(msg, GameMessage.class);

        TypeToken type = new TypeToken<GameMessage<String>>() {};
        switch(obj.getMessageType()) {
            case CHAT:
                type = new TypeToken<GameMessage<String>>() {};
                break;
            case CONFIGURATION:
                type = new TypeToken<GameMessage<BattleShipConfiguration>>() {};
                break;
            case PLACEMENT:
                type = new TypeToken<GameMessage<Fleet>>() {};
                break;
            case STATUS:
                type = new TypeToken<GameMessage<BattleChatStatus>>() {};
                break;
            case FIRE:
                type = new TypeToken<GameMessage<ArrayList<Coordinate>>>() {};
                break;
        }

        return gson.fromJson(msg, type.getType());
    }
}

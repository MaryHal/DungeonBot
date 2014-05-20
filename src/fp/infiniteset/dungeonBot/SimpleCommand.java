package fp.infiniteset.dungeonBot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

public interface SimpleCommand
{
    // Takes a command string (e.g. "!roll 5d6" or "!setting" and returns the response text.
    public String execute(MessageEvent<PircBotX> event);
}

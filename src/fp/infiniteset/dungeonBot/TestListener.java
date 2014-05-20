package fp.infiniteset.dungeonBot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Date;
import java.util.Random;

public class TestListener extends ListenerAdapter<PircBotX>
{
    protected Random rng;
    protected String currentSetting;
    protected HashMap<String, SimpleCommand> commandMap;
    protected HashMap<String, Character> characterMap;

    public TestListener()
    {
        rng = new Random();
        currentSetting = "";

        characterMap = new HashMap<String, Character>();

        SimpleCommand rollCommand = new SimpleCommand()
        {
            public String execute(MessageEvent<PircBotX> event)
            {
                Pattern rollPattern = Pattern.compile("^!roll\\s+(\\d+)d(\\d+)");
                Matcher rollMatcher = rollPattern.matcher(event.getMessage());

                Pattern procPattern = Pattern.compile("\\s*(c?)\\s*([<>=])\\s*(\\d*)");
                Matcher procMatcher = procPattern.matcher(event.getMessage());

                ArrayList<Integer> rolls = new ArrayList<Integer>();
                if (rollMatcher.find())
                {
                    int numRolls = Math.max(Integer.parseInt(rollMatcher.group(1)), 20);
                    int max = Math.max(Integer.parseInt(rollMatcher.group(2)), 256);

                    for (int i = 0; i < numRolls; i++)
                    {
                        rolls.add(rng.nextInt(max) + 1);
                    }
                }

                if (procMatcher.find())
                {
                    int count = 0;
                    int bound = Integer.parseInt(procMatcher.group(3));
                    for (Integer i : rolls)
                    {
                        if (procMatcher.group(2).equals("<"))
                        {
                            if (i < bound)
                                count++;
                        }
                        else if (procMatcher.group(2).equals(">"))
                        {
                            if (i > bound)
                                count++;
                        }
                        else if (procMatcher.group(2).equals("="))
                        {
                            if (i == bound)
                                count++;
                        }
                    }
                    return String.format("[%s] -> %s -> %d", event.getMessage(), rolls.toString(), count);
                }
                else
                {
                    return String.format("[%s] -> %s", event.getMessage(), rolls.toString());
                }
            }
        };

        SimpleCommand settingCommand = new SimpleCommand()
        {
            public String execute(MessageEvent<PircBotX> event)
            {
                int settingIndex = event.getMessage().indexOf(' ');
                if (settingIndex != -1)
                {
                    currentSetting = event.getMessage().substring(settingIndex + 1);
                }

                return "The current setting is: " + (currentSetting.isEmpty() ? "None" : currentSetting);
            }
        };

        SimpleCommand timeCommand = new SimpleCommand()
        {
            public String execute(MessageEvent<PircBotX> event)
            {
                return new Date().toString();
            }
        };

        SimpleCommand generateCommand = new SimpleCommand()
        {
            public String execute(MessageEvent<PircBotX> event)
            {
                characterMap.put(event.getUser().getNick(), new Character());
                return "Generated a character for " + event.getUser().getNick();
            }
        };

        SimpleCommand meCommand = new SimpleCommand()
        {
            public String execute(MessageEvent<PircBotX> event)
            {
                Character c = characterMap.get(event.getUser().getNick());
                if (c != null)
                {
                    return String.format("Health[%d], Attack[%d], Defense[%d]",
                            c.health, c.attack, c.defense);
                }

                return "No character found for " + event.getUser().getNick();
            }
        };

        SimpleCommand attackCommand = new SimpleCommand()
        {
            public String execute(MessageEvent<PircBotX> event)
            {
                String inputCommand = event.getMessage().trim();
                String user = event.getUser().getNick();

                int index = inputCommand.lastIndexOf(' ');
                if (index == -1)
                {
                    return "No target specified";
                }
                String target = inputCommand.substring(index + 1);

                Character userCharacter = characterMap.get(user);
                Character targetCharacter = characterMap.get(target);

                if (userCharacter == null)
                {
                    return "No character found for " + user;
                }

                if (targetCharacter == null)
                {
                    return "No character found for " + target;
                }

                targetCharacter.health -= userCharacter.attack - targetCharacter.defense;
                return "attacks " + target + " " + targetCharacter.health;
            }
        };

        SimpleCommand healCommand = new SimpleCommand()
        {
            public String execute(MessageEvent<PircBotX> event)
            {
                String user = event.getUser().getNick();

                Character userCharacter = characterMap.get(user);

                if (userCharacter == null)
                {
                    return "No character found for " + user;
                }

                userCharacter.health += rng.nextInt(4) + 2;
                return "healed " + userCharacter.health;
            }
        };

        commandMap = new HashMap<String, SimpleCommand>();
        commandMap.put("!roll", rollCommand);
        commandMap.put("!setting", settingCommand);
        commandMap.put("!time", timeCommand);

        commandMap.put("!generate", generateCommand);
        commandMap.put("!me", meCommand);
        commandMap.put("!attack", attackCommand);
        commandMap.put("!heal", healCommand);
    }

    public void onMessage(MessageEvent<PircBotX> event) throws Exception
    {
        for (String command : commandMap.keySet())
        {
            if (event.getMessage().startsWith(command))
            {
                event.respond(commandMap.get(command).execute(event));
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        Configuration<PircBotX> configuration = new Configuration.Builder<PircBotX>()
            .setName("bp92")
            .setLogin("nowhere")
            .setAutoNickChange(true)
            .setAutoReconnect(false)
            .setCapEnabled(true)
            .addCapHandler(new TLSCapHandler(new UtilSSLSocketFactory().trustAllCertificates(), true))
            .addListener(new TestListener())
            .setServerHostname("irc.freenode.net")
            .addAutoJoinChannel("#mhal")
            .buildConfiguration();

        //bot.connect throws various exceptions for failures
        try
        {
            PircBotX bot = new PircBotX(configuration);

            //Connect to the freenode IRC network
            bot.startBot();
        } //In your code you should catch and handle each exception seperately,
        //but here we just lump them all togeather for simpliciy
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

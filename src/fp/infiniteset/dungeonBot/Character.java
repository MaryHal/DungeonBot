package fp.infiniteset.dungeonBot;

import java.util.Random;

public class Character
{
    public static final long WAIT_TIME = 5 * 60 * 1000;
    public static final long MS_TO_HOURS = 3600000;

    public int health;
    public int attack;
    public int defense;
    public long time;

    public Character()
    {
        Random rng = new Random();
        health  = rng.nextInt(10) + 5;
        attack  = rng.nextInt(3) + 2;
        defense = rng.nextInt(1) + 1;
        time = 0;
    }

    public void logTime()
    {
        time = System.currentTimeMillis();
    }

    public void attack(Character target)
    {
        target.health -= this.attack - target.defense;
    }
}


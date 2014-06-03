package fp.infiniteset.dungeonBot;

import java.util.Random;

public class Character
{
    public static final long WAIT_TIME = 5 * 60 * 1000;
    public static final long MS_TO_HOURS = 3600000;

    public int health;
    public Weapon weapon;
    public long time;

    public Character(Weapon w)
    {
        Random rng = new Random();
        health  = rng.nextInt(10) + 5;
        weapon = w;
        time = 0;
    }

    public void logTime()
    {
        time = System.currentTimeMillis();
    }

    public void attack(Character target)
    {
        target.health -= this.weapon.modifier;
    }
}


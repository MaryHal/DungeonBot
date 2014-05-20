package fp.infiniteset.dungeonBot;

import java.util.Random;

public class Character
{
    public int health;
    public int attack;
    public int defense;

    public Character()
    {
        Random rng = new Random();
        health  = rng.nextInt(10) + 5;
        attack  = rng.nextInt(3) + 2;
        defense = rng.nextInt(1) + 1;
    }
}


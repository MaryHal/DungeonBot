package fp.infiniteset.dungeonBot;

import java.io.FileNotFoundException;
import java.util.Random;

public class ItemGenerator
{
    private WordCloud adjectives;
    private Random rng;

    public ItemGenerator() throws FileNotFoundException
    {
        adjectives = new WordCloud("adjectives");
        rng = new Random();
    }

    public Weapon makeWeapon()
    {
        return new Weapon(adjectives.getRandomWord() + " Sword",
                rng.nextInt(2));
    }
}


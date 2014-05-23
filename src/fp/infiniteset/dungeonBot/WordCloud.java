package fp.infiniteset.dungeonBot;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class WordCloud
{
    protected ArrayList<String> words;

    public WordCloud(String filename) throws FileNotFoundException
    {
        words = new ArrayList<String>();

        Scanner reader = new Scanner(new File(filename));
        while (reader.hasNextLine())
        {
            words.add(reader.nextLine());
        }
        reader.close();
    }

    public String getRandomWord()
    {
        Random rng = new Random();
        return words.get(rng.nextInt(words.size()));
    }
}


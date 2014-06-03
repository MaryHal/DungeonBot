package fp.infiniteset.dungeonBot;

public class Item
{
    protected String baseName;
    protected int modifier;

    public String toString()
    {
        return String.format("+%d %s", modifier, baseName);
    }
}


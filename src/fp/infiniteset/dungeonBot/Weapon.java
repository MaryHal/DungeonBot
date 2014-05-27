package fp.infiniteset.dungeonBot;

public class Weapon extends Item
{
    public Weapon(String name, int mod, int power)
    {
        this.baseName = name;
        this.modifier = mod;
        this.power = power;
    }
}


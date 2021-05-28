package ua.project.forit.data.entities.enums;

public enum WritingType
{
    POST(1, "post"), ARTICLE(2, "article"), NEW(3, "new");

    protected int id;
    protected String name;

    WritingType(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}

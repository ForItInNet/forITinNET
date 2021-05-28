package ua.project.forit.data.entities.enums;

public enum WritingReaction
{
    LIKE, DISLIKE, VIEW, NONE;

    @Override
    public String toString()
    {
        return this.name();
    }
}

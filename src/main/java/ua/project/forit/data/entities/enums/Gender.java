package ua.project.forit.data.entities.enums;

public enum Gender
{
    NONE(0), MALE(1), FEMALE(2);

    int id;

    Gender(int id)
    {
        this.id = id;
    }
}

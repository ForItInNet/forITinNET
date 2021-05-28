package ua.project.forit.data.entities.interfaces;

import lombok.Data;
import ua.project.forit.data.annotations.OpenForView;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.validators.TextNotBlank;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class Comment extends ModelEntity
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User author;

    @OpenForView
    @TextNotBlank
    protected String comment;

    @OpenForView
    protected int likesNumber;

    @OpenForView
    protected int dislikesNumber;

    @OpenForView
    protected Date publishDate;

    {
        likesNumber = 0;
        dislikesNumber = 0;
        publishDate = new Date();
    }
}

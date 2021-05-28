package ua.project.forit.data.entities.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ua.project.forit.data.annotations.OpenForView;
import ua.project.forit.data.entities.enums.WritingType;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.validators.TextNotBlank;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public abstract class Writing extends ModelEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @OpenForView
    @TextNotBlank
    protected String title;

    @JsonIgnore
    @OpenForView
    protected Date publishDate;

    @OpenForView
    protected boolean draft;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User author;

    protected WritingType type;

    {
        publishDate = new Date();
        draft = true;
    }

//    @OneToMany(mappedBy = "writing", cascade = CascadeType.ALL, orphanRemoval = true)
//    protected List<Comment> comments;

    public Writing() {}

    public Writing(Long id, String title, Date publishDate, boolean draft, WritingType type) {
        this.id = id;
        this.title = title;
        this.publishDate = publishDate;
        this.draft = draft;
        this.type = type;
    }

    public boolean isAuthor(User author)
    {
        return this.author.equals(author);
    }

    @Override
    public int hashCode()
    {
        return id.intValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Writing))
            return false;

        Writing authority = (Writing) object;
        return id.equals(authority.getId());
    }
}

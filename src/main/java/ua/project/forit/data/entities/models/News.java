package ua.project.forit.data.entities.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.project.forit.data.annotations.OpenForView;
import ua.project.forit.data.entities.interfaces.Writing;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "news")
public class News extends Writing
{
    @Size(min = 100)
    @NotBlank
    private String text;

    @OpenForView
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<NewsReaction> newsReactions;

    {
        newsReactions = new ArrayList<>();
    }
}

package ua.project.forit.data.entities.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.project.forit.data.annotations.OpenForView;
import ua.project.forit.data.entities.enums.WritingType;
import ua.project.forit.data.entities.interfaces.Writing;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonRootName(value = "article", namespace = "articles")
@Entity
@Table(name = "article")
public class Article extends Writing
{
    @OpenForView
    @Size(min = 150, max = 10000)
    @NotBlank
    protected String text;

    @OpenForView
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<ArticleReaction> articleReactions;

    {
        articleReactions = new ArrayList<>();
    }

    public Article() {}

    public Article(Long id, String title, Date publishDate, boolean draft, @Size(min = 150, max = 10000) @NotBlank String text) {
        super(id, title, publishDate, draft, WritingType.ARTICLE);
        this.text = text;
    }

    @Override
    public int hashCode()
    {
        return id.intValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Article))
            return false;

        Article article = (Article) object;
        return id.equals(article.getId());
    }
}

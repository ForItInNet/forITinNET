package ua.project.forit.data.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.forit.data.entities.enums.WritingReaction;
import ua.project.forit.data.entities.models.*;
import ua.project.forit.data.repositories.ArticleReactionRepository;
import ua.project.forit.data.repositories.NewsReactionRepository;
import ua.project.forit.data.repositories.PostReactionRepository;
import ua.project.forit.exceptions.WritingServiceException;

@Service
public class WritingReactionService
{
    protected final ArticleReactionRepository articleReactionRepository;
    protected final PostReactionRepository postReactionRepository;
    protected final NewsReactionRepository newsReactionRepository;

    @Autowired
    public WritingReactionService(ArticleReactionRepository articleReactionRepository,
                                  PostReactionRepository postReactionRepository,
                                  NewsReactionRepository newsReactionRepository)
    {
        this.articleReactionRepository = articleReactionRepository;
        this.postReactionRepository = postReactionRepository;
        this.newsReactionRepository = newsReactionRepository;
    }

    public WritingReaction getUserReactionOnWriting(@NonNull Article article, @NonNull User user)
    {
        ArticleReaction articleReaction = articleReactionRepository.getByArticleAndUser(article, user);

        if(articleReaction == null)
            return WritingReaction.NONE;
        else
            return articleReaction.getWritingReaction();
    }

    public WritingReaction getUserReactionOnWriting(@NonNull Post post, @NonNull User user) throws WritingServiceException
    {
        PostReaction postReaction = postReactionRepository.getByPostAndUser(post, user)
                .orElseThrow(() -> new WritingServiceException("Допис не знайдено"));


        return postReaction.getWritingReaction();
    }

    public WritingReaction getUserReactionOnWriting(@NonNull News news, @NonNull User user)
    {
        NewsReaction newsReaction = newsReactionRepository.getByNewsAndUser(news, user);

        if(newsReaction == null)
            return WritingReaction.NONE;
        else
            return newsReaction.getWritingReaction();
    }

    public void addUserViewIfNotExists(@NonNull Article article, @NonNull User user)
    {
        if(!articleReactionRepository.existsPostReactionByArticleAndUser(article, user))
            articleReactionRepository.saveAndFlush(new ArticleReaction(article, user));
    }

    public void addUserViewIfNotExists(@NonNull Post post, @NonNull User user)
    {
        if(!postReactionRepository.existsPostReactionByPostAndUser(post, user))
            postReactionRepository.saveAndFlush(new PostReaction(post, user));
    }

    public void addUserViewIfNotExists(@NonNull News news, @NonNull User user)
    {
        if(!newsReactionRepository.existsPostReactionByNewsAndUser(news, user))
            newsReactionRepository.saveAndFlush(new NewsReaction(news, user));
    }

    public void setReactionOnWriting(@NonNull Post post, @NonNull User user, @NonNull WritingReaction writingReaction) throws WritingServiceException
    {
        PostReaction reaction = postReactionRepository.getByPostAndUser(post, user)
                .orElseThrow(() -> new WritingServiceException("Допис не знайдено"));

        if(reaction.getWritingReaction() == writingReaction)
            reaction.setWritingReaction(WritingReaction.VIEW);
        else
            reaction.setWritingReaction(writingReaction);
        
        postReactionRepository.saveAndFlush(reaction);
    }
}

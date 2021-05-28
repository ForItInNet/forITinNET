package ua.project.forit.data.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.forit.data.entities.enums.WritingType;
import ua.project.forit.data.entities.models.Article;
import ua.project.forit.data.entities.models.Post;
import ua.project.forit.data.entities.models.PostComment;
import ua.project.forit.data.entities.models.User;
import ua.project.forit.data.repositories.ArticleRepository;
import ua.project.forit.data.repositories.PostRepository;
import ua.project.forit.exceptions.UserServiceException;
import ua.project.forit.exceptions.WritingServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WritingService
{
    protected final ArticleRepository articleRepository;
    protected final PostRepository postRepository;

    protected final WritingReactionService writingReactionService;

    @Autowired
    public WritingService(ArticleRepository articleRepository,
                          PostRepository postRepository,
                          WritingReactionService writingReactionService)
    {
        this.articleRepository = articleRepository;
        this.postRepository = postRepository;
        this.writingReactionService = writingReactionService;
    }

    public void createArticle(@NonNull Article article, @NonNull User user)
    {
        article.setAuthor(user);

        articleRepository.saveAndFlush(article);
    }

    public Post createPost(@NonNull Map<String, String> data, @NonNull User user)
    {
        Post post = new Post();

        post.setTitle(data.get("title"));
        post.setText(data.get("post"));
        post.setAuthor(user);
        post.setEntityToken(EntityTokenService.generatePostEntityToken());
        post.setType(WritingType.POST);

        postRepository.saveAndFlush(post);

        return post;
    }

    public Post getPostByEntityToken(@NonNull String entityToken) throws WritingServiceException
    {
        return postRepository.getPostByEntityToken(entityToken)
                .orElseThrow(() -> new WritingServiceException("Допис не знайдено"));
    }

    public void deletePost(@NonNull String userEmail, @NonNull String entityToken)
    {
        postRepository.removePostByAuthorEmailAndEntityToken(userEmail, entityToken);
    }

    public Map<String, List<Map<String, Object>>> getUserWritings(@NonNull User fromUser, @NonNull User forUser) throws UserServiceException
    {
        Map<String, List<Map<String, Object>>> writings = new HashMap<>();

        writings.put("articles", fromUser.getArticles().stream()
                .map(article -> {
                    writingReactionService.addUserViewIfNotExists(article, forUser);
                    Map<String, Object> data = article.getJSON();
                    data.put("emotion", writingReactionService.getUserReactionOnWriting(article, fromUser));
                    return data;
                })
                .collect(Collectors.toList()));

        writings.put("posts", fromUser.getPosts().stream()
                .map(post -> {
                    writingReactionService.addUserViewIfNotExists(post, forUser);
                    Map<String, Object> data = post.getJSON();
                    try {
                        data.put("emotion", writingReactionService.getUserReactionOnWriting(post, fromUser));
                    } catch (WritingServiceException ignore) {}
                    return data;
                })
                .collect(Collectors.toList()));

        writings.put("news", fromUser.getNews().stream()
                .map(news -> {
                    writingReactionService.addUserViewIfNotExists(news, forUser);
                    Map<String, Object> data = news.getJSON();
                    data.put("emotion", writingReactionService.getUserReactionOnWriting(news, fromUser));
                    return data;
                })
                .collect(Collectors.toList()));

        return writings;
    }

    public PostComment addPostComment(@NonNull User user, @NonNull Post post, @NonNull String comment)
    {
        List<PostComment> comments = post.getPostComments();
        PostComment postComment = new PostComment(user, post, comment);
        comments.add(postComment);
        post.setPostComments(comments);
        postRepository.saveAndFlush(post);

        return postComment;
    }
}

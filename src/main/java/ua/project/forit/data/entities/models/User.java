package ua.project.forit.data.entities.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.project.forit.data.annotations.OpenForView;
import ua.project.forit.data.entities.enums.Gender;
import ua.project.forit.data.entities.interfaces.ModelEntity;
import ua.project.forit.data.validators.NameNotBlank;
import ua.project.forit.data.validators.Password;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "user")
public class User extends ModelEntity implements UserDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @OpenForView
    @NameNotBlank
    protected String name;

    @OpenForView
    @NameNotBlank
    protected String surname;

    @OpenForView
    @Email(message = "Вказано невалідний email")
    protected String email;

    @Password
    protected String password;

    @OpenForView
    protected String userImg;

    @OpenForView
    protected String userHeaderImg;

    protected String phone;

    @OpenForView
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "authority_id"))
    protected Set<Authority> authorities;

    @OpenForView
    protected boolean active;

    protected Date userBanPeriod;
    protected Date permanentBanDate;

    protected Date writeArticleBanPeriod;
    protected Date writeArticlePermanentBanDate;

    @OpenForView
    protected Gender gender;

    @OpenForView
    protected Date dateOfBirthday;

    @OpenForView
    protected Date lastOnline;

    @OpenForView
    protected Date registrationDate;

    protected String registerIP;
    protected String lastIP;

    @OpenForView
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Article> articles;

    //@OpenForView
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<Post> posts;

    //@OpenForView
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<News> news;

    //@OpenForView
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<AuthenticationToken> authenticationTokens;

    @OpenForView
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<ArticleReaction> articleReactions;

    @OpenForView
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<PostReaction> postReactions;

    @OpenForView
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected List<NewsReaction> newsReactions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_following",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "following_id", referencedColumnName = "id"))
    protected List<User> followings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"))
    protected List<User> followers;

    {
        gender = Gender.NONE;

        dateOfBirthday = null;

        registrationDate = new Date();
        lastOnline = new Date();
        active = true;

        userBanPeriod = null;
        permanentBanDate = null;

        writeArticleBanPeriod = null;
        writeArticlePermanentBanDate = null;
        
        articles = new ArrayList<>();
        news = new ArrayList<>();
        authorities = new HashSet<>();
        authenticationTokens = new ArrayList<>();
        articleReactions = new ArrayList<>();
        postReactions = new ArrayList<>();
        newsReactions = new ArrayList<>();
        followings = new ArrayList<>();
        followers = new ArrayList<>();
    }

    public User() {}

    public User(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public User(Confirm confirm)
    {
        email = confirm.getEmail();
        surname = confirm.getSurname();
        name = confirm.getName();
        password = confirm.
                getPassword();
        registerIP = confirm.getRegisterIP();
        lastIP = registerIP;
        entityToken = email;
    }

    @Override
    protected void addAdditionalParameters(Map<String, Object> data)
    {
        data.put("userImg", getUserImg());

        data.put("followings", followings.stream().map(User::getShortJSON)
        .collect(Collectors.toList()));

        data.put("followers", followers.stream().map(User::getShortJSON)
                .collect(Collectors.toList()));
    }

    public String getUserImg()
    {
        return "https://res.cloudinary.com/for-it-in-net/image/upload/v1621419205/"
        + (userImg != null ? userImg : "null_ujzaxy") + ".jpg";
    }

    protected Map<String, Object> getShortJSON()
    {
        Map<String, Object> data = new HashMap<>();

        data.put("name", name);
        data.put("surname", surname);
        data.put("userImg", getUserImg());
        data.put("entityToken", entityToken);

        return data;
    }

    @Override
    public int hashCode()
    {
        return id.intValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof User))
            return false;

        User user = (User) object;
        return id.equals(user.getId());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return (userBanPeriod == null || userBanPeriod.after(new Date())) && permanentBanDate == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }
}

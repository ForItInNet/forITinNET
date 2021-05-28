package ua.project.forit.data.entities.models;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.codec.binary.Base64;
import ua.project.forit.data.annotations.OpenForView;
import ua.project.forit.data.entities.interfaces.JsonConvertor;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "authentication_token")
public class AuthenticationToken extends JsonConvertor
{
    @Transient
    transient private static final String KEY;

    @Transient
    transient private static final SecretKey secretKey;

    @Transient
    transient private static final long availableTokenTime;

    static
    {
        KEY = "Arf24kHrs1J";

        byte[] decodedKey = Base64.encodeBase64(KEY.getBytes());
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        availableTokenTime = 36000000L;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User user;

    @Size(max = 200)
    protected String token;

    @OpenForView
    protected Date registerDate;

    @OpenForView
    protected Date lastSingInDate;

    @OpenForView
    protected String tokenRegisterIP;

    protected Date dateExpiry;


    {
        lastSingInDate = new Date();
        registerDate = new Date();
    }

    public AuthenticationToken() {}

    public AuthenticationToken(@NonNull User user, @NonNull String tokenRegisterIP, boolean addDateExpiry)
    {
        this.user = user;
        this.tokenRegisterIP = tokenRegisterIP;

        Map<String, Object> data = new HashMap<>();

        data.put("email", user.getEmail());
        data.put("id", user.getId());
        data.put("password", user.getEmail());

        if(addDateExpiry)
            dateExpiry = new Date(System.currentTimeMillis() + availableTokenTime);

        token = Jwts.builder().setClaims(data)
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }


    protected Claims getTokenBody()
    {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return claimsJws.getBody();
    }



    @Override
    public String toString()
    {
        return token;
    }

    @Override
    public int hashCode()
    {
        return id.intValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof AuthenticationToken))
            return false;

        AuthenticationToken authenticationToken = (AuthenticationToken) object;
        return token.equals(authenticationToken.getToken());
    }
}

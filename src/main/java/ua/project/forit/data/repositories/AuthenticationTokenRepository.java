package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ua.project.forit.data.entities.models.AuthenticationToken;

import java.util.Optional;

public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Long>
{
    Optional<AuthenticationToken> getAuthenticationTokenByToken(String token);
    AuthenticationToken getAuthenticationTokenByTokenAndTokenRegisterIP(String token, String IP);

    boolean existsAuthenticationTokenByToken(String token);

    @Transactional
    void removeAuthenticationTokenByToken(String token);
}

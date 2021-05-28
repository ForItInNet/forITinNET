package ua.project.forit.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.forit.config.AuthoritiesContainer;
import ua.project.forit.data.entities.models.Authority;
import ua.project.forit.data.repositories.AuthorityRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityService
{
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityService(AuthorityRepository authorityRepository)
    {
        this.authorityRepository = authorityRepository;
    }

    @PostConstruct
    private void init()
    {
        Set<String> authorityInClass = new HashSet<>(AuthoritiesContainer.getAuthorities());

        Set<String> authorityInDatabase = authorityRepository.findAll().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());

        differenceAuthority(authorityInClass, authorityInDatabase)
                .forEach(authority -> authorityRepository.saveAndFlush(new Authority(authority)));
    }

    private Set<String> differenceAuthority(final Set<String> firstAuthorities,
                                            final Set<String> secondAuthorities)
    {
        Set<String> result = new HashSet<>(firstAuthorities);
        result.removeAll(secondAuthorities);

        return result;
    }
}

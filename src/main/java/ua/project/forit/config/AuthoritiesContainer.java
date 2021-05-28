package ua.project.forit.config;

import java.util.HashSet;
import java.util.Set;

public abstract class AuthoritiesContainer
{
    public enum Authorities
    {
        READER("READER", 1), REDACTOR("REDACTOR", 2),
        ADMIN("ADMIN", 3), SUPERUSER("SUPERUSER", 4);

        private final String authority;
        private final int rank;

        Authorities(String authority, int rank)
        {
            this.authority = authority;
            this.rank = rank;
        }
    }

    public static Set<String> getAuthorities()
    {
        Set<String> authorities = new HashSet<String>();

        for(Authorities authority: Authorities.values())
            authorities.add(authority.authority);

        return authorities;
    }

    public static Set<String> getSetAuthoritiesFrom(Authorities authority)
    {
        Set<String> authorities = new HashSet<String>();

        for(Authorities auth: Authorities.values())
            if(auth.rank >= authority.rank)
                authorities.add(auth.authority);

        return authorities;
    }

    public static String[] getArrayAuthoritiesFrom(Authorities authority)
    {
        Set<String> authorities = new HashSet<String>();

        for(Authorities auth: Authorities.values())
            if(auth.rank >= authority.rank)
                authorities.add(auth.authority);

        return authorities.toArray(new String[0]);
    }
}

package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.project.forit.data.entities.models.Authority;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long>
{


    Authority findByName(String name);

    //@Transactional
    //void removeByAuthority(String authority);
}

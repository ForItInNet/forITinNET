package ua.project.forit.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.project.forit.data.entities.models.Confirm;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, Long>
{
    Confirm getByConfirmHash(String confirmHash);

    @Transactional
    void removeConfirmByEmail(String email);
}

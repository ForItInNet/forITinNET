package ua.project.forit.data.entities.models;

import lombok.Data;
import ua.project.forit.data.validators.NameNotBlank;
import ua.project.forit.data.validators.Password;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;

@Data
@Entity
@Table(name = "confirm_request")
public class Confirm
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NameNotBlank
    protected String name;

    @NameNotBlank
    protected String surname;

    @Email(message = "Вказано невалідний email")
    protected String email;

    @Password
    protected String password;

    protected String registerIP;
    protected String confirmHash;
    protected String confirmCode;
    protected Date dateRequest;

    {
        dateRequest = new Date();
    }

    public boolean isCodeCorrect(String confirmCode)
    {
        return this.confirmCode.equals(confirmCode);
    }

    @Override
    public int hashCode()
    {
        return id.intValue();
    }

    @Override
    public boolean equals(Object object)
    {
        if(!(object instanceof Confirm))
            return false;

        Confirm request = (Confirm) object;
        return email.equals(request.getEmail());
    }
}

package ua.project.forit.data.entities.interfaces;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.project.forit.data.annotations.OpenForView;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = false)
@Data
@MappedSuperclass
public abstract class ModelEntity extends JsonConvertor
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;

    @OpenForView
    @NotBlank
    protected String entityToken;

    public ModelEntity() {}

}

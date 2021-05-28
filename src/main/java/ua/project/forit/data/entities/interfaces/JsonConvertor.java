package ua.project.forit.data.entities.interfaces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.project.forit.data.annotations.OpenForView;

import java.lang.reflect.Field;
import java.util.*;

public abstract class JsonConvertor
{
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    private static Set<String> objects = new HashSet<>();

    public final Map<String, Object> getJSON()
    {
        Map<String, Object> json = new HashMap<>();

        for(Field field: getObjectFields(this.getClass()))
        {
            try
            {
                json.put(getFieldName(field.getAnnotation(OpenForView.class), field), getFieldValue(field));
            }
            catch (IllegalAccessException ex)
            {
                logger.error(ex.getMessage());
            }
        }

        addAdditionalParameters(json);

        return json;
    }

    private List<Field> getObjectFields(Class<?> object)
    {
        List<Field> fields = new ArrayList<>();

        for(; object != JsonConvertor.class; object = object.getSuperclass())
        {
            for(Field field: object.getDeclaredFields())
            {
                if(field.isAnnotationPresent(OpenForView.class))
                {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
        }

        return fields;
    }

    private String getFieldName(OpenForView annotation, Field field)
    {
        return annotation.value().equals("none") ? field.getName() : annotation.value();
    }

    private Object getFieldValue(Field field) throws IllegalAccessException
    {
        Object fieldValue = field.get(this);

        if(fieldValue instanceof Collection)
        {
            List<Object> dataValue = new ArrayList<>();
            for(Object obj: (Collection) fieldValue)
            {
                String className = obj.getClass().getName();
                objects.add(className.substring(0, className.indexOf(".")) + Integer.toString(obj.hashCode()) + className.substring(className.lastIndexOf(".") + 1));

                if(obj instanceof JsonConvertor)
                    dataValue.add(((JsonConvertor) obj).getJSON());
            }

            return dataValue;
        }
        else
            return fieldValue;
    }

    protected void addAdditionalParameters(Map<String, Object> data)
    {
    }
}

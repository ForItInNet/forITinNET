package ua.project.forit.exceptions;

import java.util.function.Supplier;

public class WritingServiceException extends Throwable
{
    public WritingServiceException(String message)
    {
        super(message);
    }
}

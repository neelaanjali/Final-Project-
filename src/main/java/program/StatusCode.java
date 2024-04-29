package program;


/**
 * Enum representing potential status codes to be returned by methods
 * @author hargu
 */
public enum StatusCode {
    SUCCESS,			//method executed successfully
    FAILURE,			//method failed
    EXCEPTION,			//method failed with exception
    INVALID_INPUT,		//method failed due to invalid input
    NOT_FOUND,			//the object could not be found
    NOT_IMPLEMENTED,	//the method is not yet implemented
}

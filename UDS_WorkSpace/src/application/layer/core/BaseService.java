package application.layer.core;

public abstract class BaseService {
    public abstract Request makeRequest(Object... args);
    public abstract InterpretedResponse interpretResponse(Response response);
}

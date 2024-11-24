namespace FarmGameBackend.Middlewares;

public class DebugWithConstantUserMiddleware(RequestDelegate next)
{
    public async Task InvokeAsync(HttpContext context)
    {
        context.Items["Email"] = "testemail@gmail.com";
        await next(context);
    }
}
namespace FarmGameBackend.Middlewares;

public class DebugWithConstantUserMiddleware(RequestDelegate next)
{
    public async Task InvokeAsync(HttpContext context)
    {
        context.Items["Email"] = "testemail@mail.com";
        await next(context);
    }
}
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;

namespace FarmGameBackend.Middlewares;

public class DatabaseHandlerMiddleware(RequestDelegate next)
{
    public async Task InvokeAsync(HttpContext context)
    {
        var dbContext = context.RequestServices.GetRequiredService<FarmApplicationContext>();
        var userEmail = context.Items["Email"]?.ToString();
        if (userEmail == null)
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsync("Email is missing.");
            return;
        }
        
        User currentUser = dbContext.GetCurrentUser(userEmail);
        context.Items["CurrentUser"] = currentUser;
        
        await next(context);

        if (context.Response.StatusCode == StatusCodes.Status200OK || 
            context.Response.StatusCode == StatusCodes.Status201Created ||
            context.Response.StatusCode == StatusCodes.Status204NoContent)
        {
            await dbContext.SaveChangesAsync();
        }
    }
}

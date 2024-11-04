using Google.Apis.Auth;

namespace FarmGameBackend;

public class GoogleAuthMiddleware(RequestDelegate next)
{
    public async Task InvokeAsync(HttpContext context)
    {
        if (!context.Request.Headers.TryGetValue("Authorization", out var token))
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsync("Authorization header is missing.");
            return;
        }

        var idToken = token.ToString().Replace("Bearer ", "");
        var payload = await VerifyGoogleIdToken(idToken);
        if (payload == null)
        {
            context.Response.StatusCode = StatusCodes.Status401Unauthorized;
            await context.Response.WriteAsync("Invalid Google ID token.");
            return;
        }

        await next(context);
    }
    
    private async Task<GoogleJsonWebSignature.Payload?> VerifyGoogleIdToken(string idToken)
    {
        try
        {
            var settings = new GoogleJsonWebSignature.ValidationSettings()
            {
                Audience = new List<string>() { Environment.GetEnvironmentVariable("GOOGLE_CLIENT_ID")! }
            };

            var payload = await GoogleJsonWebSignature.ValidateAsync(idToken, settings);
            return payload;
        }
        catch (InvalidJwtException)
        {
            return null;
        }
    }
}
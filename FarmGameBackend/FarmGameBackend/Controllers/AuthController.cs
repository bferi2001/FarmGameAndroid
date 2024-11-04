using Google.Apis.Auth;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace FarmGameBackend.Controllers;

[ApiController]
[Route("[controller]")]
public class AccountController : ControllerBase
{
    [HttpPost("verify-google-id-token")]
    public async Task<IActionResult> VerifyGoogleIdToken([FromBody] string idToken)
    {
        try
        {
            var settings = new GoogleJsonWebSignature.ValidationSettings()
            {
                Audience = new List<string>() { Environment.GetEnvironmentVariable("GOOGLE_CLIENT_ID")! }
            };

            var payload = await GoogleJsonWebSignature.ValidateAsync(idToken, settings);
            return Ok(payload);
        }
        catch (InvalidJwtException)
        {
            // Token is invalid
            return Unauthorized();
        }
    }
}
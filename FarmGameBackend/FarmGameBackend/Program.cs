using DotNetEnv;
using FarmGameBackend;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Middlewares;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;

Env.Load();
const bool useAuth = true;

var builder = WebApplication.CreateBuilder(args);

string? connection;
if (builder.Environment.IsDevelopment())
{
    builder.Configuration.AddEnvironmentVariables().AddJsonFile("appsettings.Development.json");
    connection = builder.Configuration.GetConnectionString("AZURE_SQL_CONNECTIONSTRING");
}
else
{
    connection = Environment.GetEnvironmentVariable("AZURE_SQL_CONNECTIONSTRING");
}

builder.Services.AddDbContext<FarmApplicationContext>(options =>
    options.UseSqlServer(connection));
builder.Services.AddControllers();

if (useAuth)
{
    // Add authentication to swagger UI
    builder.Services.AddSwaggerGen(opt =>
    {
        opt.SwaggerDoc("v1", new OpenApiInfo { Title = "My Api", Version = "v1" });
        opt.AddSecurityDefinition("bearer", new OpenApiSecurityScheme
        {
            Type = SecuritySchemeType.Http,
            BearerFormat = "JWT",
            In = ParameterLocation.Header,
            Scheme = "bearer"
        });
        opt.OperationFilter<AuthenticationRequirementsOperationFilter>();
    });
}

// Add services to the container.
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

if (useAuth)
{
    // Middleware for Google authentication
    app.UseMiddleware<GoogleAuthMiddleware>();
}
else
{
    app.UseMiddleware<DebugWithConstantUserMiddleware>();
}

app.UseMiddleware<DatabaseHandlerMiddleware>();

app.UseHttpsRedirection();

app.MapControllers();
app.Run();

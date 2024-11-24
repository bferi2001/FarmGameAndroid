using DotNetEnv;
using FarmGameBackend;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Middlewares;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;

Env.Load();

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


app.UseCors();
app.UseHttpsRedirection();

app.MapControllers();
app.Run();

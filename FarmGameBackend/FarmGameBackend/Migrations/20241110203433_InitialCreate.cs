using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace FarmGameBackend.Migrations
{
    /// <inheritdoc />
    public partial class InitialCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Barns",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    UserName = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    TypeName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Position = table.Column<int>(type: "int", nullable: false),
                    ProductionStartTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false),
                    ProductionEndTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false),
                    FeedingTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: true),
                    CleaningTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Barns", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "BarnTypes",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    FirstUpgradeCost = table.Column<int>(type: "int", nullable: false),
                    SecondUpgradeCost = table.Column<int>(type: "int", nullable: false),
                    ThirdUpgradeCost = table.Column<int>(type: "int", nullable: false),
                    ProductName = table.Column<string>(type: "nvarchar(max)", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_BarnTypes", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Classifieds",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    UserName = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    Price = table.Column<int>(type: "int", nullable: false),
                    ProductName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Deadline = table.Column<DateTime>(type: "datetime2", nullable: false),
                    Quantity = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Classifieds", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "PlantedPlants",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    UserName = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    CropsTypeName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Position = table.Column<int>(type: "int", nullable: false),
                    PlantTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false),
                    HarvestTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: false),
                    WateringTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: true),
                    WeedingTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: true),
                    FertilisingTime = table.Column<DateTimeOffset>(type: "datetimeoffset", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_PlantedPlants", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Products",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Name = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    QuickSellPrice = table.Column<int>(type: "int", nullable: false),
                    ProductionTimeAsSeconds = table.Column<int>(type: "int", nullable: false),
                    RewardXP = table.Column<int>(type: "int", nullable: false),
                    IsCrop = table.Column<bool>(type: "bit", nullable: false),
                    UnlockXP = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Products", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Quests",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    UserName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    QuestTypeId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Quests", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "QuestTypes",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Description = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    RewardMoney = table.Column<int>(type: "int", nullable: false),
                    RewardXP = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_QuestTypes", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "UserProduct",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    ProductName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    UserName = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    Quantity = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_UserProduct", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "Users",
                columns: table => new
                {
                    Id = table.Column<int>(type: "int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Email = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    UserXP = table.Column<int>(type: "int", nullable: false),
                    UserMoney = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Users", x => x.Id);
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Barns");

            migrationBuilder.DropTable(
                name: "BarnTypes");

            migrationBuilder.DropTable(
                name: "Classifieds");

            migrationBuilder.DropTable(
                name: "PlantedPlants");

            migrationBuilder.DropTable(
                name: "Products");

            migrationBuilder.DropTable(
                name: "Quests");

            migrationBuilder.DropTable(
                name: "QuestTypes");

            migrationBuilder.DropTable(
                name: "UserProduct");

            migrationBuilder.DropTable(
                name: "Users");
        }
    }
}

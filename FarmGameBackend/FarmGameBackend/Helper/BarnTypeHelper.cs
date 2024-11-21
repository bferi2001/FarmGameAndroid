using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace FarmGameBackend.Helper
{
    public class BarnTypeHelper
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public BarnTypeHelper(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }
        public async Task<List<BarnType>> GetBarnTypes()
        {
            return await _context.BarnTypes.ToListAsync();
        }

        public async Task<List<string>> GetBarnTypeNames()
        {
            var barnTypeNames = await _context.BarnTypes.Select(barnType => barnType.Name).ToListAsync();

            if (barnTypeNames == null)
            {
                throw new NotFoundException("");
            }

            return barnTypeNames;
        }

        public async Task<BarnType> GetBarnType(string barntype)
        {
            var barnType = await GetBarnTypeByName(barntype);

            if (barnType == null)
            {
                throw new NotFoundException("");
            }

            return barnType;
        }

        public async Task<string> GetBarnTypeName(string barntype)
        {
            var barnType = await GetBarnTypeByName(barntype);

            if (barnType == null)
            {
                throw new NotFoundException("");
            }

            return barnType.Name;
        }

        public async Task<int> GetBarnTypeCostByLevel(string barntype, int level)
        {
            var barnType = await GetBarnTypeByName(barntype);

            if (barnType == null)
            {
                throw new NotFoundException("");
            }
            switch (level)
            {
                case 1:
                    return barnType.FirstUpgradeCost;
                case 2:
                    return barnType.SecondUpgradeCost;
                case 3:
                    return barnType.ThirdUpgradeCost;
                default:
                    throw new NotFoundException("This level is not exists for this building.");
            }
        }

        public async Task<BarnType> GetBarnTypeByName(string barntype)
        {
            var product = await _context.BarnTypes.Where(bt => bt.Name == barntype).FirstOrDefaultAsync();
            if (product == null)
            {
                throw new NotFoundException("This barntype does not exists.");
            }
            return product;
        }

        private bool BarnTypeExists(int id)
        {
            return _context.BarnTypes.Any(e => e.Id == id);
        }
        public async Task<string> GetProductnameByBarntype(string barntype)
        {
            var product = await _context.BarnTypes.Where(bt => bt.Name == barntype).Select(bt => bt.ProductName).FirstOrDefaultAsync();
            if (product == null)
            {
                throw new NotFoundException("This barntype does not exists.");
            }
            return product;
        }
    }
}

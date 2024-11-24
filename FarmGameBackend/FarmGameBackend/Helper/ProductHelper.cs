using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using FarmGameBackend.CustomExceptions;

namespace FarmGameBackend.Helper
{
    public class ProductHelper
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public ProductHelper(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }
        public class MarketUserProduct
        {
            public string ProductName { get; set; }
            public int QuickSellPrice { get; set; }
            public int Quantity { get; set; }
        }
        public async Task<List<UserProduct>> GetUserProductsAsync()
        {
            List<UserProduct> userProducts = await _context.UserProduct
                .Where(up => up.UserName == _currentUser.Email).ToListAsync();
            return userProducts;
        }
        public bool UserProductExists(int id)
        {
            return _context.UserProduct.Any(e => e.Id == id);
        }
        public bool ProductExists(int id)
        {
            return _context.Products.Any(e => e.Id == id);
        }
        public Boolean DoesProductExists(string _productName)
        {
            return _context.Products.Any(product => product.Name == _productName);
        }
        public async Task<Product?> GetUnlockedCropByName(string name, User currentUser)
        {
            if (!_context.PlantHelper.DoesCroptypeExistsAndUnlocked(name, currentUser))
            {
                return null;
            }
            Product? productType = await _context.Products.Where(product => product.Name == name).FirstAsync();
            return productType;
        }
        public async Task<Product?> GetProductByName(string name)
        {
            Product? productType = await _context.Products.Where(product => product.Name == name).FirstOrDefaultAsync();
            if (productType == null)
            {
                throw new NotFoundException("");
            }
            return productType;
        }
        public async Task<Product?> GetUnlockedProductByName(string name)
        {
            Product? productType = await _context.Products.Where(product => product.Name == name && product.UnlockXP <= _currentUser.UserXP).FirstOrDefaultAsync();
            if (productType == null)
            {
                return null;
            }
            return productType;
        }
        public async Task<List<string>> GetUnlockedProductNames()
        {
            List<string> productType = await _context.Products.Where(product => product.UnlockXP <= _currentUser.UserXP).Select(product => product.Name).ToListAsync();
            if (productType == null)
            {
                return new List<string>();
            }
            return productType;
        }
        public async Task<UserProduct> PostUserProduct(UserProduct userProduct)
        {
            _context.UserProduct.Add(userProduct);
            await _context.SaveChangesAsync();

            return userProduct;

        }

        public async Task PutUserProduct(int id, UserProduct userProduct)
        {
            if (id != userProduct.Id)
            {
                throw new BadRequestException("Id not match for  modifying "+userProduct.Id);
            }

            _context.Entry(userProduct).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserProductExists(id))
                {
                    throw new NotFoundException("UserProduct does not exists with the id: " + id);
                }
                else
                {
                    throw;
                }
            }

            return;
        }
        public async Task<UserProduct?> GetUserProduct(string productName)
        {
            var userProduct = await _context.UserProduct.Where(userProduct => userProduct.UserName == _currentUser.Email && userProduct.ProductName == productName).FirstOrDefaultAsync();
            if (userProduct == null)
            {
                return null;
            }
            return userProduct;
        }
        public async Task AddUserProduct(string productName, int quantity)
        {
            if (!DoesProductExists(productName))
            {
                throw new NotFoundException("Product doesn't exists: " + productName);
            }
            UserProduct? userProduct = await GetUserProduct(productName);
            if (userProduct == null)
            {
                if (quantity < 0)
                {
                    throw new BadRequestException("Can't add new Product with negative value");
                }
                UserProduct product = new UserProduct
                {
                    ProductName = productName,
                    UserName = _currentUser.Email,
                    Quantity = quantity
                };
                await PostUserProduct(product);
                return;
            }
            else
            {
                // This is used in MarketController to subtract Products as well
                if(userProduct.Quantity - quantity < 0)
                {
                    throw new BadRequestException("Can't add new Product with negative value");
                }
                userProduct.Quantity += quantity;
                await PutUserProduct(userProduct.Id, userProduct);
                return;
            }
        }
        public List<string?> GetUnlockedMeatNames()
        {
            int userXP = _currentUser.UserXP;
            var productNames =  _context.Products.Where(product => product.Name.StartsWith("meat_") && product.UnlockXP <= userXP)
                                    .Select(product => product.Name)
                                    .ToList();

            if (!productNames.Any())
            {
                throw new NotFoundException("No unlocked meat.");
            }
            return productNames;
        }
    }
}

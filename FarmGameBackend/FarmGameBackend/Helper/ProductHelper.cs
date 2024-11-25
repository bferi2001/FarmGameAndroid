using FarmGameBackend.CustomExceptions;
using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using FarmGameBackend.CustomExceptions;

namespace FarmGameBackend.Helper
{
    public class ProductHelper(FarmApplicationContext context)
    {
        public class MarketUserProduct
        {
            public string ProductName { get; set; }
            public int QuickSellPrice { get; set; }
            public int Quantity { get; set; }
        }
        public async Task<List<UserProduct>> GetUserProductsAsync(User currentUser)
        {
            List<UserProduct> userProducts = await context.UserProduct
                .Where(up => up.UserName == currentUser.Email).ToListAsync();
            return userProducts;
        }
        public bool UserProductExists(int id)
        {
            return context.UserProduct.Any(e => e.Id == id);
        }
        public bool ProductExists(int id)
        {
            return context.Products.Any(e => e.Id == id);
        }
        public Boolean DoesProductExists(string _productName)
        {
            return context.Products.Any(product => product.Name == _productName);
        }
        public async Task<Product?> GetUnlockedCropByName(string name, User currentUser)
        {
            if (!context.PlantHelper.DoesCroptypeExistsAndUnlocked(name, currentUser))
            {
                return null;
            }
            Product? productType = await context.Products.Where(product => product.Name == name).FirstAsync();
            return productType;
        }
        public async Task<Product?> GetProductByName(string name)
        {
            Product? productType = await context.Products.Where(product => product.Name == name).FirstOrDefaultAsync();
            if (productType == null)
            {
                throw new NotFoundException("");
            }
            return productType;
        }
        public async Task<Product?> GetUnlockedProductByName(string name, User currentUser)
        {
            Product? productType = await context.Products.Where(product => product.Name == name && product.UnlockXP <= currentUser.UserXP).FirstOrDefaultAsync();
            if (productType == null)
            {
                return null;
            }
            return productType;
        }
        public async Task<List<string>> GetUnlockedProductNames(User currentUser)
        {
            List<string> productType = await context.Products.Where(product => product.UnlockXP <= currentUser.UserXP).Select(product => product.Name).ToListAsync();
            if (productType == null)
            {
                return new List<string>();
            }
            return productType;
        }
        public async Task<UserProduct> PostUserProduct(UserProduct userProduct)
        {
            context.UserProduct.Add(userProduct);
            await context.SaveChangesAsync();

            return userProduct;

        }

        public async Task PutUserProduct(int id, UserProduct userProduct)
        {
            if (id != userProduct.Id)
            {
                throw new BadRequestException("Id not match for  modifying "+userProduct.Id);
            }

            context.Entry(userProduct).State = EntityState.Modified;

            try
            {
                await context.SaveChangesAsync();
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
        public async Task<UserProduct?> GetUserProduct(string productName, User currentUser)
        {
            var userProduct = await context.UserProduct.Where(userProduct => userProduct.UserName == currentUser.Email && userProduct.ProductName == productName).FirstOrDefaultAsync();
            if (userProduct == null)
            {
                return null;
            }
            return userProduct;
        }
        public async Task AddUserProduct(string productName, int quantity, User currentUser)
        {
            if (!DoesProductExists(productName))
            {
                throw new NotFoundException("Product doesn't exists: " + productName);
            }
            UserProduct? userProduct = await GetUserProduct(productName, currentUser);
            if (userProduct == null)
            {
                if (quantity < 0)
                {
                    throw new BadRequestException("Can't add new Product with negative value");
                }
                UserProduct product = new UserProduct
                {
                    ProductName = productName,
                    UserName = currentUser.Email,
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
        public List<string?> GetUnlockedMeatNames(User currentUser)
        {
            int userXP = currentUser.UserXP;
            var productNames =  context.Products.Where(product => product.Name.StartsWith("meat_") && product.UnlockXP <= userXP)
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

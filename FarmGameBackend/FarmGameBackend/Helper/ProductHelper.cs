﻿using FarmGameBackend.CustomExceptions;
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
            _currentUser = new User
            {
                Id = 0,
                Email = "testemail@gmail.com",
                UserXP = 2000,
                UserMoney = 2000
            };
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
        private bool UserProductExists(int id)
        {
            return _context.UserProduct.Any(e => e.Id == id);
        }
        private bool ProductExists(int id)
        {
            return _context.Products.Any(e => e.Id == id);
        }
        private Boolean DoesProductExists(string _productName)
        {
            return _context.Products.Any(product => product.Name == _productName);
        }
        private async Task<Product?> GetUnlockedProductByName(string name)
        {
            if (!DoesCroptypeExistsAndUnlocked(name))
            {
                return null;
            }
            Product? productType = await _context.Products.Where(product => product.Name == name).FirstAsync();
            return productType;
        }
        private async Task<UserProduct> PostUserProduct(UserProduct userProduct)
        {
            _context.UserProduct.Add(userProduct);
            await _context.SaveChangesAsync();

            return userProduct;

        }

        private async Task PutUserProduct(int id, UserProduct userProduct)
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
        private async Task<UserProduct?> GetUserProduct(string productName)
        {
            var userProduct = await _context.UserProduct.Where(userProduct => userProduct.UserName == _currentUser.Email && userProduct.ProductName == productName).FirstOrDefaultAsync();
            if (userProduct == null)
            {
                return null;
            }
            return userProduct;
        }
        private async Task AddUserProduct(string productName, int quantity)
        {
            if (!DoesProductExists(productName))
            {
                throw new NotFoundException("Product doesn't exists: " + productName);
            }
            UserProduct? userProduct = await GetUserProduct(productName);
            if (userProduct == null)
            {
                UserProduct product = new UserProduct
                {
                    ProductName = productName,
                    UserName = _currentUser.Email,
                    Quantity = quantity
                };
                await PostUserProduct(product);
                return 
            }
            else
            {
                userProduct.Quantity += quantity;
                return await PutUserProduct(userProduct.Id, userProduct);
            }
        }
    }
}

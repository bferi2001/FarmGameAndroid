using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using FarmGameBackend.CustomExceptions;

namespace FarmGameBackend.Helper
{
    public class UserHelper
    {
        private readonly FarmApplicationContext _context;
        private readonly User _currentUser;
        public UserHelper(FarmApplicationContext context)
        {
            _context = context;
            //string? userEmail = HttpContext.Items["Email"]?.ToString();
            //_currentUser = _context.GetCurrentUser(userEmail!);
            _currentUser = _context.GetCurrentUser("testemail@gmail.com");
        }
        public async Task PutUser(int id, User user)
        {
            if (id != user.Id)
            {
                throw new BadRequestException("");
            }

            _context.Entry(user).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!UserExists(id))
                {
                    throw new NotFoundException("");
                }
                else
                {
                    throw;
                }
            }

            return;
        }
        private bool UserExists(int id)
        {
            return _context.Users.Any(e => e.Id == id);
        }
    }
}

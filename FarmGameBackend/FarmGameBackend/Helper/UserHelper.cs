using FarmGameBackend.DbContexts;
using FarmGameBackend.Entity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using FarmGameBackend.CustomExceptions;

namespace FarmGameBackend.Helper
{
    public class UserHelper(FarmApplicationContext context)
    {
        public async Task PutUser(int id, User user)
        {
            if (id != user.Id)
            {
                throw new BadRequestException("");
            }

            context.Entry(user).State = EntityState.Modified;

            try
            {
                await context.SaveChangesAsync();
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
            return context.Users.Any(e => e.Id == id);
        }
    }
}

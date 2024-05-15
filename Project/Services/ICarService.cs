using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;

public interface ICarService
{
    Task CreateCarAsync(Car car);
    Task<bool> UpdateCarAsync(int id, Car car);
    Task<bool> DeleteCarAsync(int id);
    Task<IEnumerable<Car>> GetCarsAsync();
    Task<IEnumerable<Car>> GetCarsPerFilterAsync(FilterDefinition<Car> filter);

}

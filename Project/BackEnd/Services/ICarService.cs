using MongoDB.Driver;

public interface ICarService
{
    Task CreateCarAsync(Car car);
    Task<bool> UpdateCarAsync(int id, Car car);
    Task<bool> DeleteCarAsync(int id);
    Task<IEnumerable<Car>> GetCarsPerFilterAsync(FilterDefinition<Car> filter);
    Task<Car> GetCarByIdAsync(int id);
    Task<bool> UpdateCarAvailabilityByIdAsync(int id, bool availability);
}

using Microsoft.Extensions.Logging;
using MongoDB.Driver;
using System.Threading.Tasks;

public class CarService : ICarService
{
    private readonly IMongoCollection<Car> _carCollection;
    private readonly ILogger<CarService> _logger;

    public CarService(IMongoCollection<Car> carCollection, ILogger<CarService> logger)
    {
        _carCollection = carCollection;
        _logger = logger;
    }

    public async Task CreateCarAsync(Car car)
    {
        try
        {
            _logger.LogInformation("Attempting to create car: {@Car}", car);
            if (_carCollection == null)
            {
                _logger.LogError("Car collection is null");
                return;
            }
            await _carCollection.InsertOneAsync(car);
            _logger.LogInformation("Car created successfully: {@Car}", car);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "An error occurred while creating the car");
            throw;
        }
    }
}

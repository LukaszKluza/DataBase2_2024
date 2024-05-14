using System.Threading.Tasks;

public interface ICarsModelsService
{
    Task CreateCarModelAsync(CarModel carModel);
    Task<bool> UpdateCarModelAsync(int id, CarModel carModel);
    Task<bool> DeleteCarModelAsync(int id);
}
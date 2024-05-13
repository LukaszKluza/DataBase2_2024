using System.Threading.Tasks;

public interface ICarService
{
    Task CreateCarAsync(Car car);
    Task<bool> DeleteCarAsync(int id);
    Task<bool> UpdateCarAsync(int id, Car car);
}

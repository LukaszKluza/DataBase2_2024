using Microsoft.AspNetCore.Mvc;
using System;
using System.Threading.Tasks;

[Route("api/[controller]")]
[ApiController]
public class CarController : ControllerBase
{
    private readonly ICarService _carService;

    public CarController(ICarService carService)
    {
        _carService = carService;
    }

    [HttpPost]
    public async Task<IActionResult> CreateCar([FromBody] Car car)
    {
        try
        {
            await _carService.CreateCarAsync(car);
            return Ok("Car created successfully.");
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"An error occurred while creating the car: {ex.Message}");
        }
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateCar(int id, [FromBody] Car car)
    {
        try
        {
            var success = await _carService.UpdateCarAsync(id, car);
            if (success)
            {
                return Ok($"Car with ID '{id}' updated successfully.");
            }
            else
            {
                return NotFound("Car not found.");
            }
           
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"An error occurred while updating the car: {ex.Message}");
        }
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteCar(int id)
    {
        try
        {
            var success = await _carService.DeleteCarAsync(id);
            if (success)
            {
                return Ok("Car deleted successfully.");
            }
            else
            {
                return NotFound("Car not found.");
            }
        }
        catch (Exception ex)
        {
            return StatusCode(500, $"An error occurred while deleting the car: {ex.Message}");
        }
    }
}

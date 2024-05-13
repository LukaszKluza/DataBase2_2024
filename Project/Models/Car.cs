using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

public class Car
{
    [BsonId]
    [BsonElement("_id")]
    public int Id { get; set; } 

    [BsonElement("_carModelId")]
    public int CarModelId { get; set; }

    [BsonElement("seats")]
    public int Seats { get; set; }

    [BsonElement("type")]
    public string Type { get; set; }

    [BsonElement("color")]
    public string Color { get; set; }

    [BsonElement("power")]
    public int Power { get; set; }

    [BsonElement("curr_mileage")]
    public int CurrentMileage { get; set; }

    [BsonElement("price_per_day")]
    public int PricePerDay { get; set; }

    [BsonElement("isAvailable")]
    public bool IsAvailable { get; set; }

    [BsonElement("production_year")]
    public int ProductionYear { get; set; }
}

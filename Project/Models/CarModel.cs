using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

public class CarModel
{
    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
    public string Id { get; set; }

    [BsonElement("mark")]
    public string? Mark { get; set; }

    [BsonElement("model")]
    public string? Model { get; set; }

    [BsonElement("type")]
    public string[]? Type { get; set; }
}

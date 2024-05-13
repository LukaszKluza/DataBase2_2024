using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;

public class Client
{
    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
    public string Id { get; set; }

    [BsonElement("first_name")]
    public string? FirstName { get; set; }

    [BsonElement("last_name")]
    public string? LastName { get; set; }

    [BsonElement("phone_number")]
    public string? PhoneNumber { get; set; }

    [BsonElement("gender")]
    public string? Gender { get; set; }

    [BsonElement("birth_day")]
    [BsonDateTimeOptions(DateOnly = true)]
    public DateTime? BirthDay { get; set; }

    [BsonElement("pesel")]
    public string? Pesel { get; set; }

    [BsonElement("email")]
    public string? Email { get; set; }

    [BsonElement("address")]
    public string? Address { get; set; }

    [BsonElement("city")]
    public string? City { get; set; }

    [BsonElement("country")]
    public string? Country { get; set; }

    [BsonElement("customer_since")]
    [BsonDateTimeOptions(DateOnly = true)]
    public DateTime? CustomerSince { get; set; }

    [BsonElement("total_rental_days")]
    public int? TotalRentalDays { get; set; }
}

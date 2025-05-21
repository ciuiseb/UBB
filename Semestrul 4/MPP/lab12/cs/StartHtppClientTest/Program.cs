using System;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Collections.Generic;
using Triathlon.Model;

namespace BasicTriathlonClient;

class Program
{
    private static readonly string BaseUrl = "http://localhost:8080/api/events";
    private static readonly HttpClient Client = new HttpClient();
    private static readonly JsonSerializerOptions JsonOptions = new JsonSerializerOptions
    {
        PropertyNamingPolicy = JsonNamingPolicy.CamelCase
    };

    static void Main(string[] args)
    {
        var testEvent = new Event
        {
            Name = "Test event",
            Date = "some date",
            Location = "some location",
            Description = "no description"
        };

        var id = 0L;
        Console.WriteLine("Creating new event...");
        try
        {
            Event createdEvent = CreateEvent(testEvent);
            id = createdEvent.Id;
            Console.WriteLine($"Created: {createdEvent.Id} - {createdEvent.Name}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error creating event: {ex.Message}");
        }
        

        

        Console.WriteLine($"\nUpdating event with ID {id}...");
        try
        {
            Event eventToUpdate = GetEventById(id);

            string originalName = eventToUpdate.Name;
            eventToUpdate.Name = "Updated Event Name";
            eventToUpdate.Description = "This description has been updated";

            Event updatedEvent = UpdateEvent(eventToUpdate);
            Console.WriteLine($"Updated event details: {updatedEvent.Id}: {updatedEvent.Name}\n" +
                              $"Date: {updatedEvent.Date}\n" +
                              $"Location: {updatedEvent.Location}\n" +
                              $"Description: {updatedEvent.Description}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error updating event with ID {id}: {ex.Message}");
        }
        Console.WriteLine("\nGetting all events...");
        try
        {
            List<Event> allEvents = GetAllEvents();
            foreach (var evt in allEvents)
            {
                Console.WriteLine($"{evt.Id}: {evt.Name} - {evt.Date} - {evt.Location}");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error getting all events: {ex.Message}");
        }

        Console.WriteLine($"\nGetting event with ID {id}...");
        try
        {
            Event retrievedEvent = GetEventById(id);
            Console.WriteLine($"{retrievedEvent.Id}: {retrievedEvent.Name}\n" +
                             $"Date: {retrievedEvent.Date}\n" +
                             $"Location: {retrievedEvent.Location}\n" +
                             $"Description: {retrievedEvent.Description}");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error getting event with ID {id}: {ex.Message}");
        }

        Console.WriteLine($"\nDeleting event with ID {id}...");
        try
        {
            DeleteEvent(id);
            Console.WriteLine("Event deleted successfully");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error deleting event with ID {id}: {ex.Message}");
        }
    }

    static Event CreateEvent(Event newEvent)
    {
        try
        {
            string json = JsonSerializer.Serialize(newEvent, JsonOptions);
            StringContent content = new StringContent(json, Encoding.UTF8, "application/json");
            
            HttpResponseMessage response = Client.PostAsync(BaseUrl, content).Result;
            
            string responseJson = response.Content.ReadAsStringAsync().Result;
            
            if (response.IsSuccessStatusCode)
            {
                return JsonSerializer.Deserialize<Event>(responseJson, JsonOptions);
            }
            else
            {
                throw new Exception($"HTTP {(int)response.StatusCode} - {response.StatusCode}: {responseJson}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Create event failed: {ex.Message}", ex);
        }
    }




    static List<Event> GetAllEvents()
    {
        try
        {
            HttpResponseMessage response = Client.GetAsync(BaseUrl).Result;
            
            string json = response.Content.ReadAsStringAsync().Result;
            
            if (response.IsSuccessStatusCode)
            {
                return JsonSerializer.Deserialize<List<Event>>(json, JsonOptions);
            }
            else
            {
                throw new Exception($"HTTP {(int)response.StatusCode} - {response.StatusCode}: {json}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Get all events failed: {ex.Message}", ex);
        }
    }

    static Event GetEventById(long id)
    {
        try
        {
            HttpResponseMessage response = Client.GetAsync($"{BaseUrl}/{id}").Result;
            
            string json = response.Content.ReadAsStringAsync().Result;
            
            if (response.IsSuccessStatusCode)
            {
                return JsonSerializer.Deserialize<Event>(json, JsonOptions);
            }
            else
            {
                throw new Exception($"HTTP {(int)response.StatusCode} - {response.StatusCode}: {json}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Get event by ID failed: {ex.Message}", ex);
        }
    }

    static void DeleteEvent(long id)
    {
        try
        {
            HttpResponseMessage response = Client.DeleteAsync($"{BaseUrl}/{id}").Result;
            
            string content = response.Content.ReadAsStringAsync().Result;
            
            if (!response.IsSuccessStatusCode)
            {
                throw new Exception($"HTTP {(int)response.StatusCode} - {response.StatusCode}: {content}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Delete event failed: {ex.Message}", ex);
        }
    }
    static Event UpdateEvent(Event eventToUpdate)
    {
        try
        {
            string json = JsonSerializer.Serialize(eventToUpdate, JsonOptions);
            StringContent content = new StringContent(json, Encoding.UTF8, "application/json");

            HttpResponseMessage response = Client.PutAsync($"{BaseUrl}/{eventToUpdate.Id}", content).Result;

            string responseJson = response.Content.ReadAsStringAsync().Result;

            if (response.IsSuccessStatusCode)
            {
                return JsonSerializer.Deserialize<Event>(responseJson, JsonOptions);
            }
            else
            {
                throw new Exception($"HTTP {(int)response.StatusCode} - {response.StatusCode}: {responseJson}");
            }
        }
        catch (Exception ex)
        {
            throw new Exception($"Update event failed: {ex.Message}", ex);
        }
    }
}
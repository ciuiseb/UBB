namespace lab10;

public interface IContainer
{
    // Elimină și returnează un element din colecție
    Task Remove();
 
    // Adaugă un element în colecție
    void Add(Task task);
 
    // Returnează numărul de elemente din colecție
    int Size();
 
    // Verifică dacă colecția este goală
    bool IsEmpty();
}
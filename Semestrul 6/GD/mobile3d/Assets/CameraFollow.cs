using UnityEngine;

public class CameraFollow : MonoBehaviour
{
    public Transform player; // Aici vom lega mingea
    private Vector3 offset;  // Distanța menținută între cameră și minge

    void Start()
    {
        // Calculează distanța inițială dintre cameră și minge ca să o mențină mereu
        if (player != null)
        {
            offset = transform.position - player.position;
        }
    }

    // Folosim LateUpdate pentru cameră ca să se miște DUPĂ ce mingea s-a mișcat
    void LateUpdate()
    {
        if (player != null)
        {
            transform.position = player.position + offset;
        }
    }
}
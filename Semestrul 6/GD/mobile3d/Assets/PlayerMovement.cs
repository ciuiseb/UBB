using UnityEngine;
using UnityEngine.SceneManagement;

public class PlayerMovement : MonoBehaviour
{
    public float speed = 10f; 
    private Rigidbody rb;
    public GameObject winText; 
    
    // NOU: O variabilă care va ține minte câte cuburi mai avem
    private int coinsLeft;

    void Start()
    {
        rb = GetComponent<Rigidbody>();
        
        if (winText != null)
        {
            winText.SetActive(false);
        }

        // Când dăm Play, Unity numără automat câte cuburi cu tag-ul "Coin" există în scenă
        coinsLeft = GameObject.FindGameObjectsWithTag("Obiectiv").Length;
    }

    void FixedUpdate()
    {
        float moveHorizontal = Input.GetAxis("Horizontal");
        float moveVertical = Input.GetAxis("Vertical");

        Vector3 movement = new Vector3(moveHorizontal, 0.0f, moveVertical);
        rb.AddForce(movement * speed);

        if (transform.position.y < -5f)
        {
            RestartGame();
        }
    }

    void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("Obiectiv"))
        {
            // Când lovim un cub, îl distrugem...
            Destroy(collision.gameObject);
            
            // ...și scădem 1 din numărul total de cuburi rămase!
            coinsLeft--;

            // Dacă numărul a ajuns la 0, am câștigat!
            if (coinsLeft <= 0)
            {
                WinGame();
            }
        }
    }

    void WinGame()
    {
        // Afișăm textul și oprim timpul
        if (winText != null)
        {
            winText.SetActive(true);
        }
        Time.timeScale = 0f; 
    }

    void RestartGame()
    {
        Time.timeScale = 1f; 
        SceneManager.LoadScene(SceneManager.GetActiveScene().name);
    }
}
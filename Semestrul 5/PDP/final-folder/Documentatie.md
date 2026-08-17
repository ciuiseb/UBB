## 1. Mediu de Lucru

* **Limbaj:** Java (JDK 17+)
* **IDE:** IntelliJ IDEA
* **Build System:** Gradle

## 2. Parametrii de Configurare

Configurarea se face în fișierul `files/config.txt`.

| Parametru | Descriere | Valoare Curentă |
| :--- | :--- | :--- |
| **Server** | | |
| `server.port` | Portul TCP de ascultare. | `5000` |
| `server.shows_file` | Calea fișierului cu spectacole. | `files/spectacole.txt` |
| `server.pool_size` | Nr. thread-uri procesare server. | `10` |
| `server.t_max` | Timp expirare rezervare (sec). | `10` |
| `server.audit_interval`| Interval verificare auditor (sec). | `10` |
| **Testare** | | |
| `client.count` | Nr. clienți simulați simultan. | `10` |
| `test.running-time` | Durata rulării testului (sec). | `180` |

## 3. Cum rulez

Testarea este realizată prin clasa `TestClass`. Aceasta pornește Serverul și Clienții pe baza configurației.

1.  **Încărcare Configurare:** Citește fișierul `config.txt` și parsează parametrii pentru Server și Client.
2.  **Pornire Server:** Lansează logica serverului (`StartServer`) pe un fir de execuție separat, transmițându-i parametrii citiți.
3.  **Delay:** Așteaptă 2 secunde pentru a fi siguri de inițializarea completă a Serverului (deschiderea portului).
4.  **Inițializare Clienti:** Creează un `ExecutorService` cu dimensiunea `client.count` pentru gestionarea concurentă a clienților.
5.  **Generare Trafic:** Într-o buclă, pe durata `test.running-time`:
    * Trimite un nou task `Client` către pool.
    * Așteaptă 2 secunde între cereri.
6.  **Final:** La expirarea timpului, oprește forțat pool-ul de clienți și închide aplicația.
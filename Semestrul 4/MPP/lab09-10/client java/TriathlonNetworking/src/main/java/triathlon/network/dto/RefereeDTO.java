package triathlon.network.dto;

import java.io.Serializable;

public class RefereeDTO implements Serializable {
    private Long id;
    private String username;
    private String password;

    public RefereeDTO() {}

    public RefereeDTO(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
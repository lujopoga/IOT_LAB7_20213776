package pe.pucp.lab7iot;

public class User {
    private String dni;
    private String email;


    public User(String dni, String email) {
        this.dni = dni;
        this.email = email;
    }


    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
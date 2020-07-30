import java.awt.*;

public class Case {
    private Point position;
    private Pion pion;


    public Case(Point position, Pion pion) {
        this.position = position;
        this.pion = pion;
    }

    public Point getPosition() {
        return this.position;
    }

    public Pion getPion() {
        return pion;
    }

    public void setPion(Pion pion){
        this.pion = pion;
    }
}

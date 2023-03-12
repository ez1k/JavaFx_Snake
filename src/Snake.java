public class Snake{
private int x;
private int y;


    public Snake(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void MoveRight()
    {
        x++;
    }
    public void MoveLeft()
    {
        x--;
    }
    public void MoveUp()
    {
        y--;
    }
    public void MoveDown()
    {
        y++;
    }

}

package se.ju.taun15a16.group5.mjilkmjecipes.backend;

/**
 * Created by Fco on 06/12/2016.
 */

public class Direction {
    private int id;
    private int order;
    private String description;

    public Direction(){}

    public Direction(int id, int order, String description) {
        this.id = id;
        this.order = order;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "order=" + order +
                ", description='" + description + '\'' +
                '}';
    }
}
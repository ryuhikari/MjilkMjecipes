package se.ju.taun15a16.group5.mjilkmjecipes.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fernando on 2016-11-29.
 */

public class Recipe {

    class Direction {

        private int order;
        private String description;

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

    private String name;
    private String description;
    private String creatorId;
    private ArrayList<Direction> directions;
    private String image;
    private int created;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public ArrayList<Direction> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<Direction> directions) {
        this.directions = directions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "created=" + created +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", directions=" + directions +
                ", image='" + image + '\'' +
                '}';
    }
}

package se.ju.taun15a16.group5.mjilkmjecipes.backend;

import java.util.ArrayList;

/**
 * Created by Fernando on 2016-11-29.
 */

public class Recipe {

    private long id;
    private String name;
    private String description;
    private String creatorId;
    private ArrayList<Direction> directions;
    private String image;
    private long created;

    public Recipe(){}

    public Recipe(long id, String name, String description, String creatorId, ArrayList<Direction> directions, String image, long created) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.directions = directions;
        this.image = image;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", directions=" + directions +
                ", image='" + image + '\'' +
                ", created=" + created +
                '}';
    }
}

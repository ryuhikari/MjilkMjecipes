package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

class ListModel {

    private  String recipeImage = "";
    private  String recipeName = "";
    private  int recipeRating = 0;

    /*********** Set Methods ******************/

    void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    void setRecipeRating(int recipeRating) {
        this.recipeRating = recipeRating;
    }

    /*********** Get Methods ****************/

    String getRecipeImage() {
        return recipeImage;
    }

    String getRecipeName() {
        return recipeName;
    }

    int getRecipeRating() {
        return recipeRating;
    }
}

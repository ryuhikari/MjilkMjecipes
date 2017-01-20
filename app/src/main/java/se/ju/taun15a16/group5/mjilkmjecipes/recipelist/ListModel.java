package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

class ListModel {

    private  String recipeImage = "";
    private  String recipeName = "";
    private  String recipeAuthor = "";
    private  int recipeRating = 0;

    /*********** Set Methods ******************/

    void setRecipeAuthor(String recipeAuthor) {
        this.recipeAuthor = recipeAuthor;
    }

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

    String getRecipeAuthor() {
        return recipeAuthor;
    }

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

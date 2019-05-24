package reflection.baeldung;

public abstract class Animal {
    public static String CATEGORY = "domestic";
    private String name;

    protected abstract String getSound();

    // constructor, standard getters and setters omitted
}

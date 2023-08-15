package me.simzahn.spigotmovieplayer.movie;

public class Resolution {

    private final int width;
    private final int height;

    /**
     * Creates a new Resolution object with the given width and height.
     * @param width How wide the screen is.
     * @param height How high the screen is.
     */
    public Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Resolution(String resolution) {
        String[] split = resolution.split("x");
        this.width = Integer.parseInt(split[0]);
        this.height = Integer.parseInt(split[1]);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }

    public boolean equals(Resolution resolution) {
        return this.width == resolution.getWidth() && this.height == resolution.getHeight();
    }
}

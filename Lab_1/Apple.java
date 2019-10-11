package Lab_1;

public class Apple extends Food {

    private String size;

    public Apple(String size) {
        super("Яблоко");
        this.size = size;
    }

    @Override
    public void consume() {
        System.out.println(this + " съедено");
    }

    @Override
    public int calculateCalories(){
        if(size.toUpperCase().startsWith("МАЛ"))
            return 46000;
        if(size.toUpperCase().startsWith("БОЛЬШ"))
            return 100000;
        if(size.toUpperCase().startsWith("СРЕДН"))
            return 70000;
        System.out.println("Unknown size of Apple. Returned 0");
        return 0;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
            if (!(arg0 instanceof Apple)) return false;
            return size.equals(((Apple)arg0).size);
        } else
            return false;
    }

    @Override
    public String toString() {
        return super.toString() + " размера '" + size.toUpperCase() + "'";
    }
}

package Lab_1;

public class Coctail extends Food{

    private String drink = null;
    private String fruit = null;

    public Coctail (String drink, String fruit) {
        super("Коктейль");
        this.drink = drink;
        this.fruit = fruit;
    }

    @Override
    public void consume() {
        System.out.println(this + " выпит");
    }

    @Override
    public int calculateCalories(){
        return 100000;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getFruit() {
        return fruit;
    }

    public void setFruit(String fruit) {
        this.fruit = fruit;
    }

    @Override
    public boolean equals(Object arg0) {
        if (super.equals(arg0)) {
            if (!(arg0 instanceof Coctail)) return false;
            return drink.equals(((Coctail)arg0).drink) && fruit.equals(((Coctail)arg0).fruit);
        } else
            return false;
    }

    @Override
    public String toString() {
        return super.toString() + " из '" + fruit.toUpperCase() + "' c '" + drink.toUpperCase() + "'";
    }
}

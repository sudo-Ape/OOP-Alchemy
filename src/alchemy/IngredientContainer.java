package alchemy;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

public class IngredientContainer {
    private int capacity = 0;
    private int content = 0;

    /**
     * Sets the capacity of the container to the given capacity
     *
     * @param capacity
     *        capacity of the container
     */
    @Basic @Raw
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Sets the content of te
     * @param content
     */
    @Basic @Raw
    public void setContent(int content) {
        this.getContent(content);
    }

    /**
     * Return the current capacity of the container
     *
     * @return capacity of the container
     */
    @Basic @Raw
    public int getCapacity() {
        return capacity;
    }

    /**
     * Return the current contents of the container
     *
     * @return current content
     */
    @Basic @Raw
    public int getContent() {
        return content;
    }

    /**
     * Adds given amount to current content
     *
     * @effect if the sum of the current content and the amount exceeds the capacity,
     *         the content is set to the capacity of the container.
     *      | if (amount + getContent() > getCapacity())
     *      |   then setContent(getCapacity())
     *
     * @effect If the sum of the current content and the amount does not exceed the capacity,
     *         the content is increased by the given amount.
     *      | if (amount + getContent() <= getCapacity())
     *      |   then setContent(getContent() + amount)
     *
     * @post If the amount is not positive, the content remains unchanged.
     *      | if (amount <= 0)
     *      |   then new.getContent() == getContent()
     *
     * @param amount
     *        Amount added to the content of the oil tank
     */
    public void add(int amount){
        if (amount > 0) {
            if (amount + getContent() > getCapacity()) {
                setContent(getCapacity());
            } else {
                setContent(getContent() + amount);
            }
        }
    }

}

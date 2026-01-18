public class Session
{
    private String orderType; // Dine-in or Takeaway
    private String identifier; // Table number or phone number
    private Cart cart; //Shopping cart to add foods/drinks
    private String paymentMethod; // Cash or Card Payment
    private double totalAmount; //Total payment made
    
    public String getPaymentMethod() 
    {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) 
    {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() 
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public Session(String orderType, String identifier) {
        this.orderType = orderType;
        this.identifier = identifier;
        this.cart = new Cart(); //New cart for each session
    }

    //Getters
    public String getOrderType() 
    {
        return orderType;
    }

    
    public String getIdentifier() 
    {
        return identifier;
    }

    public Cart getCart() 
    {
        return cart;
    }
}
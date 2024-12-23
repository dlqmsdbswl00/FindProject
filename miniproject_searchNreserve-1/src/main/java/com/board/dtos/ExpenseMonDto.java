package com.board.dtos;

public class ExpenseMonDto {
	
    private int coffeeTotal;
    private int cigaretteTotal;
    private int taxiTotal;
    private int amountTotal; 

    
    public int getCoffeeTotal() {
        return coffeeTotal;
    }

    public void setCoffeeTotal(int coffeeTotal) {
        this.coffeeTotal = coffeeTotal;
    }

    public int getCigaretteTotal() {
        return cigaretteTotal;
    }

    public void setCigaretteTotal(int cigaretteTotal) {
        this.cigaretteTotal = cigaretteTotal;
    }

    public int getTaxiTotal() {
        return taxiTotal;
    }

    public void setTaxiTotal(int taxiTotal) {
        this.taxiTotal = taxiTotal;
    }

    public int getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(int amountTotal) {
        this.amountTotal = amountTotal;
    }

    

	@Override
	public String toString() {
		return "ExpenseMonDto [coffeeTotal=" + coffeeTotal + ", cigaretteTotal=" + cigaretteTotal + ", taxiTotal="
				+ taxiTotal + ", amountTotal=" + amountTotal + "]";
	}
    
}


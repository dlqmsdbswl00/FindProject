package com.board.dtos;

public class ExpenseMonDto {

	private int coffeeTotal = 0;
	private int cigaretteTotal = 0;
	private int taxiTotal = 0;
	private int amountTotal = 0;

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

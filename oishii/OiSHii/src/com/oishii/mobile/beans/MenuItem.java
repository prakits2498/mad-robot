package com.oishii.mobile.beans;


public class MenuItem extends BitmapBean{
	int id;
	String name;
	String image;
	String description;
	int itemsRemain;
	float price;
	MenuItemCategory category;
	
	public MenuItemCategory getCategory() {
		return category;
	}
	public void setCategory(MenuItemCategory category) {
		this.category = category;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description.trim();
	}
	public int getItemsRemain() {
		return itemsRemain;
	}
	public void setItemsRemain(int itemsRemain) {
		this.itemsRemain = itemsRemain;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "MenuItem [id=" + id + ", name=" + name + ", image=" + image
				+ ", description=" + description + ", itemsRemain="
				+ itemsRemain + ", price=" + price + "]";
	}
	
	

}

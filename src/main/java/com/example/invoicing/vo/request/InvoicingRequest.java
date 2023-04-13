package com.example.invoicing.vo.request;

import java.util.List;

import com.example.invoicing.entity.Invoicing;

public class InvoicingRequest {

	public List<Invoicing> isbnList;
	
	public List<SalesRequest> salesList;

	public String isbn;

	public String book;

	public String author;

	public List<String> categories;

	public String category;

	public Integer price;

	public Integer purchase;

	public InvoicingRequest() {

	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public List<Invoicing> getIsbnList() {
		return isbnList;
	}

	public void setIsbnList(List<Invoicing> isbnList) {
		this.isbnList = isbnList;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getPurchase() {
		return purchase;
	}

	public void setPurchase(Integer purchase) {
		this.purchase = purchase;
	}

	public List<SalesRequest> getSalesList() {
		return salesList;
	}

	public void setSalesList(List<SalesRequest> salesList) {
		this.salesList = salesList;
	}
	
	

}

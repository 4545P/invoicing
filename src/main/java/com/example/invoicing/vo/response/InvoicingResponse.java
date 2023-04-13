package com.example.invoicing.vo.response;

import java.util.List;

import com.example.invoicing.entity.Invoicing;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoicingResponse {

	private List<Invoicing> isbnList;

	private String isbn;

	private String book;

	private String author;

	private List<String> category;

	private Integer price;

	private Integer stock;

	private Integer sell;

	private Integer num;

	private Integer total;

	private String message;

	public InvoicingResponse() {

	}

	public InvoicingResponse(String isbn, String book, String author, Integer price) {
		super();
		this.isbn = isbn;
		this.book = book;
		this.author = author;
		this.price = price;
	}

	public InvoicingResponse(List<Invoicing> isbnList, String message) {
		this.isbnList = isbnList;
		this.message = message;
	}

	public List<Invoicing> getIsbnList() {
		return isbnList;
	}

	public void setIsbnList(List<Invoicing> isbnList) {
		this.isbnList = isbnList;
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
		return category;
	}

	public void setCategories(List<String> categories) {
		this.category = categories;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getSell() {
		return sell;
	}

	public void setSell(Integer sell) {
		this.sell = sell;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

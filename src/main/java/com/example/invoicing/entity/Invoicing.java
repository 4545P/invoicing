package com.example.invoicing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "invoicing")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Invoicing {

	@Id // 表示主鍵
	@Column(name = "isbn")
	private String isbn;

	// 書名
	@Column(name = "book")
	private String book;

	// 作者
	@Column(name = "author")
	private String author;

	// 分類
	@Column(name = "category")
	private String category;

	// 價格
	@Column(name = "price")
	private int price;

	// 庫存
	@Column(name = "stock")
	private Integer stock;

	// 銷貨
	@Column(name = "sell")
	private Integer sell;

	public Invoicing() {
		super();
	}

	public String getIsbn() {
		return isbn;
	}

	public Invoicing(String isbn, String book, String author, int price, int inventory, int sell) {
		super();
		this.isbn = isbn;
		this.book = book;
		this.author = author;
		this.price = price;
		this.stock = inventory;
		this.sell = sell;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
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

}

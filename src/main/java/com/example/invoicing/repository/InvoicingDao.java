package com.example.invoicing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.invoicing.entity.Invoicing;

@Repository
public interface InvoicingDao extends JpaRepository<Invoicing, String> {

	// 分類搜尋
	List<Invoicing> findByCategoryContaining(String category);

	// 客戶搜尋
	List<Invoicing> findByIsbn(String isbn);

	List<Invoicing> findByBook(String book);

	List<Invoicing> findByAuthor(String author);

	// 排行榜
	List<Invoicing> findTop5ByOrderBySellDesc();
	
}

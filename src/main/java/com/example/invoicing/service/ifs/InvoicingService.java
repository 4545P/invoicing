package com.example.invoicing.service.ifs;

import java.util.List;

import com.example.invoicing.entity.Invoicing;
import com.example.invoicing.vo.request.InvoicingRequest;
import com.example.invoicing.vo.response.InvoicingResponse;


public interface InvoicingService {

	// 新增書籍
	public InvoicingResponse addBook(InvoicingRequest invoicingRequest);

	// 修改分類
	List<Invoicing> updateCategory(String isbn, List<String> categories);

	// 分類搜尋
	public List<Invoicing> findByCategoryContaining(String category);

	// 客戶搜尋
	public List<Invoicing> search(String isbn, String book, String author);

	// 商店搜尋
	public List<Invoicing> searchForShop(String isbn, String book, String author);

	// 進貨
	public InvoicingResponse purchase(String isbn, Integer purchase);

	// 更新價格
	public InvoicingResponse renew(String isbn, Integer price);

	// 銷貨
	public List<InvoicingResponse> sales(InvoicingRequest invoicingRequest);

	// 排行榜
	public List<Object[]> findTop5ByOrderBySellDesc();

}

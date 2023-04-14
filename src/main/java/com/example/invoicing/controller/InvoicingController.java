package com.example.invoicing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.invoicing.service.ifs.InvoicingService;
import com.example.invoicing.vo.request.InvoicingRequest;
import com.example.invoicing.vo.response.InvoicingResponse;

@RestController
public class InvoicingController {

	@Autowired
	private InvoicingService invoicingService;

	//新增書籍
	@PostMapping("/add_book")
	public InvoicingResponse addBook(@RequestBody InvoicingRequest invoicingRequest) {
		return invoicingService.addBook(invoicingRequest);
	}
	
	//修改分類
	@PostMapping("/update_category")
	public InvoicingResponse updateCategory(@RequestBody InvoicingRequest invoicingRequest) {
	    return invoicingService.updateCategory(invoicingRequest.getIsbn(), invoicingRequest.getCategories());
	}

	//分類搜尋
	@PostMapping("/categories_search")
	public InvoicingResponse findByCategoryContaining(@RequestBody InvoicingRequest invoicingRequest) {
	    return invoicingService.findByCategoryContaining(invoicingRequest.getCategory());
	}
	
	//客戶搜尋
	@PostMapping("/search")
	public InvoicingResponse search(@RequestBody InvoicingRequest invoicingRequestr) {
		return invoicingService.search(invoicingRequestr.getIsbn(), invoicingRequestr.getBook(), invoicingRequestr.getAuthor());
	}

	//店家搜尋
	@PostMapping("/search_shop")
	public InvoicingResponse searchForShop(@RequestBody InvoicingRequest invoicingRequestr) {
		return invoicingService.searchForShop(invoicingRequestr.getIsbn(), invoicingRequestr.getBook(), invoicingRequestr.getAuthor());
	}

	//進貨
	@PostMapping("/purchase")
	public InvoicingResponse purchase(@RequestBody InvoicingRequest invoicingRequest) {
		return invoicingService.purchase(invoicingRequest.getIsbn() , invoicingRequest.getPurchase());
	}

	//更新價格
	@PostMapping("/renew")
	public InvoicingResponse renew(@RequestBody InvoicingRequest invoicingRequest) {
		return invoicingService.renew(invoicingRequest.getIsbn() , invoicingRequest.getPrice());
	}

	//銷售
	@PostMapping("/sales")
	public InvoicingResponse sales(@RequestBody InvoicingRequest invoicingRequests) {
		return invoicingService.sales(invoicingRequests);
	}

	//排行榜
	@PostMapping("/top5")
	public List<Object[]> findTop5BySalesOrderBySellDesc() {
		return invoicingService.findTop5ByOrderBySellDesc();
	}
	
}

package com.example.invoicing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.invoicing.entity.Invoicing;
import com.example.invoicing.service.ifs.InvoicingService;
import com.example.invoicing.vo.request.InvoicingRequest;
import com.example.invoicing.vo.response.InvoicingResponse;

@RestController
public class InvoicingController {

	@Autowired
	private InvoicingService invoicingService;

	@PostMapping("/add_book")
	public InvoicingResponse addBook(@RequestBody InvoicingRequest invoicingRequest) {
		return invoicingService.addBook(invoicingRequest);
	}
	
	@PostMapping("/update_category")
	public List<Invoicing> updateCategory(@RequestBody InvoicingRequest invoicingRequest) {
	    return invoicingService.updateCategory(invoicingRequest.getIsbn(), invoicingRequest.getCategories());
	}

	@PostMapping("/categories_search")
	public List<Invoicing> findByCategoryContaining(@RequestBody InvoicingRequest invoicingRequest) {
	    return invoicingService.findByCategoryContaining(invoicingRequest.getCategory());
	}
	
	@PostMapping("/search")
	public List<Invoicing> search(@RequestBody InvoicingRequest invoicingRequestr) {
		return invoicingService.search(invoicingRequestr.getIsbn(), invoicingRequestr.getBook(), invoicingRequestr.getAuthor());
	}

	@PostMapping("/search_shop")
	public List<Invoicing> searchForShop(@RequestBody InvoicingRequest invoicingRequestr) {
		return invoicingService.searchForShop(invoicingRequestr.getIsbn(), invoicingRequestr.getBook(), invoicingRequestr.getAuthor());
	}

	@PostMapping("/purchase")
	public InvoicingResponse purchase(@RequestBody InvoicingRequest invoicingRequest) {
		return invoicingService.purchase(invoicingRequest.getIsbn() , invoicingRequest.getPurchase());
	}

	@PostMapping("/renew")
	public InvoicingResponse renew(@RequestBody InvoicingRequest invoicingRequest) {
		return invoicingService.renew(invoicingRequest.getIsbn() , invoicingRequest.getPrice());
	}

	@PostMapping("/sales")
	public List<InvoicingResponse> sales(@RequestBody InvoicingRequest invoicingRequests) {
		return invoicingService.sales(invoicingRequests);
	}

	@PostMapping("/top5")
	public List<Object[]> findTop5BySalesOrderBySellDesc() {
		return invoicingService.findTop5ByOrderBySellDesc();
	}
}

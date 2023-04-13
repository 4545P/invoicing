package com.example.invoicing.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.invoicing.entity.Invoicing;
import com.example.invoicing.repository.InvoicingDao;
import com.example.invoicing.service.ifs.InvoicingService;
import com.example.invoicing.vo.request.InvoicingRequest;
import com.example.invoicing.vo.request.SalesRequest;
import com.example.invoicing.vo.response.InvoicingResponse;

@Service
public class InvoicingServiceImpl implements InvoicingService {

	@Autowired
	private InvoicingDao invoicingDao;

	@Transactional
	@Override
	public InvoicingResponse addBook(InvoicingRequest invoicingRequest) {
		List<Invoicing> isbnList = invoicingRequest.getIsbnList();
		List<Invoicing> errorList = new ArrayList<>();

		if (CollectionUtils.isEmpty(isbnList)) {
			throw new IllegalArgumentException("Order list cannot be empty");
		}

		for (Invoicing isbn : isbnList) {
			if (isbn.getIsbn().isBlank() || isbn.getBook().isBlank() || isbn.getAuthor().isBlank()
					|| isbn.getCategory().isEmpty()) {
				errorList.add(isbn);
			}
			Optional<Invoicing> existingIsbn = invoicingDao.findById(isbn.getIsbn());
			if (existingIsbn.isPresent()) {
				errorList.add(isbn);
			}
		}

		if (!errorList.isEmpty()) {
			return new InvoicingResponse(errorList, "ISBN重複或欄位為空");
		}
		invoicingDao.saveAll(isbnList);
		return new InvoicingResponse(isbnList, "書籍新增成功");
	}

	@Transactional
	@Override
	public List<Invoicing> updateCategory(String isbn, List<String> newCategories) {
		Optional<Invoicing> existingBook = invoicingDao.findById(isbn);
		if (!existingBook.isPresent()) {
			throw new IllegalArgumentException("修改錯誤,請檢查參數");
		}

		Invoicing book = existingBook.get();
		String categoriesStr = book.getCategory();
//	    List<String> categoriesList = Arrays.asList(categoriesStr.split(","));
		String[] categories = categoriesStr.split(",");
		List<String> categoriesList = new ArrayList<>();
		for (String item : categories) {
			categoriesList.add(item);
		}
		for (String category : newCategories) {
			if (!categoriesList.contains(category)) {
				categoriesList.add(category);
			}
		}
		String newCategoriesStr = String.join(",", categoriesList);
		book.setCategory(newCategoriesStr);
		invoicingDao.save(book);
		return Arrays.asList(book);
	}

	@Transactional
	@Override
	public List<Invoicing> findByCategoryContaining(String category) {
		if (category == null || category.trim().isEmpty()) {
			throw new IllegalArgumentException("Category cannot be null or empty.");
		}
		List<Invoicing> searchResult = invoicingDao.findByCategoryContaining(category);

		if (searchResult.isEmpty()) {
			throw new IllegalArgumentException("類別不存在");
		}
		List<Invoicing> scarch = invoicingDao.findByCategoryContaining(category);
		List<Invoicing> results = new ArrayList<>();
		for (Invoicing invoicing : scarch) {
			Invoicing result = new Invoicing();
			result.setIsbn(invoicing.getIsbn());
			result.setBook(invoicing.getBook());
			result.setAuthor(invoicing.getAuthor());
			result.setPrice(invoicing.getPrice());
			result.setStock(invoicing.getStock());
			results.add(result);
		}
		return results;
	}

	@Transactional
	@Override
	public List<Invoicing> search(String isbn, String book, String author) {
		List<Invoicing> searchResult;
		if (StringUtils.hasText(isbn)) {
			searchResult = invoicingDao.findByIsbn(isbn);
		} else if (StringUtils.hasText(book)) {
			searchResult = invoicingDao.findByBook(book);
		} else if (StringUtils.hasText(author)) {
			searchResult = invoicingDao.findByAuthor(author);
		} else {
			// 如果三個參數都為空，則回傳錯誤訊息
			throw new IllegalArgumentException("搜尋錯誤, 請至少提供一個參數: isbn, book, author.");
		}

		List<Invoicing> results = new ArrayList<>();
		for (Invoicing invoicing : searchResult) {
			Invoicing result = new Invoicing();
			result.setIsbn(invoicing.getIsbn());
			result.setBook(invoicing.getBook());
			result.setAuthor(invoicing.getAuthor());
			result.setPrice(invoicing.getPrice());
			results.add(result);
		}
		return results;
	}

	@Transactional
	@Override
	public List<Invoicing> searchForShop(String isbn, String book, String author) {
		List<Invoicing> searchResult;
		if (StringUtils.hasText(isbn)) {
			searchResult = invoicingDao.findByIsbn(isbn);
		} else if (StringUtils.hasText(book)) {
			searchResult = invoicingDao.findByBook(book);
		} else if (StringUtils.hasText(author)) {
			searchResult = invoicingDao.findByAuthor(author);
		} else {
			// 如果三個參數都為空，則回傳錯誤訊息
			throw new IllegalArgumentException("搜尋錯誤,請檢查參數");
		}

		List<Invoicing> results = new ArrayList<>();
		for (Invoicing invoicing : searchResult) {
			Invoicing result = new Invoicing();
			result.setIsbn(invoicing.getIsbn());
			result.setBook(invoicing.getBook());
			result.setAuthor(invoicing.getAuthor());
			result.setPrice(invoicing.getPrice());
			result.setStock(invoicing.getStock());
			result.setSell(invoicing.getSell());
			results.add(result);
		}
		return results;
	}

	@Transactional
	@Override
	public InvoicingResponse purchase(String isbn, Integer purchase) {
		Optional<Invoicing> isbnOp = invoicingDao.findById(isbn);
		if (!isbnOp.isPresent()) {
			throw new IllegalArgumentException("ISBN錯誤");
		}

		Invoicing invoicing = isbnOp.get();
		if (isbn.equals(invoicing.getIsbn())) {
			Integer stock = invoicing.getStock();
			if (stock == null) {
				stock = 0;
			}
			stock = purchase + invoicing.getStock();
			invoicing.setStock(stock);
			invoicingDao.save(invoicing);
		}
		InvoicingResponse result = new InvoicingResponse();
		result.setIsbn(invoicing.getIsbn());
		result.setBook(invoicing.getBook());
		result.setAuthor(invoicing.getAuthor());
		result.setPrice(invoicing.getPrice());
		result.setStock(invoicing.getStock());
		return result;
	}

	@Transactional
	@Override
	public InvoicingResponse renew(String isbn, Integer price) {
		Optional<Invoicing> isbnOp = invoicingDao.findById(isbn);
		if (!isbnOp.isPresent()) {
			throw new IllegalArgumentException("ISBN錯誤");
		}

		Invoicing invoicing = isbnOp.get();
		if (isbn.equals(invoicing.getIsbn()) && price > 0) {
			int newPrice = (price);
			invoicing.setPrice(newPrice);
			invoicingDao.save(invoicing);
		}
		InvoicingResponse result = new InvoicingResponse();
		result.setIsbn(invoicing.getIsbn());
		result.setBook(invoicing.getBook());
		result.setAuthor(invoicing.getAuthor());
		result.setPrice(invoicing.getPrice());
		result.setStock(invoicing.getStock());
		return result;
	}

	@Transactional
	@Override
	public List<InvoicingResponse> sales(InvoicingRequest invoicingRequest) {
		List<InvoicingResponse> results = new ArrayList<>();
		int totalCount = 0;

		for (SalesRequest item : invoicingRequest.getSalesList()) {
			List<String> isbnIds = Collections.singletonList(item.getIsbn());
			List<Invoicing> invoicings = invoicingDao.findAllById(isbnIds);
			Invoicing invoicing1 = invoicingDao.findById(item.getIsbn())
					.orElseThrow(() -> new IllegalArgumentException("ISBN錯誤"));
			if (item.getNum() < 0 || item.getNum() > 3) {
				throw new IllegalArgumentException("數量錯誤");
			}
			if (item.getNum() > invoicing1.getStock()) {
				throw new IllegalArgumentException("庫存不足");
			}
			totalCount += item.getNum();
			if (totalCount > 3) {
				throw new IllegalArgumentException("購買數量超過限制");
			}
			Invoicing invoicing = invoicings.get(0);
			int price = invoicing.getPrice();
			int total = price * item.getNum();
			invoicing.setStock(invoicing.getStock() - item.getNum());
			invoicing.setSell(invoicing.getSell() + item.getNum());
			InvoicingResponse result = new InvoicingResponse();
			result.setIsbn(invoicing.getIsbn());
			result.setBook(invoicing.getBook());
			result.setAuthor(invoicing.getAuthor());
			result.setPrice(price);
			result.setNum(item.getNum());
			result.setTotal(total);
			invoicingDao.save(invoicing);
			results.add(result);
		}
		return results;
	}

	@Transactional
	@Override
	public List<Object[]> findTop5ByOrderBySellDesc() {
		List<Invoicing> invoicings = invoicingDao.findTop5ByOrderBySellDesc();
		List<Object[]> results = invoicings.stream().map(invoicing -> new Object[] { invoicing.getIsbn(),
				invoicing.getBook(), invoicing.getAuthor(), invoicing.getPrice() }).collect(Collectors.toList());
		return results;
	}
}

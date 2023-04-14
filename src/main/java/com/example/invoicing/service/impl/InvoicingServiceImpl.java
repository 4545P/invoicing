package com.example.invoicing.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

	// 如果只修改一本書的分類，那回傳型態就不需要是List<Invoicing>，
	// 回傳Invoicing就好。
	// 現在可以多重尋找分類了 但是只能找到單一分類的書籍

	@Autowired
	private InvoicingDao invoicingDao;

	@Transactional
	@Override
	public InvoicingResponse addBook(InvoicingRequest invoicingRequest) {
		List<Invoicing> isbnList = invoicingRequest.getIsbnList();
		List<Invoicing> errorList = new ArrayList<>();

		if (CollectionUtils.isEmpty(isbnList)) {
			return new InvoicingResponse(errorList, "ISBN欄位為空");
		}

		// 檢查參數是否正確以及是否有重複輸入的ISBN
		Set<String> isbnSet = new HashSet<>();
		for (Invoicing isbn : isbnList) {
			if (isbn.getIsbn().isBlank() || isbn.getBook().isBlank() || isbn.getAuthor().isBlank()
					|| isbn.getCategory().isEmpty() || isbn.getPrice() < 0) {
				errorList.add(isbn);
			} else if (!isbnSet.add(isbn.getIsbn())) {
				errorList.add(isbn);
			}
			Optional<Invoicing> existingIsbn = invoicingDao.findById(isbn.getIsbn());
			if (existingIsbn.isPresent()) {
				errorList.add(isbn);
			}
		}
		if (!errorList.isEmpty()) {
			return new InvoicingResponse(errorList, "ISBN重複或欄位為空或價格錯誤");
		}
		invoicingDao.saveAll(isbnList);
		return new InvoicingResponse(isbnList, "書籍新增成功");
	}

	@Transactional
	@Override
	public InvoicingResponse updateCategory(String isbn, List<String> newCategories) {
		Optional<Invoicing> existingBook = invoicingDao.findById(isbn);
		if (!existingBook.isPresent()) {
			return new InvoicingResponse("修改錯誤請檢查參數");
		}

		// 如果 newCategories 為 null 或者為空，或者包含 null 或者空白字串，則執行相應的防呆處理
		if (newCategories == null || newCategories.isEmpty() || newCategories.contains(null)
				|| newCategories.stream().anyMatch(String::isBlank)) {
			return new InvoicingResponse("修改錯誤請檢查參數");
		}

		Invoicing book = existingBook.get();
		String categoriesStr = book.getCategory();
		List<String> categoriesList = Arrays.asList(categoriesStr.split(","));

		Set<String> categoriesSet = new HashSet<>(categoriesList);
		for (String category : newCategories) {
			if (!categoriesSet.contains(category)) {
				categoriesSet.add(category);
			} else {
				categoriesSet.remove(category);
			}
		}

		List<String> newCategoriesList = new ArrayList<>(categoriesSet);
		String newCategoriesStr = String.join(",", newCategoriesList);
		book.setCategory(newCategoriesStr);
		invoicingDao.save(book);

		return new InvoicingResponse(Arrays.asList(book), "書籍類別更新成功");
	}

	@Override
	public InvoicingResponse findByCategoryContaining(String category) {
		if (category == null || category.isEmpty()) {
			return new InvoicingResponse("查詢錯誤請檢查參數");
		}
		
		List<Invoicing> search = invoicingDao.findByCategoryContaining(category);
		
		if (search.isEmpty()) {
			return new InvoicingResponse("找不到符合的類別");
		}
		
		List<Invoicing> results = new ArrayList<>();
		for (Invoicing invoicing : search) {
			Invoicing result = new Invoicing();
			result.setIsbn(invoicing.getIsbn());
			result.setBook(invoicing.getBook());
			result.setAuthor(invoicing.getAuthor());
			result.setPrice(invoicing.getPrice());
			result.setStock(invoicing.getStock());
			results.add(result);
		}

		return new InvoicingResponse(results, "查詢成功");
	}

	@Override
	public InvoicingResponse search(String isbn, String book, String author) {
		// 查詢輸入的是哪一個參數
		List<Invoicing> searchResult;
		if (StringUtils.hasText(isbn)) {
			if (invoicingDao.findByIsbn(isbn).isEmpty()) {
				return new InvoicingResponse("找不到符合的ISBN");
			}
			searchResult = invoicingDao.findByIsbn(isbn);
		} else if (StringUtils.hasText(book)) {
			if (invoicingDao.findByBook(book).isEmpty()) {
				return new InvoicingResponse("找不到符合的書名");
			}
			searchResult = invoicingDao.findByBook(book);
		} else if (StringUtils.hasText(author)) {
			if (invoicingDao.findByAuthor(author).isEmpty()) {
				return new InvoicingResponse("找不到符合的作者");
			}
			searchResult = invoicingDao.findByAuthor(author);
		} else {
			// 如果三個參數都為空，則回傳錯誤訊息
			return new InvoicingResponse("搜尋錯誤, 請至少提供一個參數: isbn, book, author.");
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
		return new InvoicingResponse(results, "查詢成功");
	}

	@Override
	public InvoicingResponse searchForShop(String isbn, String book, String author) {
		// 查詢輸入的是哪一個參數
		List<Invoicing> searchResult;
		if (StringUtils.hasText(isbn)) {
			if (invoicingDao.findByIsbn(isbn).isEmpty()) {
				return new InvoicingResponse("找不到符合的ISBN");
			}
			searchResult = invoicingDao.findByIsbn(isbn);
		} else if (StringUtils.hasText(book)) {
			if (invoicingDao.findByBook(book).isEmpty()) {
				return new InvoicingResponse("找不到符合的書名");
			}
			searchResult = invoicingDao.findByBook(book);
		} else if (StringUtils.hasText(author)) {
			if (invoicingDao.findByAuthor(author).isEmpty()) {
				return new InvoicingResponse("找不到符合的作者");
			}
			searchResult = invoicingDao.findByAuthor(author);
		} else {
			// 如果三個參數都為空，則回傳錯誤訊息
			return new InvoicingResponse("搜尋錯誤, 請至少提供一個參數: isbn, book, author.");
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
		return new InvoicingResponse(results, "查詢成功");
	}

	@Transactional
	@Override
	public InvoicingResponse purchase(String isbn, Integer purchase) {
		Optional<Invoicing> isbnOp = invoicingDao.findById(isbn);
		if (!isbnOp.isPresent()) {
			return new InvoicingResponse("ISBN錯誤");
		}

		// 找到進貨的書籍資料
		Invoicing invoicing = isbnOp.get();
		Integer stock = invoicing.getStock();
		// 檢查進貨數量
		if (stock == null) {
			stock = 0;
		}
		// 更新庫存
		stock = purchase + invoicing.getStock();
		invoicing.setStock(stock);
		invoicingDao.save(invoicing);

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
		// 檢查ISBN及價格
		Optional<Invoicing> isbnOp = invoicingDao.findById(isbn);
		if (!isbnOp.isPresent()) {
			return new InvoicingResponse("ISBN錯誤");
		}
		Invoicing invoicing = isbnOp.get();
		if (price < 0) {
			return new InvoicingResponse("price錯誤");
		}

		// 更新價格
		int newPrice = (price);
		invoicing.setPrice(newPrice);
		invoicingDao.save(invoicing);
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
	public InvoicingResponse sales(InvoicingRequest invoicingRequest) {
		List<InvoicingResponse> results = new ArrayList<>();
		int totalCount = 0;

		// 遍歷購買的書籍並檢查
		for (SalesRequest item : invoicingRequest.getSalesList()) {
			List<String> isbnIds = Collections.singletonList(item.getIsbn());
			List<Invoicing> invoicings = invoicingDao.findAllById(isbnIds);
			Invoicing invoicing1 = invoicingDao.findById(item.getIsbn())
					.orElseThrow(() -> new IllegalArgumentException("ISBN錯誤"));

			// 針對數量做限制及防呆
			if (item.getNum() < 0 || item.getNum() > 3) {
				return new InvoicingResponse("數量錯誤");
			}
			if (item.getNum() > invoicing1.getStock()) {
				return new InvoicingResponse("庫存不足");
			}
			totalCount += item.getNum();
			if (totalCount > 3) {
				return new InvoicingResponse("訂單數量超過限制");
			}
			// 回傳價格與相關資料
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
		return new InvoicingResponse("銷售成功", results);
	}

	// 排行榜
	@Override
	public List<Object[]> findTop5ByOrderBySellDesc() {
		List<Invoicing> invoicings = invoicingDao.findTop5ByOrderBySellDesc();
		List<Object[]> results = invoicings.stream().map(invoicing -> new Object[] { invoicing.getIsbn(),
				invoicing.getBook(), invoicing.getAuthor(), invoicing.getPrice() }).collect(Collectors.toList());
		return results;
	}
}

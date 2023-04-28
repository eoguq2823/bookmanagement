package com.toyproject.bookmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.book.CategoryRespDto;
import com.toyproject.bookmanagement.dto.book.GetBookRespDto;
import com.toyproject.bookmanagement.dto.book.RentalListRespDto;
import com.toyproject.bookmanagement.dto.book.SearchBookReqDto;
import com.toyproject.bookmanagement.dto.book.SearchBookRespDto;
import com.toyproject.bookmanagement.repository.BookRepository;
import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	
	public GetBookRespDto getBook(int bookId) {
		return bookRepository.getBook(bookId).toGetBookDto();
	}
	
	public Map<String, Object> searchBooks(SearchBookReqDto searchBookReqDto){
		List<SearchBookRespDto> list = new ArrayList<>();
		
		int index = (searchBookReqDto.getPage() -1) * 20;  //20개씩 들고 오게만들기 50하면 50개씩
		Map<String, Object> map = new HashMap<>();
		map.put("index", index);  //이런 처리를 하는 곳이 서비스 / 컨트롤러는 데이터요청하고 응답만받음
		map.put("categoryIds", searchBookReqDto.getCategoryIds());
		map.put("searchValue", searchBookReqDto.getSearchValue());
		bookRepository.searchBooks(map).forEach(book -> {
			list.add(book.toDto());
		});
		
		System.out.println(searchBookReqDto.getSearchValue());
		
		int totalCount = bookRepository.getTotalCount(map);
		
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("totalCount", totalCount);
		responseMap.put("bookList", list);
		return responseMap;
	}
	
	public List<CategoryRespDto> getCategories(){
		
		List<CategoryRespDto> list = new ArrayList<>();
		
		bookRepository.getCategories().forEach(category -> {
			list.add(category.toDto());
		});
		return list;
	}
	public int getLikeCount(int bookId) {
		return bookRepository.getLikeCount(bookId);	
		}

	public int getLikeStatus(int bookId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookId", bookId);
		map.put("userId", userId);
		
		return bookRepository.getLikeStatus(map);
//		String email = SecurityContextHolder.getContext().getAuthentication().getName(); // email jwt 필터에서 홀더에 넣어놨음
//		User userEntity = userRepository.findUserByEmail(email);
	}
	
	public int setLike(int bookId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookId", bookId);
		map.put("userId", userId);
		
		return bookRepository.setLike(map);
	}
	
	
	public int disLike(int bookId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookId", bookId);
		map.put("userId", userId);
		
		return bookRepository.disLike(map);
	}
	
	public List<RentalListRespDto> getRentalListByBookId(int bookId) {
		List<RentalListRespDto> list = new ArrayList<>(); 
		bookRepository.getRentalListByBookId(bookId).forEach(rentalData->{
			list.add(rentalData.toDto());
		});
		
		return list;
	}

	public int rentalBook(int bookListId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookListId", bookListId);
		map.put("userId", userId);
		
		return bookRepository.rentalBook(map);
	}
	
	public int returnBook(int bookListId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookListId", bookListId);
		map.put("userId", userId);
		
		return bookRepository.returnBook(map);
	}
}

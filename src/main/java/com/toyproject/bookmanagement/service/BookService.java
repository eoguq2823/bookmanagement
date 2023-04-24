package com.toyproject.bookmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.book.SearchBookReqDto;
import com.toyproject.bookmanagement.dto.book.SearchBookRespDto;
import com.toyproject.bookmanagement.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	
	private final BookRepository bookRepository;
	
	public List<SearchBookRespDto> searchBooks(SearchBookReqDto searchBookReqDto) {
		List<SearchBookRespDto> list = new ArrayList<>();
		
		int index = (searchBookReqDto.getPage() -1) * 20; //20개씩 들고 오게만들기 50하면 50개씩
		Map<String, Object> map = new HashMap<>();
		map.put("index", index); //이런 처리를 하는 곳이 서비스 / 컨트롤러는 데이터요청하고 응답만받음
		
		bookRepository.searchBooks(map).forEach(book -> {
			list.add(book.toDto());
		});
		
		return list;
	}
}

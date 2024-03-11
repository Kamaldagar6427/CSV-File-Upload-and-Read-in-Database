package com.spring.files.csv.config;

import org.springframework.batch.item.ItemProcessor;

import com.spring.files.csv.model.Tutorial;

public class TutorialProcessor implements ItemProcessor<Tutorial, Tutorial>{

	@Override
	public Tutorial process(Tutorial item) throws Exception {
		return item;
	}

}

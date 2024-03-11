package com.spring.files.csv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tutorials")
public class Tutorial {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  private int id;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "published")
  private int published;

  public Tutorial() {

  }

public Tutorial(int id, String title, String description, int published) {
	super();
	this.id = id;
	this.title = title;
	this.description = description;
	this.published = published;
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public int getPublished() {
	return published;
}

public void setPublished(int published) {
	this.published = published;
}

@Override
public String toString() {
	return "Tutorial [id=" + id + ", title=" + title + ", description=" + description + ", published=" + published
			+ "]";
}
  
  
}

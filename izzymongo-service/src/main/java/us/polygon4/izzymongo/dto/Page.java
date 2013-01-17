/*
 * IzzyMongo Database Viewer 
 * 
 * Copyright (C) 2013 Polygon4, and individual contributors
 * by the @authors tag.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package us.polygon4.izzymongo.dto;



import java.util.List;

import com.mongodb.DBObject;

/**
 * Page container
 * 
 * @author Mikhail Izrailov
 *
 */
public class Page {
	private CollectionMetadata collectionInfo;
	private int maxSize;
	private int size;
	private String firstId;
	private String lastId;
	private List<DBObject> items;
	private DBObject dbObject;
	private int pageId;
	private boolean firstPage;
	private boolean lastPage;
	
	
	public Page(){}
	
	public Page(CollectionMetadata colMeta) {
		collectionInfo=colMeta;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getFirstId() {
		return firstId;
	}
	public void setFirstId(String firstId) {
		this.firstId = firstId;
	}
	public String getLastId() {
		return lastId;
	}
	public void setLastId(String lastId) {
		this.lastId = lastId;
	}
	public List<DBObject> getItems() {
		return items;
	}
	public void setItems(List<DBObject> items) {
		this.items = items;
	}
	public boolean isFirstPage() {
		return firstPage;
	}
	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	
	public CollectionMetadata getCollectionInfo() {
		return collectionInfo;
	}
	public void setCollectionInfo(CollectionMetadata collectionInfo) {
		this.collectionInfo = collectionInfo;
	}
	public DBObject getDbObject() {
		return dbObject;
	}
	public void setDbObject(DBObject dbObject) {
		this.dbObject = dbObject;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page [collectionInfo=" + collectionInfo + ", maxSize="
				+ maxSize + ", size=" + size + ", firstId=" + firstId
				+ ", lastId=" + lastId + ", items=" + items + ", dbObject="
				+ dbObject + ", pageId=" + pageId + ", firstPage=" + firstPage
				+ ", lastPage=" + lastPage + "]";
	}
	
	

}

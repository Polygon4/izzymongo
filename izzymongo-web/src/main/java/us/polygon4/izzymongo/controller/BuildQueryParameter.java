package us.polygon4.izzymongo.controller;

public class BuildQueryParameter {
	public String db;
	public String col;
	public String[] criterias;
	public String[] criteria_vals;
	public String[] criteria_types;
	public String[] fields;
	public String prevId;
	public String nextId;
	public String browse_dir;
	public String sort;
	public int sort_dir;
	public int limit;

	public BuildQueryParameter(String db, String col, String[] criterias,
			String[] criteria_vals, String[] criteria_types, String[] fields,
			String prevId, String nextId, String browse_dir, String sort,
			int sort_dir, int limit) {
		this.db = db;
		this.col = col;
		this.criterias = criterias;
		this.criteria_vals = criteria_vals;
		this.criteria_types = criteria_types;
		this.fields = fields;
		this.prevId = prevId;
		this.nextId = nextId;
		this.browse_dir = browse_dir;
		this.sort = sort;
		this.sort_dir = sort_dir;
		this.limit = limit;
	}
}
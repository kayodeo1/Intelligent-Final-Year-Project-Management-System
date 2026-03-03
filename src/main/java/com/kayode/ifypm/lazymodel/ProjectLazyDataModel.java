/**
 * 
 */
package com.kayode.ifypm.lazymodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.constants.QueryType;
import com.kayode.ifypm.model.Project;
import com.kayode.ifypm.model.PagedList;
import com.kayode.ifypm.model.Status;
import com.kayode.ifypm.service.ProjectService;

/**
 * @author AAfolayan
 *
 */
public class ProjectLazyDataModel extends LazyDataModel<Project> {

	private ProjectService service;
	private QueryType query;

	List<Project> list = new ArrayList<>();

	private static Logger LOG = LoggerFactory.getLogger(ProjectLazyDataModel.class);

	public ProjectLazyDataModel(ProjectService service,QueryType query) {
		this.service = service;
		this.query = query;
	}

	@Override
	public Project getRowData(String rowKey) {
		// LOG.info("getRowData method invoked!");
		for (Project r : list) {
			if ((r.getId()).equals(rowKey))
				return r;
		}

		return null;
	}

	@Override
	public Object getRowKey(Project r) {
		// LOG.info("getRowKey method invoked! " + cdt);
		return r.getId();
	}

	@Override
	public List<Project> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		try {
			LOG.info("query invoked >>> " + query);
			List<Project> data = new ArrayList<>();
			// paginate db entries
			PagedList<Project> pagedList = new PagedList<>();
			switch (query) {
			case GET_ALL_PROJECT:
				pagedList = service.fetchProject(first, pageSize);
				break;
			default:
				LOG.warn("query type not found! , " + query);
				break;
			}

			// rowCount
			int dataSize = pagedList.getCount();// data.size();
			this.setRowCount(dataSize);

			// LOG.info("count >>> " + dataSize + " , pagedList.getList() >>> "
			// + pagedList.getList().size());

			return pagedList.getList();

		} catch (Exception e) {
			LOG.error("oops error encountered while paginating Project entries!!!", e);
			e.printStackTrace();
			return new ArrayList<Project>();
		}
	}

}

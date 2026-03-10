/**
 * 
 */
package com.kayode.ifypm.lazymodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.constants.QueryType;
import com.kayode.ifypm.model.Proposal;
import com.kayode.ifypm.model.PagedList;
import com.kayode.ifypm.model.Status;
import com.kayode.ifypm.service.ProposalService;

/**
 * @author AAfolayan
 *
 */
public class ProposalLazyDataModel extends LazyDataModel<Proposal> {

	private ProposalService service;
	private QueryType query;
	private Long userId;

	List<Proposal> list = new ArrayList<>();

	private static Logger LOG = LoggerFactory.getLogger(ProposalLazyDataModel.class);

	public ProposalLazyDataModel(ProposalService service,QueryType query) {
		this.service = service;
		this.query = query;
	}
	public ProposalLazyDataModel(ProposalService service,QueryType query,Long id) {
		this.service = service;
		this.query = query;
		this.userId=id;
	}

	@Override
	public Proposal getRowData(String rowKey) {
		// LOG.info("getRowData method invoked!");
		for (Proposal r : list) {
			if ((r.getId()).equals(rowKey))
				return r;
		}

		return null;
	}

	@Override
	public String getRowKey(Proposal r) {
		// LOG.info("getRowKey method invoked! " + cdt);
	    return r.getId() != null ? r.getId().toString() : null;
	}

	@Override
	public List<Proposal> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		try {
			LOG.info("query invoked >>> " + query);
			List<Proposal> data = new ArrayList<>();
			// paginate db entries
			PagedList<Proposal> pagedList = new PagedList<>();
			switch (query) {
			case GET_ALL_PROPOSAL:
				pagedList = service.fetchProposal(first, pageSize);
				break;
			case GET_ALL_STUDENT_PROPOSAL:
				pagedList = service.fetchStudentProposal(first, pageSize,userId);
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
			LOG.error("oops error encountered while paginating Proposal entries!!!", e);
			e.printStackTrace();
			return new ArrayList<Proposal>();
		}
	}

	@Override
	public int count(Map<String, FilterMeta> arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}

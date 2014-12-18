package ntut.csie.ezScrum.test.CreateData;

import java.util.Date;

import ntut.csie.ezScrum.issue.core.ITSEnum;
import ntut.csie.ezScrum.issue.sql.service.core.Configuration;
import ntut.csie.ezScrum.pic.core.IUserSession;
import ntut.csie.ezScrum.web.mapper.UnplannedItemMapper;
import ntut.csie.jcis.core.util.DateUtil;
import ntut.csie.jcis.resource.core.IProject;

public class EditUnplannedItem {
	private Configuration mConfig = new Configuration();
	public CreateUnplannedItem CU;
	public CreateProject CP;
	public CreateAccount CA;
	IProject mProject;
	String TEST_NOTE = "i am the update one";
	String TEST_STATUS = "";
	
	public EditUnplannedItem(CreateUnplannedItem CU, CreateProject CP) {
		this.CP = CP;
		this.CU = CU;
		mProject = CP.getProjectList().get(0);
	}
	
	public EditUnplannedItem(CreateUnplannedItem CU, CreateProject CP, CreateAccount CA) {
		this.CP = CP;
		this.CU = CU;
		this.CA = CA;
		mProject = CP.getProjectList().get(0);
	}

	public void exe() {
		IUserSession userSession = mConfig.getUserSession();
		UnplannedItemMapper uiMapper = new UnplannedItemMapper(mProject, userSession);
		long issueId;
		String name, handler, status, partners, estimated, actualHour, sprintID;
		String specificTime = DateUtil.getNow();
		Date date = DateUtil.dayFillter(specificTime, DateUtil._16DIGIT_DATE_TIME);
		for (int i = 0; i < CU.getIdList().size(); i++) {
			issueId = CU.getIdList().get(i);
			name = CU.getIssueList().get(i).getSummary();
			handler = CU.getIssueList().get(i).getAssignto(); 
			status = CU.getIssueList().get(i).getStatus();
			partners = CU.getIssueList().get(i).getPartners();
			estimated = CU.getIssueList().get(i).getEstimated();
			actualHour = CU.getIssueList().get(i).getActualHour();
			sprintID = CU.getIssueList().get(i).getSprintID();
			uiMapper.update(issueId, name, handler, status, partners, estimated, actualHour, TEST_NOTE, sprintID, date);
		}
	}
	
	/**
	 * 將unplan itme checkout
	 */
	public void exe_CO() {
		IUserSession userSession = mConfig.getUserSession();
		UnplannedItemMapper uiMapper = new UnplannedItemMapper(mProject, userSession);
		long issueId;
		String name, handler, partners, estimated, actualHour, sprintID;
		String specificTime = DateUtil.getNow();
		Date date = DateUtil.dayFillter(specificTime, DateUtil._16DIGIT_DATE_TIME);
		for (int i = 0; i < CU.getIdList().size(); i++) {
			issueId = CU.getIdList().get(i);
			name = CU.getIssueList().get(i).getSummary();
			handler = CA.getAccount_ID(i+1);
			partners = CU.getIssueList().get(i).getPartners();
			estimated = CU.getIssueList().get(i).getEstimated();
			actualHour = CU.getIssueList().get(i).getActualHour();
			sprintID = CU.getIssueList().get(i).getSprintID();
			uiMapper.update(issueId, name, handler, ITSEnum.S_ASSIGNED_STATUS, partners, estimated, actualHour, TEST_NOTE, sprintID, date);
		}
	}
	
	/**
	 * 將unplan itme 拉至done
	 */
	public void exe_DONE() {
		IUserSession userSession = mConfig.getUserSession();
		UnplannedItemMapper uiMapper = new UnplannedItemMapper(mProject, userSession);
		long issueId;
		String name, handler, partners, estimated, actualHour, sprintID;
		String specificTime = DateUtil.getNow();
		Date date = DateUtil.dayFillter(specificTime, DateUtil._16DIGIT_DATE_TIME);
		for (int i = 0; i < CU.getIdList().size(); i++) {
			issueId = CU.getIdList().get(i);
			name = CU.getIssueList().get(i).getSummary();
			handler = CA.getAccount_ID(i+1);
			partners = CU.getIssueList().get(i).getPartners();
			estimated = CU.getIssueList().get(i).getEstimated();
			actualHour = CU.getIssueList().get(i).getActualHour();
			sprintID = CU.getIssueList().get(i).getSprintID();
			uiMapper.update(issueId, name, handler, ITSEnum.S_CLOSED_STATUS, partners, estimated, actualHour, TEST_NOTE, sprintID, date);
		}
	}
}

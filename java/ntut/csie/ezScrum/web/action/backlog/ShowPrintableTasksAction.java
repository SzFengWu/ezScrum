package ntut.csie.ezScrum.web.action.backlog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;

import com.lowagie.text.DocumentException;

import ntut.csie.ezScrum.pic.core.IUserSession;
import ntut.csie.ezScrum.pic.core.MakePDFService;
import ntut.csie.ezScrum.pic.core.ScrumRole;
import ntut.csie.ezScrum.web.dataObject.AccountObject;
import ntut.csie.ezScrum.web.dataObject.ProjectObject;
import ntut.csie.ezScrum.web.dataObject.TaskObject;
import ntut.csie.ezScrum.web.logic.ScrumRoleLogic;
import ntut.csie.ezScrum.web.support.SessionManager;

public class ShowPrintableTasksAction extends DownloadAction {
	private static Log log = LogFactory.getLog(ShowPrintableStoryAction.class);
	
	protected StreamInfo getStreamInfo(ActionMapping mapping,
	        ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get session info
		ProjectObject project = (ProjectObject) SessionManager.getProject(request);
		IUserSession session = (IUserSession) request.getSession().getAttribute("UserSession");
				
		String[] getSelectedTaskId = request.getParameterValues("selects");
		// Make ["["1", "2", "3"]"] into ["1", "2", "3"]
		String[] selectedSerialTasksId = getSelectedTaskId[0].replaceAll("\\[", "").replaceAll("\\]", "").split(",");
		
		ArrayList<TaskObject> selectedTasks = new ArrayList<TaskObject>();
		
		for (String serialTaskId : selectedSerialTasksId) {
			long serialId = Long.parseLong(serialTaskId);
			long projectId = project.getId();
			TaskObject task = TaskObject.get(projectId, serialId);
			selectedTasks.add(task);
		}

		request.setAttribute("Tasks", selectedTasks);
		File file = null;
		
		try {
			//直接嵌入server上的pdf字型擋給系統 
			ServletContext servletContext = getServlet().getServletContext();
			String filePath = servletContext.getRealPath("") + "/WEB-INF/otherSetting/uming.ttf";

			MakePDFService makePDFService = new MakePDFService();
			file = makePDFService.getTaskFile(filePath, selectedTasks);
		} catch(DocumentException de){
			log.debug(de.toString());
		} catch (IOException ioe) {
			log.debug(ioe.toString());
		}
		
		response.setHeader("Content-disposition", "inline; filename=SprintStoryTask.pdf");

		AccountObject account = session.getAccount();
		
		ScrumRole scrumRole = new ScrumRoleLogic().getScrumRole(project, account);
		if (file == null) {
			throw new Exception(" pdf file is null");
		}
		if (scrumRole.getAccessSprintBacklog()) {
			String contentType = "application/pdf";
			return new FileStreamInfo(contentType, file);
		} else {
			return null;
		}
	}
}

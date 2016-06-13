package ntut.csie.ezScrum.robust.carelessCleanup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ntut.csie.ezScrum.issue.sql.service.core.Configuration;
import ntut.csie.ezScrum.issue.sql.service.core.InitialSQL;
import ntut.csie.ezScrum.robust.aspectj.tool.AspectJSwitch;
import ntut.csie.ezScrum.robust.resource.tool.ResourceManager;
import ntut.csie.ezScrum.test.CreateData.CreateProject;
import ntut.csie.ezScrum.web.dataObject.ProjectObject;
import servletunit.struts.MockStrutsTestCase;

public class ExportStoriesFromProductBacklogActionTest extends MockStrutsTestCase {
	private CreateProject mCP;
	private Configuration mConfig;
	private String mActionPath = "/exportStories";
	private String mActionName = "ExportStoriesFromProductBacklogAction";
	
	public ExportStoriesFromProductBacklogActionTest(String testMethod) {
		super(testMethod);
	}

	protected void setUp() throws Exception {
		// Turn test mode on
		mConfig = new Configuration();
		mConfig.setTestMode(true);
		mConfig.save();
		
		// Initialize database
		InitialSQL ini = new InitialSQL(mConfig);
		ini.exe();

		// Create Project
		mCP = new CreateProject(1);
		mCP.exeCreateForDb();
		
		cleanEzscrumExcelTempFiles();

		super.setUp();

		setContextDirectory(new File(mConfig.getBaseDirPath() + "/WebContent")); // 設定讀取的
		setServletConfigFile("/WEB-INF/struts-config.xml");
		setRequestPathInfo(mActionPath);

		// ================ set initial data =======================
		ProjectObject project = mCP.getAllProjects().get(0);
		// ================ set session info ========================
		request.getSession().setAttribute("UserSession", mConfig.getUserSession());
		request.getSession().setAttribute("Project", project);
		request.setHeader("Referer", "?projectName=" + project.getName());
				
		// ============= release ==============
		ini = null;
	}

	protected void tearDown() throws Exception {
		// Clean database
		InitialSQL ini = new InitialSQL(mConfig);
		ini.exe();

		// Turn test mode off
		mConfig.setTestMode(false);
		mConfig.save();

		super.tearDown();

		// ============= release ==============
		ini = null;
		mCP = null;
		mConfig = null;
	}

	public void testExportStoriesFromProductBacklogAction() {
		// Turn AspectJ Switch on
		AspectJSwitch.getInstance().turnOnByActionName(mActionName);
		// invoke ExportStoriesFromProductBacklogAction
		actionPerform();
		File ezScrumExcel = getEzScrumExcelTempFile();
		// if ezScrumExcel does not exist, 
		// getEzScrumExcelTempFile() will return null.
		assertNull(ezScrumExcel);
		// Turn AspectJ Switch off
		AspectJSwitch.getInstance().turnOff();
	}
	
	private File getEzScrumExcelTempFile() {
		try {
			// Create temp file to get the directory of ezScrumExcel
			File findPathFile = File.createTempFile("findFile", Long.toString(System.nanoTime()));
			File parentFile = findPathFile.getParentFile();
			for (File file : parentFile.listFiles()) {
				// Filter files which file name match "ezScrumExcel"
				if(!file.isDirectory() && file.getName().contains("ezScrumExcel")){
					return file;
				}
			}
			findPathFile.delete();
		} catch (IOException e) {
			ResourceManager.recordExceptionMessage(e);
		}
		return null;
	}
	
	private ArrayList<File> getEzScrumExcelTempFiles() {
		ArrayList<File> ezScrumExcels = new ArrayList<File>();
		try {
			// Create temp file to get the directory of ezScrumExcel
			File findPathFile = File.createTempFile("findFile", Long.toString(System.nanoTime()));
			File parentFile = findPathFile.getParentFile();
			for (File file : parentFile.listFiles()) {
				// Filter files which file name match "ezScrumExcel"
				if(!file.isDirectory() && file.getName().contains("ezScrumExcel")){
					ezScrumExcels.add(file);
				}
			}
			findPathFile.delete();
		} catch (IOException e) {
			ResourceManager.recordExceptionMessage(e);
		}
		return ezScrumExcels;
	}
	
	private void cleanEzscrumExcelTempFiles() {
		ArrayList<File> ezScrumExcels = getEzScrumExcelTempFiles();
		// Delete files which name match "ezScrumExcel"
		for(File file : ezScrumExcels){
			file.delete();
		}
		// Check all ezScrumExcel files deleted
		for(File file : ezScrumExcels){
			assertFalse(file.exists());
		}
	}
}

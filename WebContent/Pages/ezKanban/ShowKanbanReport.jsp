<%@ page contentType="text/html; charset=utf-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">

Ext.onReady(function() {
	
	var tabs = new Ext.TabPanel({
		id: 'tabs',
	    region: 'center',
	    activeTab: 0,
	    items: [{
	    	title: 'Flow Diagram',
	    	html:'<iframe src=showFlowDiagram.do width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
		},{
			title: 'History',
			html:'<iframe src=showKanbanHistory.do width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
		}]
	});
	
	var content = new Ext.Panel({
		renderTo: 'content',
		layout : 'border',
		title : 'Kanban Report List',
		height: 650,
		items : [tabs]
	});
})
</script>	

<div id = "content">
</div>
<div id="SideShowItem" style="display:none;">showKanbanReport</div>
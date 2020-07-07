package property;




public class PropertyInspection {
private String inspectionid;
private String propertyId;
private DateTime InspectionDateFrom;
private DateTime InspectionDateTo;
private boolean finalstatus;


public PropertyInspection(String inspectionid, String propertyId, DateTime inspectionDateFrom,
		DateTime inspectionDateTo, boolean finalstatus) {
	super();
	this.inspectionid = inspectionid;
	this.propertyId = propertyId;
	InspectionDateFrom = inspectionDateFrom;
	InspectionDateTo = inspectionDateTo;
	this.finalstatus = finalstatus;
}
public String getInspectionid() {
	return inspectionid;
}
public void setInspectionid(String inspectionid) {
	this.inspectionid = inspectionid;
}
public String getPropertyId() {
	return propertyId;
}
public void setPropertyId(String propertyId) {
	this.propertyId = propertyId;
}
public DateTime getInspectionDateFrom() {
	return InspectionDateFrom;
}
public void setInspectionDateFrom(DateTime inspectionDateFrom) {
	InspectionDateFrom = inspectionDateFrom;
}
public DateTime getInspectionDateTo() {
	return InspectionDateTo;
}
public void setInspectionDateTo(DateTime inspectionDateTo) {
	InspectionDateTo = inspectionDateTo;
}
public boolean isFinalstatus() {
	return finalstatus;
}
public void setFinalstatus(boolean finalstatus) {
	this.finalstatus = finalstatus;
}



}

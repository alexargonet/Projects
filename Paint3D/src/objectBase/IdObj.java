package objectBase;

public class IdObj {
	private int id=0;
	private IdObj subObj=null;
	public IdObj(int id) {
		this.id=id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public IdObj getSubObj() {
		return subObj;
	}
	public void setSubObj(IdObj subObj) {
		this.subObj = subObj;
	}
	public void setSubObj(int subId) {
		if(this.subObj==null)
			this.subObj = new IdObj(subId);
		else
			this.subObj.setId(subId);
	}
	public void cloneId(IdObj idOrig) {
		IdObj idObjTmp = null;
		IdObj idObjOrigOld = null;
		
		idObjTmp = idOrig;
		IdObj idObjOrig = this;
		
		while(idObjTmp!=null){
			if(idObjOrig!=null){
				idObjOrig.id = idObjTmp.getId();
			}
			else{
				idObjOrigOld.setSubObj(idObjTmp.getId());
				idObjOrig=idObjOrigOld.getSubObj();
			}
			idObjTmp=idObjTmp.getSubObj();
			
			idObjOrigOld=idObjOrig;
			idObjOrig=idObjOrig.getSubObj();
		}
	}
	
	public boolean isUguale(IdObj idVerif){
		IdObj idObjVerifTmp = idVerif;
		IdObj idObjRifTmp = this;
		
		while(true){
			if(idObjVerifTmp!=null && idObjRifTmp!=null){
				if(idObjVerifTmp.getId()!=idObjRifTmp.getId()){
					return false;
				}
				idObjVerifTmp=idObjVerifTmp.getSubObj();
				idObjRifTmp=idObjRifTmp.getSubObj();
			}
			else if(idObjVerifTmp==null && idObjRifTmp==null)
				return true;
			else
				return false;
		}
	}

}

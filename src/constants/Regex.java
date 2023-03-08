package constants;

public class Regex {
	
	public static final String ALL_TEXT = "^([a-zA-Z]|[à-ü]|[À-Ü]|\\-|\\.|\\,|\\/|\\(|\\)|[0-9]|"+MyValues.aSUPERSCRIPT+"|"+MyValues.oSUPERSCRIPT+"| )+$";
	public static final String NAME = "^([a-zA-Z]|[à-ü]|[À-Ü]|"+MyValues.aSUPERSCRIPT+"|"+MyValues.oSUPERSCRIPT+"| )+$";
	public static final String ACRONYM = "^([a-zA-Z])+$";
	public static final String EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
	public static final String ANO = "^\\d{4}|\\d{2}$";
	public static final String INT = "^\\d+$";
	public static final String DOUBLES = "^[+]?[0-9]*[.]?[0-9]+$";
	public static final String CC = "^[\\d]{8}$";
	public static final String PHONE = "^[\\d]{9}$";
	public static final String POSTALCODE = "^\\d{4}(-\\d{3})?$";
	public static final String STAM = "^([A-Z]|[0-9])+$";
}

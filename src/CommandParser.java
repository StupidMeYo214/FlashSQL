import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandParser {

	//Categorizing command types.
	public static int parse1(String y){
		String x=y.toLowerCase();
		if(x.startsWith("show schemas"))
			return 1;
		else if(x.startsWith("show tables"))
			return 2;
		else if(x.startsWith("use "))
			return 3;
		else if(x.startsWith("create schema "))
			return 4;
		else if(x.startsWith("create table "))
			return 5;
		else if(x.startsWith("insert into "))
			return 6;
		else if(x.startsWith("select *"))
			return 7;
		else if(x.startsWith("show columns"))
			return 8;
		else if(x.startsWith("help"))
			return 9;
		else if(x.startsWith("drop table "))
			return 10;
		else
		return 0;
	}
//#############################################################################
	//EXTRACT schema name from USE command.
	public static String parseUse(String x){
		String [] temp=x.split("\\s+");
		String result=temp[1];
		return result;
	}
//#############################################################################	
	//EXTRACT Schema_Name from CREATE SCHEMA command.
	public static String parseCreateSchema(String x){
		String [] temp=x.split("\\s+");
		String result=temp[2];
		return result;
	}
//#############################################################################	
	//EXTRACT Table_Name from CREATE TABLE command.
	public static String parseCreateTableTName(String x){
		String y=x.substring(0,x.indexOf('('));
		String [] temp=y.split("\\s+");
		String tableName=temp[2];
		return tableName;
	}
	
//#############################################################################
	//EXTRACT Column_Names from CREATE TABLE command.
	public static ArrayList<String> parseCreateTableColumnNames(String x){
		String result=x.substring(x.indexOf('(')+1, x.length()-1);
		String [] eachConstraint = result.split(",");
		ArrayList<String> columnNames=new ArrayList<>();
		for(int i=0; i<eachConstraint.length; i++)
		{
			String Name_and_Type = eachConstraint[i].substring(0, eachConstraint[i].indexOf('[')-1);
			if (Character.isWhitespace(Name_and_Type.charAt(0)))
			eachConstraint[i]=Name_and_Type.replaceFirst("\\s+","");
			else 
			eachConstraint[i]=Name_and_Type;
			String [] eachName_and_Type=eachConstraint[i].split("\\s+");
			String Name=eachName_and_Type[0];//String Type=eachName_and_Type[1];
			columnNames.add(Name);
		}
		return columnNames;
	}
	
	//EXTRACT Column_DataTypes from CREATE TABLE command.
	public static ArrayList<String> parseCreateTableDataTypes(String x){
		String result=x.substring(x.indexOf('(')+1, x.length()-1);
		String [] eachConstraint = result.split(",");
		ArrayList<String> columnNames=new ArrayList<>();
		
		for(int i=0; i<eachConstraint.length; i++)
		{
			String Type="";
			String Name_and_Type = eachConstraint[i].substring(0, eachConstraint[i].indexOf('[')-1);
			if (Character.isWhitespace(Name_and_Type.charAt(0)))
			eachConstraint[i]=Name_and_Type.replaceFirst("\\s+","");
			else 
			eachConstraint[i]=Name_and_Type;
			String [] eachName_and_Type=eachConstraint[i].split("\\s+");
			for(int j=1; j<eachName_and_Type.length;j++){
			Type+=eachName_and_Type[j];}
			columnNames.add(Type);
		}
		return columnNames;
	}
	
	//EXTRACT key constraint from CREATE TABLE command
	public static ArrayList<String> parseCreateTableKey(String x){
		ArrayList<String> keys=new ArrayList<>();
		String result=x.substring(x.indexOf('(')+1, x.length()-1);
		String [] eachConstraint = result.split(",");
		for(int i=0; i<eachConstraint.length; i++)
		{
			String keyStatus=eachConstraint[i].substring(eachConstraint[i].indexOf('[')+1, eachConstraint[i].indexOf('|'));
			if(eachConstraint[i].indexOf('[')+1==eachConstraint[i].indexOf('|'))
			{keys.add("");}
			else 
			keys.add(keyStatus);
		}
		return keys;
	}
	
	//EXTRACT Null constraint from CREATE TABLE command
	public static ArrayList<String> parseCreateTableNull(String x){
		ArrayList<String> Nulls=new ArrayList<>();
		String result=x.substring(x.indexOf('(')+1, x.length()-1);
		String [] eachConstraint = result.split(",");
		for(int i=0; i<eachConstraint.length; i++)
		{
			String NullStatus=eachConstraint[i].substring(eachConstraint[i].indexOf('|')+1, eachConstraint[i].indexOf(']'));
			if(eachConstraint[i].indexOf('|')+1==eachConstraint[i].indexOf(']'))
			{Nulls.add("");}
			else
			Nulls.add(NullStatus);
		}
		return Nulls;
	}
//#############################################################################
	//Extract Table Name Which To Insert Into.
	public static String parseInsertTableName(String x){
		String TableName="";
		String SubNoVal=x.substring(0,x.indexOf('('));
		String [] temp=SubNoVal.split("\\s+");
		TableName=temp[2];
		return TableName;
	}

	//Extract insert values
	public static ArrayList<String> parseInsertValues(String x){
		ArrayList<String> valueList=new ArrayList<>();
		String values=x.substring(x.indexOf('(')+1,x.indexOf(')'));
		
		String values1=values.replaceAll("\'", "");
		String [] temp=values1.split(",");
		for(int i=0; i<temp.length;i++)
		{valueList.add(temp[i].replaceAll("\\s+", ""));}
		return valueList;
	}
	
//#############################################################################
	//Extract Table Name for Select
	public static String parseSelectTableName(String x){
		String [] temp=x.split("\\s+");
		return temp[3];
	}
	
	//Extract column name
	public static String parseSelectColumnName(String x){
		String [] temp=x.split("\\s+");
		String columnName = temp[5].substring(0, parseSelectComparisonPosition1(temp[5]));
		return columnName;
	}
	
	//Extract compare
	public static int parseSelectComparison(String x){
		if(x.contains("="))
			return 1;
		else if(x.contains("<>"))
			return 2;
		else if(x.contains("<") && !x.contains(">"))
			return 3;
		else if(x.contains(">") && !x.contains("<"))
			return 4;
		else 
			return 0;
	}
	
	//Extract compare value
	public static String parseSelectValue(String x){
		String sub=x.substring(parseSelectComparisonPosition(x)+1,x.length());
		String sub1=sub.replaceAll("'","");
		String sub2=sub1.replaceAll("\\s+", "");
		return sub2;
	}
	
	//Extract Compare position
	public static int parseSelectComparisonPosition(String x){
		if(x.contains("="))
			return x.indexOf('=');
		else if(x.contains("<>"))
			return x.indexOf('>');
		else if(x.contains("<") && !x.contains(">"))
			return x.indexOf('<');
		else if(x.contains(">") && !x.contains("<"))
			return x.indexOf('>');
		else 
			return x.length();
	}
	
	public static int parseSelectComparisonPosition1(String x){
		if(x.contains("="))
			return x.indexOf('=');
		else if(x.contains("<>"))
			return x.indexOf('<');
		else if(x.contains("<") && !x.contains(">"))
			return x.indexOf('<');
		else if(x.contains(">") && !x.contains("<"))
			return x.indexOf('>');
		else 
			return x.length();
	}
	
//########################################################################################################
public static String parseDropTableName(String x){
	String [] temp=x.split("\\s+");
	return temp[2];
}
	//public static void main(String [] args){
		//Scanner scanner = new Scanner(System.in).useDelimiter(";");
		//String userCommand;
		//do{userCommand = scanner.next().trim();
		//System.out.println(parse1(userCommand));
		//System.out.println(parseUse(userCommand)+"test");
		//System.out.println(parseCreateSchema(userCommand)+"test");
		//System.out.println(parseCreateTableTName(userCommand)+"test");
		//ArrayList<String> constraints = parseCreateTableColumnNames(userCommand);
		//for(int i=0; i<constraints.size(); i++){
			//System.out.println(constraints.get(i));
		//}create table newtable ( name1 datat1(10) [primary key|not null], name2 data2(10) [|], name3 data3(5) [|not null], name4 data(14) [|not null] );
		//System.out.println(parseCreateTableTNameX(userCommand));
		//System.out.println(parseCreateTableTNameZ(userCommand)[0]+"\n"+parseCreateTableTNameZ(userCommand)[1]);
		//create table tablex (column1 dat1(10) [|],column3 dat3(10) [primary key|not null],column2 dat2(10) [|not null]);
		//ArrayList<String> names=parseCreateTableColumnNames(userCommand);
		//for(int i=0; i<names.size(); i++){
		//	System.out.println(names.get(i));
		//}
		//ArrayList<String> TableName=parseInsertValues(userCommand);
		//for(int i=0; i<TableName.size();i++)
		//System.out.println(TableName.get(i));
		//System.out.println(parseSelectColumnName(userCommand));
		//System.out.println(parseSelectComparison(userCommand));
		//System.out.println(parseSelectValue(userCommand));
		//}
		//while(!userCommand.equals("exit"));
		//System.out.println("Exiting...");
	//}
}

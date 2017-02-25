import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class DavisBase {
	public static void main(String [] args){
		
		System.out.println("Welcom To Davis Base.");
		System.out.println("Type \"help;\" to display supported commands.");
		System.out.println("****************************************************************************************");
		
		MakeInformationSchema.createInfotmationSchema();
		final String helpinfo="Type 'show schemas;' to see all schama in database.\nType 'show tables;' to check tables of a schema in database\nType 'show columns;' to see all the columns information in database\nType 'use + schemaname;' to use schema you specified\nType 'create schema + schemaname' to build new schema in given name\nType 'create table + tablename +(columnname datatype [primary key|not null],```columnnameN datatype(datalength) [|])' to createtable\n(Notice: key and null constraint must be specified in [primary key/(blank)|not null/(blank)]!)\nType insert into +tablename values(value1,value2,```valueN) to insert values in specified table\nType 'drop tablename;' to drop table\nType 'select * from +tablename+ where + columnname =/<>/</> + comapre value' to show rows in specified table\nType 'exit;' to leave DavisBase\n*****************************************************************************";
		
		String SchemaInUse="";
		
		
		
		Scanner scanner = new Scanner(System.in).useDelimiter(";");
		String userCommand;
		do{userCommand = scanner.next().trim();
		
		if(!userCommand.toLowerCase().equals("exit")){
		int category = CommandParser.parse1(userCommand);
		
		switch (category) {
		//SHOW SCHEMA
		case 1:
			ShowSchemas.showSchemas();
			break;
		//SHOW TABLES
		case 2:
			ShowTables.showTables(SchemaInUse);
			break;
		//USE
		case 3:
			String potentialSchemaNameString=CommandParser.parseUse(userCommand);
			boolean found=UseSchema.useSchema(potentialSchemaNameString);
			if(found)
			{SchemaInUse=potentialSchemaNameString;
			System.out.println("You are now using schema  |"+SchemaInUse+"|");}
			else {
				System.out.println("Sorry, we cannot find schema names: "+potentialSchemaNameString);
			}
			break;
		//CREATE SCHEMA
		case 4:
			String newSchamaName=CommandParser.parseCreateSchema(userCommand);
			Create_Schema.CreateSchema(newSchamaName);
			break;
		//CREATE TABLE
		case 5:
			if(SchemaInUse.equals(""))
			{System.out.println("Please Choose A Schema First");
			break;}
			String TableName=CommandParser.parseCreateTableTName(userCommand);
			ArrayList<String> columnNames=CommandParser.parseCreateTableColumnNames(userCommand);
			ArrayList<String> columnTypes=CommandParser.parseCreateTableDataTypes(userCommand);
			boolean correctInput=InputValidation.TableCreationValidation(columnTypes);
			if(correctInput){
			ArrayList<String> columnNulable=CommandParser.parseCreateTableNull(userCommand);
			ArrayList<String> columnKey=CommandParser.parseCreateTableKey(userCommand);
			Create_Table.CreateTable(SchemaInUse,TableName,columnNames,columnTypes,columnNulable,columnKey);
			System.out.println("Table  |"+TableName+"|  has been created.");
			break;}
			else {
				System.out.println("Please try again");
				break;
			}
		//INSERT INTO
		case 6:
			String TableNameForInsert=CommandParser.parseInsertTableName(userCommand);
			ArrayList<String> InsertValues=CommandParser.parseInsertValues(userCommand);
			if(SchemaInUse.equals(""))
			{System.out.println("Please Choose A Schema First");
			break;}
			//Table Exist?
			boolean tableExist=Create_Table.checkTableExistance(TableNameForInsert, SchemaInUse);
			if(!tableExist)
			{System.out.println("Table "+TableNameForInsert+"Doesn't Exist.");
			break;}
			InsertInto.InsertValues(SchemaInUse, TableNameForInsert, InsertValues);
			
			break;
		//SELECT FROM
		case 7:
			if(SchemaInUse.equals(""))
			{System.out.println("Please Choose A Schema First");
			break;}
			String TableNameForSelection=CommandParser.parseSelectTableName(userCommand);
			boolean tableExistance=Create_Table.checkTableExistance(TableNameForSelection, SchemaInUse);
			if(!tableExistance)
			{System.out.println("Table "+TableNameForSelection+"Doesn't Exist.");
			break;}
			String ColumnName=CommandParser.parseSelectColumnName(userCommand);
			String compValue=CommandParser.parseSelectValue(userCommand);
			Integer judgeSymble=CommandParser.parseSelectComparison(userCommand);
			ArrayList<Integer> locations=SelectFrom.getLocations(SchemaInUse, TableNameForSelection, ColumnName, compValue, judgeSymble);
			SelectFrom.printRow(SchemaInUse, TableNameForSelection, locations);
			
			break;
		//SHOW COLUMNS
		case 8:
			showcolumns.ShowColumns();
			break;
		case 9:
			System.out.println(helpinfo);
			break;
		case 10:
			if(SchemaInUse.equals(""))
			{System.out.println("Please Choose A Schema First");
			break;}
			String TableNameForDrop=CommandParser.parseDropTableName(userCommand);
			boolean tableExistance2=Create_Table.checkTableExistance(TableNameForDrop, SchemaInUse);
			if(!tableExistance2)
			{System.out.println("Table "+TableNameForDrop+"Doesn't Exist.");
			break;}
			
			DropTable.dropTable(SchemaInUse, TableNameForDrop);
			
		break;
		
		case 0:
			System.out.println("Sorry, I didn't understand the command: \"" + userCommand + "\"");
			break;
		}
		}
		}
		while(!userCommand.equals("exit"));
		System.out.println("Exiting...");
		
		try{
			
		}catch(Exception e){System.out.println("Error Occurs In information_schema building. "+e.getMessage());}
	}
}

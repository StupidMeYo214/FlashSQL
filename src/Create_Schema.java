import java.io.RandomAccessFile;

//Adding new schema into schemata table
public class Create_Schema {
	public static void CreateSchema(String SchemaName){
		//when a new schema is created, schema name will be listed in the schema information. schema table
		try{
			
		RandomAccessFile schemataTableFile = new RandomAccessFile("information_schema.schemata.tbl", "rw");
		long currentLength=schemataTableFile.length();
		schemataTableFile.seek(currentLength);
		schemataTableFile.writeByte(SchemaName.length());
		schemataTableFile.writeBytes(SchemaName);
		}catch(Exception e){System.out.println("Error Occurs in "+e.getMessage());};
		System.out.println("New Schema '"+SchemaName+"' Has Benn Created!");
	}

}

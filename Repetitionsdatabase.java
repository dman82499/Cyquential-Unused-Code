package ctfirstpack;


import java.io.InputStreamReader;
import java.io.ObjectInputStream;
//maybe import the interfaces too?
import java.io.ObjectOutputStream;
import javax.crypto.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.Scanner;


/**
 * Utility class (built for Cyquential Project but is multipurposeful) that provides a range of functions for storeing repetitions/objects in a file for long term acess.
 *Please note that this class is mainly for storing repetitions, not using them. It follows that multiple renderings of the same 
 * 
 *
 */
//Eventually this should be it's own class for writing and reading to files
public class Repetitionsdatabase{
	
	/**
	 * Private constructor so this utility class cannot be instantiated.
	 */
	private Repetitionsdatabase() {}
    
	public static File createnewrepetitionlfile(String nam) {
		File file = new File(nam);
		String text = "ABCD";
		//The first step before doing this is to encrypt the new file
		try (FileOutputStream fos = new FileOutputStream(file)){
		
			ObjectOutputStream bos = new ObjectOutputStream(fos);
				//instead of wrapping it in a Buffered Stream, could a custom encoding text stream be created to do this?
				//For Example: byte[] data = "ABCD".getBytes(StandardCharsets.UTF_8);
				//(Self note: The only difference between between a Buffered and non buffered is that buffered is encoded 
				//where as non is still binary.)
				//DataOutputStream dos = new DataOutputStream(bos); 
			bos.writeBoolean(false);
			bos.writeBoolean(false);
			bos.writeBoolean(true);
			bos.writeBoolean(false);
			System.out.println("Successfully written data to the file");
		}
		catch (IOException e) {
			e.printStackTrace();
        
    }
		return file;
	}
	public static boolean checkrepetitionfile(String nam2){
		//you can fix this with a null value return, however as of now it should work correctly in this implementation
		//ObjectInputStream bos2 = null;
		boolean ifile = false;
		try (FileInputStream fos2 = new FileInputStream(nam2)) {
			ObjectInputStream bos2 = new ObjectInputStream(fos2);
			System.out.println("File Found! Dycrypting:");
			System.out.println("Checking start key:");
			boolean key1 = bos2.readBoolean();
			boolean key2 = bos2.readBoolean();
			boolean key3 = bos2.readBoolean();
			boolean key4 = bos2.readBoolean();
			if (!key1 && !key2 && key3 && !key4) {
				System.out.println("Start Key Found!");
				//convert this arraylist to a repetition after done
				ifile = true;
			}
			else {
				System.err.println("Start Key check error!");
				System.exit(5);
				//should we create a custom exception here?
				
			}
		}
		catch (IOException e) {
			//System.out.print("Cannot find Repetitions file! Creating new one.");
			//File el = createnewrepetitionlfile(nam2);
			//System.out.print("Retrying to read the new repetitions file");
			//checkrepetitionfile(nam2);
			e.printStackTrace();
			System.exit(5);

		}
		return ifile;
	}
	
	private static ArrayList<RepetitionFoundation> scan_allRepetitionsFromDecryptedLinearOrdering(ObjectInputStream reps) {
		//question: Will this work instead of using object and what are the advantages here?
		ArrayList<RepetitionFoundation> Repetit = new ArrayList<>();
		ArrayList<Object> InitialRepetit = new ArrayList();
		RepetitionFoundation cast;
		try {
			while (reps.available() > 1) {
				try {
					Object R = reps.readObject();
					cast = (RepetitionFoundation)R;
					Repetit.add(cast);
					System.out.println("Repetition found!");
					
				} catch (ClassNotFoundException e) {
					System.out.println("Could not convert class data, either file is wrong or class is incorrect.");
					e.printStackTrace();
				} catch (IOException e) {
				
					System.out.println("Something went wrong, could not find file");
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Attempting to convert Repetition Objects");
		//Repetit = (ArrayList<RepetitionFoundation>)InitialRepetit;
		return Repetit;
	}
	//Think about implementing memory controls in a seperate class, aka implementing open memory, sort memory(maybe a different class), overwriting functions,
	//close memory sort of programs.
	
	public static ArrayList<RepetitionFoundation> open_rep_bank(String nam) {
		ArrayList<RepetitionFoundation> repetitions = new ArrayList<>();
		//ArrayList<RepetitionFoundation> repetitions = null;
		if (checkrepetitionfile(nam)) {
			//this is stupidly redundant, but fix later, can't fix because of OCD
			try (FileInputStream fos2 = new FileInputStream(nam)) {
				ObjectInputStream bos2 = new ObjectInputStream(fos2);
				bos2.skipBytes(4);
				repetitions = scan_allRepetitionsFromDecryptedLinearOrdering(bos2);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else {
			//these messages will eventually be moved to a gui
			System.out.println("could not find file, would you like to create a new one? Type y for yes, anything else for no");
			Scanner read = new Scanner(System.in);
			String que = read.next();
			if (que.equals("y")) {
				createnewrepetitionlfile(nam);
				//Ideally you would have a gui for this that would just auto load all repetition files first, this method would run under
				//something like "run custom repetition file"
				System.out.println();
				
			}
		}
		
	return repetitions;
	}
	
	public static void update_repetition_bank(ObjectOutputStream data) {
		
	
	}	
}
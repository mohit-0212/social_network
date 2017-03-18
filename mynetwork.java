import java.io.*;
import java.util.*;
import java.lang.*;


/*
@author Mohit_Agarwal_2015060
@author Akhil_Goel_2015126
*/



class new_person{
	private String username;
	private String disp_name;
	private String password;

	public new_person(String a, String b, String c){
		username=a;
		disp_name=b;
		password=c;
	}

	public void enter_person(){
		String d = String.format("%s,%s,%s,%d,%d,%s",username,password,disp_name,0,0,"No Status");
		try{
			BufferedWriter out= new BufferedWriter(new FileWriter("input",true));
			out.write(d);
			out.newLine();
			out.close();
			System.out.print("\nRegistration Successful. User "+username+" is created\n");
		}
		catch(Exception e){
			System.out.print("Exception occured"+e);
		}
		
	}
}

class sign_up{
	private String username;
	private String disp_name;
	private String password;

	public sign_up(String a, String b, String c){
		username=a;
		disp_name=b;
		password=c;
	}
	
	public int check_user(){
		int fail=0;
		try{
			BufferedReader in = new BufferedReader(new FileReader("input"));
			String person;
			while((person=in.readLine())!=null){
				String[] pers = person.split(",");
				if(pers[0].equals(username)){
					in.close();
					throw new UserAlreadyExists(username);
				}

			}
		}
		catch(UserAlreadyExists e){
				System.out.println(e);
				fail=1;
		}
		finally{
			
			return fail;

		}
		

	}
	public void create_user(){
		int fail=check_user();
		if(fail==0){
			new_person obj = new new_person(username,disp_name,password);
			obj.enter_person();
		}
	}


}

class login{
	private String username;
	private String password;
	public login(String a,String b){
		username= a;
		password= b;
	}
	public int check_login(){
		int check=0;
		try{
				BufferedReader in = new BufferedReader(new FileReader("input"));
				String person;
				while((person=in.readLine())!=null){
					String[] pers = person.split(",");
					if(pers[0].equals(username)){
						if(pers[1].equals(password)){
							check=1;
							break;
						}
					}

				}
				in.close();
				if(check!=1)
				throw new InvalidLoginException();
		}
		catch (InvalidLoginException e){
			System.out.println(e);

		}
		finally{
			return check;
		}
	
	}
}


class person{
	private String username;
	private String disp_name;
	private String password;
	private String status;
	private int friends;
	private ArrayList<String> frnds;
	private int friend_requests;
	private ArrayList<String> frnd_req;
	private String orig_user;
	private String[] user;
	public person(String a){
		username = a;
		frnds=new ArrayList<String>();
		frnd_req=new ArrayList<String>();
		try{
			BufferedReader in= new BufferedReader(new FileReader("input"));
			String pers;
			while((pers=in.readLine())!=null){
				user=pers.split(",");
				if(user[0].equals(username)){
					orig_user=pers;
					password= user[1];
					disp_name=user[2];
					friends = Integer.parseInt(user[3]);
					friend_requests = Integer.parseInt(user[3+friends+1]);
					int i;
					for(i=1;i<=friends;i++){
						frnds.add(user[3+i]);
					}

					for(i=1;i<=friend_requests;i++){
						frnd_req.add(user[3+friends+1+i]);
					}
					status = user[3+friends+friend_requests+2];
					in.close();
					break;
				} 
			}
			
			
		}
		catch (Exception e){
			System.out.print(e);
		}
	}
	public int get_no_friends(){
		return friends;
	}
	public String get_dispname(){
		return disp_name;
	}
	public String get_status(){
		return status;
	}
	public void get_friends(){
		if(friends==0){
			System.out.print("\nNo friends.\n");
		}
		else{
			System.out.print("\nYour friends are: ");
			int i;
			for(i=1;i<=friends;i++){
				System.out.print(user[3+i]+" ");
			}
			System.out.println("\n");
		}
	}

	public void search(String name){
		int net_check=0;
		if(name.equals(username)){
			System.out.print("\nKindly enter a different name.\n\n");
		}
		else{
			try{
				BufferedReader in= new BufferedReader(new FileReader("input"));
				String pers;
				while((pers=in.readLine())!=null){
					String[] arr2=pers.split(",");
					if(arr2[0].equals(name)){
						net_check=1;
						break;
					} 
				}
				in.close();
				
			throw new UserDoesNotExist(name);
			}
			catch (UserDoesNotExist e){
				System.out.println(e);
			}
			catch (FileNotFoundException e2){
				System.out.print("\n"+e2+"\n");
			}
			catch (IOException e3){
				System.out.print("\n"+e3+"\n");
			}

			if(net_check==1){
				int i;
				boolean is_friend= false;
				for(i=1;i<=friends;i++){
					if(user[3+i].equals(name)){
						is_friend= true;
						break;
					}
				}
				if(is_friend){
					System.out.print("\nYou and "+name+" are friends.\n");
					person b=new person(name);
					System.out.print("\nDisplay Name: "+b.get_dispname()+"\n");
					System.out.print("Status: "+b.get_status());
					System.out.print("\nFriends: ");
					
					for(i=0;i<b.get_no_friends();i++){
						if((b.user[4+i]).equals(username)){
							continue;
						}
						else{
							System.out.print(b.user[4+i]+" ");
						}
					}
					System.out.print("\n");
					System.out.print("Mutual Friends: ");
					mutual_friends(name);
					System.out.print("\n");
				}
				else{
					System.out.print("\n"+name+" is not a friend.\n");
					System.out.print("Mutual Friends: ");
					mutual_friends(name);
					shortest_connection(name);
					request(name);
				}
			}
		}
				
	}
	public void request(String name){
		person b=new person(name);
		
		if(frnd_req.contains(name)){
			System.out.print("\n"+name+" has already sent you a request. Check your pending requests.\n");
			System.out.print("\n\tb. Back\n");
			Scanner out= new Scanner(System.in);
			String opt= out.next();
			while(!(opt.toLowerCase().equals("b"))){
				System.out.print("\n\tb. Back\n");
				opt= out.next();
			}
		}
		else{
			if(b.frnd_req.contains(username)){
				System.out.print("\n\t1. Cancel Request");
				System.out.print("\n\tb. Back\n");
				Scanner out= new Scanner(System.in);
				String opt= out.next();
				while(!(opt.toLowerCase().equals("b"))&&!(opt.equals("1"))){
					System.out.print("\n\t1. Cancel Request");
					System.out.print("\n\tb. Back\n");
					opt= out.next();
				}
				if(opt.equals("1")){
					int i;
					for(i=0;i<b.friend_requests;i++){
						if(((b.frnd_req).get(i)).equals(username)){
							(b.frnd_req).remove(i);
							b.friend_requests-=1;
							update(b.orig_user,b.user_ret());
							break;
						}
					}
				}

			}
			else{
				System.out.print("\n\t1. Send Request");
				System.out.print("\n\tb. Back\n");
				Scanner out= new Scanner(System.in);
				String opt= out.next();
				while(!(opt.toLowerCase().equals("b"))&&!(opt.equals("1"))){
					System.out.print("\n\t1. Send Request");
					System.out.print("\n\tb. Back\n");
					opt= out.next();
				}
				if(opt.equals("1")){
					(b.frnd_req).add(username);
					b.friend_requests+=1;
					update(b.orig_user,b.user_ret());
					System.out.print("\nRequest Sent\n");
					request(name);
				}

			}
	}
	}
	public void pending_request(){
		if(frnd_req.size()==0){
			System.out.print("\nNo new friend requests.\n");
			System.out.print("\n\tb. Back\n");
			Scanner out=new Scanner(System.in);
			String opt=out.next();
			while(!(opt.toLowerCase().equals("b"))){
			
				System.out.print("\n\tb. Back\n");
				opt=out.next();
			}
		}
		else{
			int i;
			for(i=0;i<frnd_req.size();i++){
				System.out.print("\n\t"+(i+1)+". "+frnd_req.get(i)+"\n");
			}
			System.out.print("\n\tb. Back\n");
			Scanner out=new Scanner(System.in);
			String opt=out.next();
			while(!(opt.toLowerCase().equals("b"))&&!(Integer.parseInt(opt)>=1&&Integer.parseInt(opt)<=frnd_req.size())){
				for(i=0;i<frnd_req.size();i++){
					System.out.print("\n\t"+(i+1)+". "+frnd_req.get(i)+"\n");
				}
				System.out.print("\n\tb. Back\n");
				opt=out.next();
			}
			if(opt.toLowerCase().equals("b"))
			{
			//pending_request();
			return;
			}

			if(Integer.parseInt(opt)>=1&&Integer.parseInt(opt)<=frnd_req.size()){
				person b=new person(frnd_req.get(Integer.parseInt(opt)-1));
				System.out.print("\n"+b.disp_name+"\n");
				System.out.print("\t1. Accept\n");
				System.out.print("\t2. Reject\n");
				
				int option=out.nextInt();
				while(option!=1&&option!=2){
					System.out.print("\t1. Accept\n");
					System.out.print("\t2. Reject\n");
					
					option=out.nextInt();

				}
				if(option==1){
					friends+=1;
					frnds.add(b.username);
					b.friends+=1;
					b.frnds.add(username);
					for(i=0;i<frnd_req.size();i++){
						if((frnd_req.get(i)).equals(b.username)){
							frnd_req.remove(i);
							friend_requests-=1;
							update(orig_user,user_ret());
							break;
						}
					}

					update(b.orig_user,b.user_ret());
					
					System.out.print("\nYou are now friends with "+b.username+"\n");

				}
				else if(option==2){
					
					
					for(i=0;i<frnd_req.size();i++){
						if((frnd_req.get(i)).equals(b.username)){
							frnd_req.remove(i);
							friend_requests-=1;
							update(orig_user,user_ret());
							break;
						}
					}
					System.out.print("\nFriend request by "+b.username+" deleted\n");
					

				}


			}
			pending_request();
	}


	}
	public void mutual_friends(String name){
		person b= new person(name);
		ArrayList<String> arr=new ArrayList<String>();
		int friends1= get_no_friends();
		int friends2= b.get_no_friends();
		int i,j;
		for(i=1;i<=friends1;i++){
			for(j=1;j<=friends2;j++){
				if(user[3+i].equals(b.user[3+j])){
					arr.add(user[3+i]);
					break;
				}
			}
		}
		if(arr.size()==0){
			System.out.print(" No Mutual Friends.\n");
		}
		else{
			for(i=0;i<arr.size();i++){
				System.out.print(arr.get(i)+ " ");
			}
			System.out.print("\n");
		}



	}


	public void shortest_connection(String name)
	{
	Hashtable mm=new Hashtable();
	Hashtable mmrev=new Hashtable();
	int count=0;
	try{
			BufferedReader in = new BufferedReader(new FileReader("input"));
			String person;
			while((person=in.readLine())!=null){
				String[] pers = person.split(",");
				mm.put(pers[0], count++);
				mmrev.put(count-1, pers[0]);
			}
				in.close();
		}
		catch(Exception e){
				System.out.println(e);
				//fail=1;
		}
LinkedList<Integer> adj[];
 adj = new LinkedList[count];
 for (int i=0; i<count; ++i)
            adj[i] = new LinkedList();
count=0;

try{
			BufferedReader in = new BufferedReader(new FileReader("input"));
			String person;
			while((person=in.readLine())!=null){
				String[] pers = person.split(",");
				int y=Integer.parseInt(pers[3]);
				int u;
				for(u=4;u<4+y;u++)
					(adj[count]).add((int)mm.get(pers[u]));
				count++;
			}
				in.close();
		}
		catch(Exception e){
				System.out.print(e);
				//fail=1;
		}

visit visi[]=new visit[count];
for(int i=0; i<count;i++)
visi[i]=new visit();
int s=(int)mm.get(username);
LinkedList<Integer> queue = new LinkedList<Integer>();
visi[s].visited=true;
        queue.add(s);
int n=0;

while (queue.size() != 0)
        {
visi[s].path.add(mmrev.get(s));
           
            s = queue.poll();
           // System.out.print(mmrev.get(s)+" ");
 		            // Get all adjacent vertices of the dequeued vertex s
           
            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext())
            {
                 n = i.next();
                if (!visi[n].visited)
                {
                    visi[n].visited = true;
			visi[n].path.addAll(visi[s].path);
			
			visi[n].path.add(mmrev.get(n));
			if(mmrev.get(n).equals(name))
break;
                    queue.add(n);
                }
            }
if(mmrev.get(n).equals(name))
{
System.out.print("\nShortest route : "/*visi[n].path*/);
int j;
for(j=0;j<visi[n].path.size();j++)
{
if(j!=visi[n].path.size()-1)
System.out.print(visi[n].path.get(j)+" -> ");
else
System.out.print(visi[n].path.get(j));

}
System.out.println();
break;
}

        }
if(mmrev.get(n).equals(name))
return;
else
System.out.println("No connections");

	}


	public void status_update(String stat){
		
		status= stat;
		user[3+friends+friend_requests+2]=status;
		update(orig_user,user_ret());
	}

	public String user_ret(){
		String new2;
		new2= username+","+password+","+disp_name+","+String.valueOf(friends)+",";
		int i;
		for(i=0;i<friends;i++){
			new2+=frnds.get(i);
			new2+=",";
		}
		new2+=String.valueOf(friend_requests)+",";
		for(i=0;i<friend_requests;i++){
			new2+=frnd_req.get(i);
			new2+=",";
		}

		new2+=status;
		return new2;
	}

	public void update(String a,String b){   //a: to replace; b: to be replaced with
		try{
			BufferedReader in = new BufferedReader(new FileReader("input"));
			String line = "", oldtext = "";
			while((line = in.readLine()) != null){
			    oldtext += line + System.lineSeparator();
			}
			in.close();
			 
			String newtext = oldtext.replace(a, b);
//System.out.println(oldtext);
//System.out.println(newtext);
			BufferedWriter out= new BufferedWriter(new FileWriter("input"));
			out.write(newtext);
			out.close();

			}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}



}
class network{
	 void first_page(){
		Scanner out = new Scanner(System.in);
		System.out.print("\nReading Database File...\n");
		System.out.print("Network is Ready...\n\n");
		System.out.print("\t 1. Sign Up\n\t 2. Login\n\t 3. Exit\n\n");
		int choice = out.nextInt();
		while(choice!=3){
			if(choice==1){
				System.out.print("\nEnter Username: ");
				String username= out.next();
				System.out.print("Enter Display Name: ");
				String disp_name= out.next();
				System.out.print("Password: ");
				String password= out.next();
				sign_up create= new sign_up(username, disp_name, password);
				create.create_user();
				
			}
			else if(choice==2){
				System.out.print("\nPlease Enter Your Username: ");
				String username= out.next();
				System.out.print("Please Enter Your Password: ");
				String password= out.next();
				login user = new login(username, password);
				int check= user.check_login();
				if(check==1){
					person a= new person(username);
					System.out.print("\n"+a.get_dispname()+" logged in now.\n");
					System.out.print(a.get_status()+"\n\n");
					System.out.print("\t 1. List Friends\n\t 2. Search\n\t 3. Update Status\n\t 4. Pending Request\n\t 5. Logout \n\n");
					int choice2=out.nextInt();
					while(choice2!=5){
						if(choice2==1){
							a.get_friends();
						}
						else if(choice2==2){
							System.out.print("\nEnter Name: ");
							String name= out.next();
							a.search(name);
							//System.out.print("\tb. Back \n");
							//String back= out.next();
							//while(!(back.toLowerCase().equals("b"))){
							//	System.out.print("\n\tb. Back \n");
							//	back= out.next();
							//}
						}
						else if(choice2==3){
							Scanner out2 = new Scanner(System.in);
							System.out.print("\nEnter Status: ");
							String status=out2.nextLine();
							a.status_update(status);
							System.out.print("\nStatus Updated!!!\n\n");
						}
						else if(choice2==4){
							a.pending_request();

						}



					System.out.print("\t 1. List Friends\n\t 2. Search\n\t 3. Update Status\n\t 4. Pending Request\n\t 5. Logout \n\n");
					choice2 = out.nextInt();
					}

					System.out.print("\n"+username+" logged out successfully.\n\n");
				}


			}
		System.out.print("\n\t 1. Sign Up\n\t 2. Login\n\t 3. Exit\n\n");
		choice = out.nextInt();
		}
	}

}


class visit
{
	boolean visited;
	LinkedList path;
	visit()
	{
		path=new LinkedList();
	}
}

class UserAlreadyExists extends Exception
{
String a;
 UserAlreadyExists(String s) {
      a=s;

   }
public String toString()
{
return "\nSorry "+a+" already exists.\n";

}
}

class InvalidLoginException extends Exception
{
/*String a;
 InvalidLoginExce(String s) {
      a=s;

   }*/
public String toString()
{
return "\nInvalid login. Try again:(\n";

}
}

class UserDoesNotExist extends Exception
{
String a;
 UserDoesNotExist(String s) {
      a=s;

   }
public String toString()
{
return "\nSorry "+a+" does not exist.\n";

}
}

class startAccount
{
public static void main(String ar[])
{
network ntk=new network();
ntk.first_page();
}
}

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class TestPriorityQueue {
	
	public static class Patient implements Comparable<Patient>{
		String name;
		int priority;
		
		public Patient(String name, int priority) {
			this.name = name;
			this.priority = priority;
		}
		
		public int getPriority() {
			return priority;
		}

		@Override
		public int compareTo(Patient o) {
			if(this.getPriority() > o.getPriority()) {
	            return 1;
	        } else if (this.getPriority() < o.getPriority()) {
	            return -1;
	        } else {
	            return 0;
	        }
		}
		
		@Override
		public String toString() {
			return name + "(" + priority + ")";
		}
	}

	public static void main(String[] args) {
		List<Patient> patients = new ArrayList<TestPriorityQueue.Patient>();
		patients.add(new Patient("Atul", 4));
		patients.add(new Patient("Anjana", 3));
		patients.add(new Patient("Alen", 1));
		patients.add(new Patient("Evan", 1));
		
		System.out.println("Queue");
		PriorityQueue<Patient> patientQueue = new PriorityQueue<TestPriorityQueue.Patient>(patients);
		while (!patientQueue.isEmpty()) {
            System.out.println(patientQueue.poll());
        }
		
		System.out.println("TreeSet");
		TreeSet<Patient> patientSet = new TreeSet<TestPriorityQueue.Patient>(patients);
		while (!patientSet.isEmpty()) {
            System.out.println(patientSet.pollFirst());
        }
		

	}

}

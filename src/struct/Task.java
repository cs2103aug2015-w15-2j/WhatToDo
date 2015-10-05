/**
 * This class defines the Task object and its methods used in the application
 *
 * @author Adrian
 */

package struct;

public class Task extends Data{

    private Date deadline;
    private boolean isFloating;
    
	//============================================
	// Constructors
	//============================================

    public Task() {
        super(); 
        this.deadline = new Date();
        this.isFloating = true;
    }
   
	public Task(String name, boolean isDone, Date deadline, boolean isFloating) {
		super(name, isDone);
		this.deadline = deadline;
		this.isFloating = isFloating;
	}

	//============================================
	// Getters
	//============================================
    public Date getDeadline() {
        return deadline;
    }

    public boolean getIsFloating() {
        return isFloating;
    }

	//============================================
	// Setters
	//============================================
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setIsFloating(boolean isFloating) {
        this.isFloating = isFloating;
    }
}


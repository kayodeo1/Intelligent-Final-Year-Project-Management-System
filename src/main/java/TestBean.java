import java.io.Serializable;

import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class TestBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private String inputValue;
	private String outputValue;

	
	public String getInputValue() {
		return inputValue;
	}
	
	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}
	
	
	public String getOutputValue() {
		return outputValue;
	}
	
	public void setOutputValue(String outputValue) {
		this.outputValue = outputValue;
	}
	
	public void submit() {
		
		System.out.println("submited !");
		this.outputValue = this.inputValue;
	}

}
